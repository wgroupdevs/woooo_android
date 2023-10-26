package com.woooapp.meeting.impl.views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.woooapp.meeting.impl.views.adapters.PeerAdapter;
import com.woooapp.meeting.impl.vm.EdiasProps;
import com.woooapp.meeting.impl.vm.MeProps;
import com.woooapp.meeting.impl.vm.RoomProps;
import com.woooapp.meeting.lib.PeerConnectionUtils;
import com.woooapp.meeting.lib.RoomClient;
import com.woooapp.meeting.lib.RoomOptions;
import com.woooapp.meeting.lib.lv.RoomStore;
import com.woooapp.meeting.lib.model.Me;
import com.woooapp.meeting.lib.model.Notify;
import com.woooapp.meeting.lib.model.Peer;

import eu.siacs.conversations.R;
import eu.siacs.conversations.databinding.ActivityRoomBinding;

import static com.woooapp.meeting.impl.utils.ClipboardCopy.clipboardCopy;
import static com.woooapp.meeting.lib.Utils.getRandomString;

import org.mediasoup.droid.Logger;
import org.mediasoup.droid.MediasoupClient;

import java.util.List;

public class RoomActivity extends AppCompatActivity {

    public static final String TAG = RoomActivity.class.getCanonicalName();
    private static final int REQUEST_CODE_SETTING = 1;
    private String mRoomId;
    private String mPeerId;
    private String mDisplayName;

    private boolean mForceH264;
    private boolean mForceVP9;

    private RoomOptions mOptions;
    private RoomStore mRoomStore;
    private RoomClient mRoomClient;

