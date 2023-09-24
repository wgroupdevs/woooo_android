package com.woooapp.meeting.impl.views;

import static com.woooapp.meeting.lib.Utils.getRandomString;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.ColorFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import com.woooapp.meeting.impl.views.adapters.ScreenPagerAdapter;
import com.woooapp.meeting.impl.vm.EdiasProps;
import com.woooapp.meeting.impl.vm.MeProps;
import com.woooapp.meeting.impl.vm.PeerProps;
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

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import eu.siacs.conversations.R;
import eu.siacs.conversations.databinding.ActivityMeetBinding;
import woooo_app.woooo.shared.components.view_models.UserPreferencesViewModel;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 6:36 pm 11/09/2023
 * <code>class</code> MeetingActivity.java
 */
public class MeetingActivity extends AppCompatActivity {

    private static final String TAG = MeetingActivity.class.getSimpleName().toUpperCase(Locale.ROOT);

    private static final int PERMISSIONS_REQ_CODE = 0x7b;

    private UserPreferencesViewModel userPreferences;

    private final String[] permissions = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private ViewPager mViewPager;
    private ActivityMeetBinding mBinding;
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
    // TODO only 1 peer for now
    private Peer mPeer;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_meet);

        mBinding.meetingButtonExit.hide();

        pd = new ProgressDialog(this);
        pd.setMessage("Setting up ...");
        pd.setCancelable(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkForPermissions(permissions, PERMISSIONS_REQ_CODE);
        } else {
            setup();
        }

        mBinding.meetingButtonExit.setOnClickListener(view -> {
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
        new Handler().postDelayed(() -> {
            try {
                putMember();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }, 8000);
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
        this.mMeetingClient.setUsername(username);
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
        mBinding.tvTempLink.setText(mMeetingId);

        // ME
        MeProps meProps = new ViewModelProvider(this, factory).get(MeProps.class);
        meProps.connect(this);

        mBinding.meView.setProps(meProps, mMeetingClient);

        // PeerView
        PeerProps peerProps = new PeerProps(getApplication(), mRoomStore);

        mRoomStore
                .getPeers()
                .observe(
                        this,
                        peers -> {
                            List<Peer> peersList = peers.getAllPeers();
                            Log.d(TAG, "Peer list size[" + peersList.size() + "]");
                            if (peersList.isEmpty()) {
//                                mBinding.remotePeers.setVisibility(View.GONE);
//                                mBinding.roomState.setVisibility(View.VISIBLE);
                                mBinding.peerView.setVisibility(View.GONE);
                            } else {
                                mBinding.peerView.setVisibility(View.VISIBLE);
//                                mBinding.remotePeers.setVisibility(View.VISIBLE);
//                                mBinding.roomState.setVisibility(View.GONE);
                                mPeer = peersList.get(0);
                                if (mPeer != null) {
                                    Log.d(TAG,
                                            "Connecting Peer >> id["
                                                    + mPeer.getId() +
                                                    "] consumerCount[" +
                                                    mPeer.getConsumers().size() + "]");
                                    peerProps.connect(this, mPeer.getId());
                                    mBinding.peerView.setProps(peerProps, mMeetingClient);
                                }
                            }
//                            mPeerAdapter.replacePeers(peersList);
                        });

        // TODO Move to events
        new Handler().postDelayed(() -> {
            mBinding.meetingButtonExit.show();
        }, 3000);
        new Handler().postDelayed(() -> {
            Snackbar.make(mBinding.meetingButtonExit, "Room Created " + mMeetingClient.getMeetingId(), Snackbar.LENGTH_LONG).show();
        }, 4500);
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
        mBinding.meView.dispose();
        mBinding.peerView.dispose();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyMeeting();
    }

    @Override
    public void onBackPressed() {
        destroyMeeting();
        super.onBackPressed();
        finish();
    }

} // end class
