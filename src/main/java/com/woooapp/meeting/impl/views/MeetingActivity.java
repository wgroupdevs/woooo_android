package com.woooapp.meeting.impl.views;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.VolleyError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.android.material.snackbar.Snackbar;
import com.woogroup.woooo_app.woooo.di.WooApplication;
import com.woooapp.meeting.device.Display;
import com.woooapp.meeting.impl.utils.WooEvents;
import com.woooapp.meeting.impl.views.adapters.MeetingGridAdapter;
import com.woooapp.meeting.impl.views.adapters.ScreenPagerAdapter;
import com.woooapp.meeting.impl.views.models.GridPeer;
import com.woooapp.meeting.impl.vm.EdiasProps;
import com.woooapp.meeting.impl.vm.MeProps;
import com.woooapp.meeting.impl.vm.RoomProps;
import com.woooapp.meeting.lib.MeetingClient;
import com.woooapp.meeting.lib.RoomOptions;
import com.woooapp.meeting.lib.Utils;
import com.woooapp.meeting.lib.lv.RoomStore;
import com.woooapp.meeting.lib.model.Peer;
import com.woooapp.meeting.net.ApiManager;
import com.woooapp.meeting.net.models.CreateMeetingBody;
import com.woooapp.meeting.net.models.CreateMeetingResponse;
import com.woooapp.meeting.net.models.PutMembersDataBody;
import com.woooapp.meeting.net.models.PutMembersDataResponse;
import com.woooapp.meeting.net.models.RoomData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import eu.siacs.conversations.R;
import eu.siacs.conversations.databinding.ActivityMeetingBinding;
import woooo_app.woooo.shared.components.view_models.UserPreferencesViewModel;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 6:36 pm 11/09/2023
 * <code>class</code> MeetingActivity.java
 */
public class MeetingActivity extends AppCompatActivity implements Handler.Callback {

    private static final String TAG = MeetingActivity.class.getSimpleName().toUpperCase(Locale.ROOT);

    private static final int PERMISSIONS_REQ_CODE = 0x7b;

    private UserPreferencesViewModel userPreferences;