    private ActivityRoomBinding mBinding;
    private PeerAdapter mPeerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_room);
        createRoom();
        checkPermission();
    }

    private void createRoom() {
        mOptions = new RoomOptions();
        loadRoomConfig();

        mRoomStore = new RoomStore();
        initRoomClient();

        getViewModelStore().clear();
        initViewModel();
    }

    private void loadRoomConfig() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Room initial config.
        mRoomId = preferences.getString("roomId", "");
        mPeerId = preferences.getString("peerId", "");
        mDisplayName = preferences.getString("displayName", "");
        mForceH264 = preferences.getBoolean("forceH264", false);
        mForceVP9 = preferences.getBoolean("forceVP9", false);
        if (TextUtils.isEmpty(mRoomId)) {
            mRoomId = getRandomString(8);
            preferences.edit().putString("roomId", mRoomId).apply();
        }
        if (TextUtils.isEmpty(mPeerId)) {
            mPeerId = getRandomString(8);
            preferences.edit().putString("peerId", mPeerId).apply();
        }
        if (TextUtils.isEmpty(mDisplayName)) {
            mDisplayName = getRandomString(8);
            preferences.edit().putString("displayName", mDisplayName).apply();
        }

        // Room action config.
        mOptions.setProduce(preferences.getBoolean("produce", true));
        mOptions.setConsume(preferences.getBoolean("consume", true));
        mOptions.setForceTcp(preferences.getBoolean("forceTcp", false));
        mOptions.setUseDataChannel(preferences.getBoolean("dataChannel", true));

        // Device config.
        String camera = preferences.getString("camera", "front");
        PeerConnectionUtils.setPreferCameraFace(camera);

        // Display version number.
        ((TextView)findViewById(R.id.version)).setText(String.valueOf(MediasoupClient.version()));
    }

    private void initRoomClient() {
        mRoomClient =
                new RoomClient(
                        this, mRoomStore, mRoomId, mPeerId, mDisplayName, mForceH264, mForceVP9, mOptions);
    }

    private void initViewModel() {
        EdiasProps.Factory factory = new EdiasProps.Factory(getApplication(), mRoomStore);

        // Room.
//        RoomProps roomProps = ViewModelProviders.of(this, factory).get(RoomProps.class);
        RoomProps roomProps = new ViewModelProvider(this, factory).get(RoomProps.class);
        roomProps.connect(this);
        mBinding.invitationLink.setOnClickListener(
                v -> {
                    String linkUrl = roomProps.getInvitationLink().get();
                    clipboardCopy(getApplication(), linkUrl, R.string.invite_link_copied);
                });
        mBinding.setRoomProps(roomProps);

        // Me.
        MeProps meProps = new ViewModelProvider(this, factory).get(MeProps.class);
        meProps.connect(this);
//        mBinding.me.setProps(meProps, mRoomClient);

        mBinding.hideVideos.setOnClickListener(
                v -> {
                    Me me = meProps.getMe().get();
                    if (me != null) {
                        if (me.isAudioOnly()) {
                            mRoomClient.disableAudioOnly();
                        } else {
                            mRoomClient.enableAudioOnly();
                        }
                    }
                });
        mBinding.muteAudio.setOnClickListener(
                v -> {
                    Me me = meProps.getMe().get();
                    if (me != null) {
                        if (me.isAudioMuted()) {
                            mRoomClient.unmuteAudio();
                        } else {
                            mRoomClient.muteAudio();
                        }
                    }
                });
        mBinding.restartIce.setOnClickListener(v -> mRoomClient.restartIce());

        // Peers.
        mPeerAdapter = new PeerAdapter(mRoomStore, this, mRoomClient);
        mBinding.remotePeers.setLayoutManager(new LinearLayoutManager(this));
        mBinding.remotePeers.setAdapter(mPeerAdapter);
        mRoomStore
                .getPeers()
                .observe(
                        this,
                        peers -> {
                            List<Peer> peersList = peers.getAllPeers();
                            if (peersList.isEmpty()) {
                                mBinding.remotePeers.setVisibility(View.GONE);
                                mBinding.roomState.setVisibility(View.VISIBLE);
                            } else {
                                mBinding.remotePeers.setVisibility(View.VISIBLE);
                                mBinding.roomState.setVisibility(View.GONE);
                            }
                            mPeerAdapter.replacePeers(peersList);
                        });

        // Notify
        final Observer<Notify> notifyObserver =
                notify -> {
                    if (notify == null) {
                        return;
                    }
                    if ("error".equals(notify.getType())) {
                        Toast toast = Toast.makeText(this, notify.getText(), Toast.LENGTH_LONG);
                        TextView toastMessage = toast.getView().findViewById(android.R.id.message);
                        toastMessage.setTextColor(Color.RED);
                        toast.show();
                    } else if ("message".equals(notify.getType())) {
                        Toast.makeText(this, notify.getTitle() + "\n" + notify.getText(), Toast.LENGTH_LONG)
                                .show();
                    } else {
                        Toast.makeText(this, notify.getText(), Toast.LENGTH_LONG).show();
                    }
                };
        mRoomStore.getNotify().observe(this, notifyObserver);

        // Chat
        mBinding.chatInput.setImeActionLabel("Send", KeyEvent.KEYCODE_ENTER);
        mBinding.chatInput.setOnKeyListener(
                (v, keyCode, event) -> {
                    String message = mBinding.chatInput.getText().toString().trim();
                    if(TextUtils.isEmpty(message)){
                        return false;
                    }
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        if (message.matches("^@bot (.*)")) {
                            mRoomClient.sendBotMessage(message);
                        } else {
                            mRoomClient.sendChatMessage(message);
                        }
                        mBinding.chatInput.getText().clear();
                    }
                    return true;
                });
    }

    private PermissionHandler permissionHandler =
            new PermissionHandler() {
                @Override
                public void onGranted() {
                    Logger.d(TAG, "permission granted");
                    if (mRoomClient != null) {
                        mRoomClient.join();
                    }
                }
            };

    private void checkPermission() {
        String[] permissions = {
//                Manifest.permission.INTERNET,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        String rationale = "Please provide permissions";
        Permissions.Options options =
                new Permissions.Options().setRationaleDialogTitle("Info").setSettingsDialogTitle("Warning");
        Permissions.check(this, permissions, null, null, permissionHandler);
    }

    private void destroyRoom() {
        if (mRoomClient != null) {
            mRoomClient.close();
            mRoomClient = null;
        }
        if (mRoomStore != null) {
            mRoomStore = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.room_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.setting) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SETTING);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_SETTING) {
            Logger.d(TAG, "request config done");
            // close, dispose room related and clear store.
            destroyRoom();
            // local config and reCreate room related.
            createRoom();
            // check permission again. if granted, join room.
            checkPermission();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyRoom();
    }

    @Override
    public void onBackPressed() {
        destroyRoom();
        super.onBackPressed();
    }

} /** end class. */