    private final String[] permissions = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private ViewPager mViewPager;
    //    private ActivityMeetBinding mBinding;
    private ActivityMeetingBinding mBinding;
    private ScreenPagerAdapter mPeerScreenAdapter;
    private MeetingClient mMeetingClient;
    private ProgressDialog pd;
    private CreateMeetingResponse createMeetingResponse;
    private PutMembersDataResponse putMembersDataResponse;
    private RoomOptions mOptions;
    private RoomStore mRoomStore;
    private boolean joining = false;
    private String email;
    private String username;
    private String accountUniqueId;
    private String picture;
    private String mMeetingId;
    private String meetingName;
    private MeetingGridAdapter peerGridAdapter;
    private List<Peer> peersList = new ArrayList<>();
    private List<GridPeer> gridPeerList = new ArrayList<>();
    private final Handler callbackHandler = new Handler(this);
    private int deviceWidthDp;
    private int deviceHeightDp;
    private String peerDisplayName = "";

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_meeting);

        WooEvents.getInstance().addHandler(callbackHandler);

        deviceWidthDp = Display.getDisplayWidth(this) - 50;
        deviceHeightDp = Display.getDisplayHeight(this);

        mBinding.bottomBarMeeting.setVisibility(View.GONE);

        pd = new ProgressDialog(this);
        pd.setMessage(" ...");
        pd.setCancelable(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkForPermissions(permissions, PERMISSIONS_REQ_CODE);
        } else {
            setup();
        }

        mBinding.meetingButtonEnd.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void setup() {
        pd.show();

        this.email = getIntent().getStringExtra("email");
        this.accountUniqueId = getIntent().getStringExtra("accountUniqueId");
        this.username = getIntent().getStringExtra("username");
        if (username != null) {
            if (username.isEmpty())
                username = Utils.getRandomString(8);
            else if (username.length() < 4) {
                username = username + Utils.getRandomString(6);
            }
        }
        this.picture = getIntent().getStringExtra("picture");
        if (picture != null) {
            if (picture.isEmpty())
                picture = "https://picsum.com/200";
        } else {
            picture = "https://picsum.com/200";
        }
        this.joining = getIntent().getBooleanExtra("joining", false);
        this.mMeetingId = getIntent().getStringExtra("meetingId");
        this.meetingName = getIntent().getStringExtra("meetingName");
        if (meetingName != null) {
            if (meetingName.isEmpty()) {
                meetingName = "Droid-" + Utils.getRandomString(4);
            }
        } else {
            meetingName = "Droid-" + Utils.getRandomString(4);
        }
        Log.d(TAG, "<< MEETING ID [" + mMeetingId + "]");
        if (!joining) {
            CreateMeetingBody.Admin admin1 = new CreateMeetingBody.Admin();
            admin1.setEmail(email);
            admin1.setAccountUniqueId(accountUniqueId);
            admin1.setUsername(username);
            admin1.setPicture(picture);
            List<CreateMeetingBody.Admin> admins = new LinkedList<>();
            admins.add(admin1);
            CreateMeetingBody body = new CreateMeetingBody();
            body.setMeetingId(mMeetingId);
            body.setMeetingName(meetingName);
            body.setAdmins(admins);

            // Create new meeting
            createMeeting(body);
        } else {
            // Join Meeting
            joinMeeting();
            dismissProgressDialog();
        }
    }

    private void joinMeeting() {
        initMeetingClient();
    }

    private void fetchRoomData() {
        ApiManager.build(this).fetchRoomData(mMeetingId, new ApiManager.ApiResult() {
            @Override
            public void onResult(Object response) {
                if (response != null) {
                    Log.d(TAG, "ROOM DATA RESPONSE >>> " + response);
                    initMeetingClient();
                }
                dismissProgressDialog();
            }

            @Override
            public void onFailure(VolleyError error) {
                Log.e(TAG, "Error while fetching room data " + error.toString());
                dismissProgressDialog();
            }
        });
    }

    /**
     * @param body
     */
    private void createMeeting(CreateMeetingBody body) {
        try {
            ApiManager.build(this).fetchCreatePreMeeting(body.toJson(), new ApiManager.ApiResult() {
                @Override
                public void onResult(Object response) {
                    if (response != null) {
                        try {
                            Log.d(TAG, "Pre Meeting Response >>> " + response);
                            createMeetingResponse = CreateMeetingResponse.fromJson(response.toString());
                            fetchRoomData();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(VolleyError error) {
                    Log.e(TAG, "Error on createMeeting()" + error.toString());
                    dismissProgressDialog();
                }
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void putMember() throws JsonProcessingException {
        PutMembersDataBody body = new PutMembersDataBody();
        body.setEmail(this.email);
        body.setAccountUniqueId(this.accountUniqueId);
        body.setUsername(this.username);
        body.setPicture(this.picture);
        body.setSocketId(mMeetingClient.getSocketId());

        ApiManager.build(this).
                putMembersData(body.toJson(),
                        mMeetingId,
                        new ApiManager.ApiResult() {
                            @Override
                            public void onResult(Object response) {
                                if (response != null) {
                                    Log.d(TAG, "RESPONSE PUT Member >>> " + response);
                                    try {
                                        putMembersDataResponse = PutMembersDataResponse.fromJson(response.toString());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(VolleyError error) {
                                Log.e(TAG, "Error while putting members data " + error.toString());
                            }
                        });
    }

    private void initMeetingClient() {
        this.mRoomStore = new RoomStore();
        this.mMeetingClient = new MeetingClient(this, mRoomStore, this.mMeetingId);
        this.mMeetingClient.setAccountUniqueId(accountUniqueId);
        this.mMeetingClient.setEmail(email);
        this.mMeetingClient.setUsername(username);
        this.mMeetingClient.setPicture(picture);
        this.mMeetingClient.start();

        getViewModelStore().clear();
        initViewModel();
    }

    private void initViewModel() {
        EdiasProps.Factory factory = new EdiasProps.Factory(WooApplication.Companion.getSharedInstance(), mRoomStore);

        RoomProps roomProps = new ViewModelProvider(this, factory).get(RoomProps.class);
        roomProps.connect(this);

        mBinding.setMeetProps(roomProps);

        // Set Meeting ID Link
        mBinding.tvMeetingId.setText(mMeetingId);

        // PeerView
        peerGridAdapter = new MeetingGridAdapter(this, this, mRoomStore, mMeetingClient);
        mBinding.gridViewMeeting.setAdapter(peerGridAdapter);
        mBinding.gridViewMeeting.setEnabled(false);

        mRoomStore
                .getPeers()
                .observe(
                        this,
                        peers -> {
                            peersList = peers.getAllPeers();
                            gridPeerList = createGridPeers(peersList);
                            Log.d(TAG, "Peer list size[" + peersList.size() + "]");
                            if (peersList.size() < 2) {
                                mBinding.gridViewMeeting.setNumColumns(1);
                                mBinding.gridViewMeeting.setColumnWidth(deviceWidthDp);
                            } else if (peersList.size() > 1 && peersList.size() < 5) {
                                mBinding.gridViewMeeting.setNumColumns(2);
                                mBinding.gridViewMeeting.setColumnWidth((deviceWidthDp / 2));
                            }
                            peerGridAdapter.replacePeers(gridPeerList);
                        });
    }

    /**
     * @param peerList
     * @return
     */
    private List<GridPeer> createGridPeers(List<Peer> peerList) {
        gridPeerList.clear();
        List<GridPeer> peers = new LinkedList<>();
        peers.add(new GridPeer(GridPeer.VIEW_TYPE_ME, null));
        for (Peer p : peerList) {
            peers.add(new GridPeer(GridPeer.VIEW_TYPE_PEER, p));
        }
        return peers;
    }

    private void dismissProgressDialog() {
        runOnUiThread(() -> {
            pd.dismiss();
        });
    }

    /**
     * @param permissions
     * @param reqCode
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void checkForPermissions(String[] permissions, int reqCode) {
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            sendRequest(permissions, reqCode);
        } else {
            setup();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void sendRequest(String[] permissions, int reqCode) {
        if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) &&
                shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {

        } else {
            requestPermissions(permissions, reqCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQ_CODE) {
            if (grantResults.length >= 3) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    setup();
                }
            } else {
                checkForPermissions(permissions, PERMISSIONS_REQ_CODE);
            }
        }
    }

    public void destroyMeeting() {
        if (this.mMeetingClient != null) {
            this.mMeetingClient.close();
            this.mMeetingClient = null;
        }
        if (mRoomStore != null) {
            mRoomStore = null;
        }
//        mBinding.meView.dispose();
        // TODO Mark check
//        mBinding.peerView.dispose();
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case WooEvents.EVENT_TYPE_SOCKET_ID:
                Log.d(TAG, "<< Handler Event SOCKET_ID Received >> " + msg.obj);
                return true;
            case WooEvents.EVENT_TYPE_PRODUCER_CREATED:
                Log.d(TAG, "<< Handler Event PRODUCER CREATED [" + msg.obj + "]");
                if (joining) {
                    try {
                        putMember();
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(() -> {
                    String txt = "New Meeting Started ";
                    if (joining) {
                        txt = "Joined Meeting ";
                    }
                    Snackbar.make(mBinding.meetingButtonEnd, txt + mMeetingClient.getMeetingId(), Snackbar.LENGTH_LONG).show();
                    mBinding.tvMeetingId.setVisibility(View.GONE);
                    mBinding.bottomBarMeeting.setVisibility(View.VISIBLE);
                });
                return true;
            case WooEvents.EVENT_TYPE_CONSUMER_CREATED:
                Log.d(TAG, "<< Handler Event CONSUMER CREATED [" + msg.obj + "]");
                return true;
            case WooEvents.EVENT_TYPE_CONSUME_BACK_CREATED:
                Log.d(TAG, "<< Handler Event CONSUME BACK CREATED [" + msg.obj + "]");
                return true;
            case WooEvents.EVENT_TYPE_PEER_DISCONNECTED:
                Log.d(TAG, "<< Handler Event Peer Disconnected with socket id >> " + msg.obj);
                return true;
            case WooEvents.EVENT_TYPE_SOCKET_DISCONNECTED:
                Log.d(TAG, "<< Handler Event Socket disconnected with socket id: [" + msg.obj + "]");
                if (peersList.isEmpty()) {
                    // Destroy and finish()
                    onBackPressed();
                }
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyMeeting();
    }

    @Override
    public void onBackPressed() {
        new Handler().postDelayed(() -> {
            WooEvents.getInstance().removeHandler(callbackHandler);
            destroyMeeting();
            super.onBackPressed();
            finish();
        }, 5000);
    }

} // end class
