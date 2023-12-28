package com.woooapp.meeting.impl.views;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.viewpager.widget.ViewPager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.woooapp.meeting.device.Display;
import com.woooapp.meeting.impl.utils.VoiceDataSource;
import com.woooapp.meeting.impl.utils.WDirector;
import com.woooapp.meeting.impl.utils.WEvents;
import com.woooapp.meeting.impl.utils.WTonePlayer;
import com.woooapp.meeting.impl.utils.WVoicePlayback;
import com.woooapp.meeting.impl.views.adapters.LangSelectionAdapter;
import com.woooapp.meeting.impl.views.adapters.MeetingChatAdapter;
import com.woooapp.meeting.impl.views.adapters.MeetingPagerAdapter;
import com.woooapp.meeting.impl.views.adapters.MeetingTranscriptAdapter;
import com.woooapp.meeting.impl.views.adapters.MemberAdapter;
import com.woooapp.meeting.impl.views.adapters.PeerAdapter2;
import com.woooapp.meeting.impl.views.animations.WooAnimationUtil;
import com.woooapp.meeting.impl.views.fragments.MeetingPageFragment2;
import com.woooapp.meeting.impl.views.models.Chat;
import com.woooapp.meeting.impl.views.models.GridPeer;
import com.woooapp.meeting.impl.views.models.Languages;
import com.woooapp.meeting.impl.views.models.ListGridPeer;
import com.woooapp.meeting.impl.views.models.Member;
import com.woooapp.meeting.impl.views.models.Transcript;
import com.woooapp.meeting.impl.views.popups.MeetingMorePopup;
import com.woooapp.meeting.impl.views.popups.WooCommonPopup;
import com.woooapp.meeting.impl.views.popups.WooProgressDialog;
import com.woooapp.meeting.impl.vm.PeerProps;
import com.woooapp.meeting.impl.vm.RoomProps;
import com.woooapp.meeting.lib.MeetingClient;
import com.woooapp.meeting.lib.RoomOptions;
import com.woooapp.meeting.lib.ScreenCaptureService;
import com.woooapp.meeting.lib.Utils;
import com.woooapp.meeting.lib.lv.RoomStore;
import com.woooapp.meeting.lib.model.Peer;
import com.woooapp.meeting.net.ApiManager;
import com.woooapp.meeting.net.models.ChatTranslation;
import com.woooapp.meeting.net.models.CreateMeetingBody;
import com.woooapp.meeting.net.models.CreateMeetingResponse;
import com.woooapp.meeting.net.models.PutMembersDataBody;
import com.woooapp.meeting.net.models.PutMembersDataResponse;
import com.woooapp.meeting.net.models.RoomData;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.ScreenCapturerAndroid;
import org.webrtc.VideoCapturer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import eu.siacs.conversations.R;
import eu.siacs.conversations.entities.Account;
import eu.siacs.conversations.persistance.DatabaseBackend;
import okhttp3.Call;
import okhttp3.Response;
import pk.muneebahmad.lib.graphics.SP;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 6:36 pm 11/09/2023
 * <code>class</code> MeetingActivity.java
 */
public class MeetingActivity extends AppCompatActivity implements Handler.Callback {
    private static final String TAG = MeetingActivity.class.getSimpleName() + ".java";
    private static final int PERMISSIONS_REQ_CODE = 0x7b;
    private static final int PERMISSION_CAMERA_CODE = 0x7a;
    private static final int PERMISSION_MIC_CODE = 0x7c;
    private static final int PERMISSION_MIC_BUTTON_CODE = 0x7f;
    private static final int PERMISSION_EXTERNAL_STORAGE_CODE = 0x7d;
    private static final int PERMISSION_NOTIFICATION_CODE = 0x7e;
    private static final int MEETING_NOTIFICATION_ID = 0x9f;
    private static final int PERMISSION_CAPTURE_RESULT_CODE = 0x99;
    public static final String EXTRA_MEETING_SCHEDULED = "meeting_scheduled";
    private MeetingClient mMeetingClient;
    private CreateMeetingResponse createMeetingResponse;
    private PutMembersDataResponse putMembersDataResponse;
    private RoomData roomData;
    private RoomOptions mOptions;
    private RoomStore mRoomStore;
    private boolean joining = false;
    private boolean isMeetingScheduled = false;
    private String email;
    private String username;
    private String accountUniqueId;
    private String picture;
    private String mMeetingId;
    private String meetingName;
    private MeetingPagerAdapter peersPagerAdapter;
    private List<Peer> peersList = new ArrayList<>();
    //    private List<GridPeer> gridPeerList = new ArrayList<>();
    private List<ListGridPeer> listGridPeer = new LinkedList<>();
    private Handler callbackHandler = new Handler(this);
    private int deviceWidthDp;
    private int deviceHeightDp;
    private boolean mSideMenuOpened = true;
    private UIManager uiManager;
    private MeetingChatAdapter chatAdapter;
    private MeetingTranscriptAdapter transcriptAdapter;
    private List<Chat> chatList = new LinkedList<>();
    private List<Transcript> transcriptList = new LinkedList<>();
    private boolean chatSelected = true;
    private MeetingMorePopup morePopup;
    private LinearLayout bottomBarMeeting;
    private ImageView buttonEnd;
    private ImageView buttonCam;
    private ImageView buttonMic;
    private ImageView buttonAI;
    private ImageView buttonMoreMenu;
    private LinearLayout drawerButton;
    private ImageView buttonChatSend;
    private EditText etChatInput;
    private LinearLayout layoutEtChat;
    private ListView listViewMeetingChat;
    private ListView listViewMeetingTranscript;
    private LinearLayout tabChat;
    private LinearLayout tabTranscript;
    private LinearLayout tabHost;
    private ImageView buttonLangSelect;
    private RelativeLayout drawerContainer;
    private RelativeLayout drawerLayout;
    private LinearLayout meetingBackground;
    private TextView tvMeetingId;
    private ViewPager viewPager;
    private RelativeLayout mainContainer;
    private boolean langSelected = false;
    private String selectedLanguageName = "English";
    private String selectedLanguageCode = "en";
    private String langsJson;
    private Account account;
    private Languages langs;
    private WooProgressDialog progressDialog;
    private List<MeetingPageFragment2> pageFragments = new LinkedList<>();
    private WTonePlayer notificationSound;
    private LinearLayout pageControlLayout;
    private LinearLayout translationLayout;
    private TextView tvTransOriginal;
    private TextView tvTransTrans;
    private ImageView ivBg;
    private boolean isCamEnabled;
    private boolean isMicEnabled;
    private AudioManager droidAudioManager;
    private boolean memberAdded = false;
    private boolean morePopupVisible = false;
    private View mutingView;
    private NotificationManagerCompat notificationManager;
    private boolean isNewIntent = false;
    private MediaProjectionManager mediaProjectionManager;
    private Intent mMediaProjectionPermissionResultData;
    private int mMediaProjectionPermissionResultCode;
    private VideoCapturer capturer;
    private LinearLayout layoutScreenShare;
    private ImageView buttonStopScreenShare;
    private RelativeLayout layoutPeerScreenShare;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (!isNewIntent) {
            this.uiManager = UIManager.getUIManager(this);
            WEvents.getInstance().addHandler(callbackHandler);
            deviceWidthDp = Display.getDisplayWidth(this) - 50;
            deviceHeightDp = Display.getDisplayHeight(this);
            this.initComponents();
            this.bottomBarMeeting.setVisibility(View.GONE);

            this.droidAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            this.droidAudioManager.setSpeakerphoneOn(true);

            this.notificationManager = NotificationManagerCompat.from(this);

            progressDialog = WooProgressDialog.make(this, "Setting up ...");
            progressDialog.show();
            createDrawer();

            this.langsJson = WDirector.getInstance().readFileFromAssets(getAssets(), "languages.json");
            try {
                this.langs = Languages.fromJson(langsJson);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                askForPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_MIC_CODE);
            } else {
                preSetup();
            }

            this.buttonEnd.setOnClickListener(view -> {
                destroyMeeting();
            });

            this.drawerButton.setOnClickListener(view -> {
                if (mSideMenuOpened) {
                    closeDrawer();
                } else {
                    openDrawer();
                }
            });

            this.buttonMoreMenu.setOnClickListener(view -> {
                // More Button
                if (!morePopupVisible) {
                    morePopupVisible = true;
                    try {
                        this.morePopup = new MeetingMorePopup(
                                this, this.mainContainer,
                                this.bottomBarMeeting.getHeight(),
                                mMeetingClient, () -> morePopupVisible = false);
                        morePopup.show();
                        fetchRoomData2();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        morePopupVisible = false;
                    }
                }
            });

            this.buttonAI.setOnClickListener(view -> {
                View contentView = LayoutInflater.from(this).inflate(R.layout.layout_bottom_sheet_translation, null);
                View buttonTextTrans = contentView.findViewById(R.id.buttonTransSheetText);
                ImageView ivText = contentView.findViewById(R.id.ivText);
                ImageView ivVoice = contentView.findViewById(R.id.ivVoice);
                if (mMeetingClient != null) {
                    ivText.setVisibility(mMeetingClient.isTextTranslationOn() ? View.VISIBLE : View.GONE);
                    ivVoice.setVisibility(mMeetingClient.isVoiceTranslationOn() ? View.VISIBLE : View.GONE);
                }
                View buttonVoiceTrans = contentView.findViewById(R.id.buttonTransSheetVoice);
                Button buttonCancel = contentView.findViewById(R.id.buttonTransSheetCancel);

                BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.SheetDialog);
                dialog.setContentView(contentView);
                dialog.show();

                buttonTextTrans.setOnClickListener(v -> {
                    if (mMeetingClient != null) {
                        mMeetingClient.setTextTranslation(!mMeetingClient.isTextTranslationOn());
                        if (mMeetingClient.isTextTranslationOn()) {
                            WEvents.getInstance().notify(WEvents.EVENT_TRANSLATION_ENABLED, null);
                        } else {
                            WEvents.getInstance().notify(WEvents.EVENT_TRANSLATION_DISABLED, null);
                        }
                        if (mMeetingClient.isTextTranslationOn() || mMeetingClient.isVoiceTranslationOn()) {
                            buttonAI.setImageResource(R.drawable.ic_lang_enabled);
                        } else {
                            buttonAI.setImageResource(R.drawable.ic_lang_disable);
                        }
                    }
                    dialog.dismiss();
                });

                buttonVoiceTrans.setOnClickListener(v -> {
                    if (mMeetingClient != null) {
                        mMeetingClient.setVoiceTranslation(!mMeetingClient.isVoiceTranslationOn());
//                    setVoiceTranslation(mMeetingClient.isVoiceTranslationOn());
                        try {
                            showCommonPopup(
                                    "Voice Translation " + (mMeetingClient.isVoiceTranslationOn() ? " is enabled" : " is disabled"),
                                    true,
                                    WooCommonPopup.VERTICAL_POSITION_TOP);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        if (mMeetingClient.isTextTranslationOn() || mMeetingClient.isVoiceTranslationOn()) {
                            buttonAI.setImageResource(R.drawable.ic_lang_enabled);
                        } else {
                            buttonAI.setImageResource(R.drawable.ic_lang_disable);
                        }

//                        mutingView.setVisibility(mMeetingClient.isVoiceTranslationOn() ? View.VISIBLE : View.GONE);
//                        new Handler().postDelayed(() -> {
//                            mutingView.setVisibility(View.GONE);
//                        }, 5000);
                    }
                    dialog.dismiss();
                });

                buttonCancel.setOnClickListener(v -> {
                    dialog.dismiss();
                });
            });

            this.buttonMic.setOnClickListener(view -> {
                // Mic
                if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    askForPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_MIC_BUTTON_CODE);
                } else {
                    if (mMeetingClient.isMicOn()) {
                        mMeetingClient.setMicOn(false);
                    } else {
                        mMeetingClient.setMicOn(true);
                    }
                }
            });

            this.buttonCam.setOnClickListener(view -> {
                // Cam
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    askForPermission(Manifest.permission.CAMERA, PERMISSION_CAMERA_CODE);
                } else {
                    if (mMeetingClient.isCamOn()) {
                        mMeetingClient.setCamOn(false);
                    } else {
                        mMeetingClient.setCamOn(true);
                    }
                }
            });

            this.buttonChatSend.setOnClickListener(view -> {
                if (!this.etChatInput.getText().toString().isEmpty()) {
                    com.woooapp.meeting.net.models.Message message = new com.woooapp.meeting.net.models.Message();
                    message.setSocketId(mMeetingClient.getSocketId());
                    message.setProfileImage(this.picture);
                    message.setName(this.username);
                    message.setMeetingId(mMeetingId);
                    message.setMessage(this.etChatInput.getText().toString());
                    mMeetingClient.sendMessage(message);

                    this.etChatInput.setText("");
                } else {
                    etChatInput.setHint("Empty Message");
                    etChatInput.setHintTextColor(Color.RED);
                }
            });
        } else {
            Log.d(TAG, "New Intent onCreate()");
        }
    }

    private void initComponents() {
        this.mainContainer = findViewById(R.id.meetingMainContainer);
        this.bottomBarMeeting = findViewById(R.id.bottomBarMeeting);
        this.buttonMoreMenu = findViewById(R.id.meetingButtonMoreMenu);
        this.buttonAI = findViewById(R.id.meetingButtonAI);
        this.buttonMic = findViewById(R.id.meetingButtonMic);
        this.buttonCam = findViewById(R.id.meetingButtonCam);
        this.buttonEnd = findViewById(R.id.meetingButtonEnd);
        this.drawerButton = findViewById(R.id.drawerButton);
        this.drawerButton.setAlpha(0.4f);

        this.buttonChatSend = findViewById(R.id.buttonMeetingChatSend);
        this.etChatInput = findViewById(R.id.etMeetingChat);
        this.layoutEtChat = findViewById(R.id.layoutEtChat);
        this.listViewMeetingChat = findViewById(R.id.listViewMeetingChat);
        this.listViewMeetingTranscript = findViewById(R.id.listViewTranscript);
        this.tabChat = findViewById(R.id.tabChat);
        this.tabTranscript = findViewById(R.id.tabTranscript);
        this.tabHost = findViewById(R.id.tabHost);
        this.drawerLayout = findViewById(R.id.drawerLayout);
        this.drawerContainer = findViewById(R.id.drawerContainer);
        this.buttonLangSelect = findViewById(R.id.drawerButtonLang);
        this.meetingBackground = findViewById(R.id.meetingBackground);

        this.tvMeetingId = findViewById(R.id.tvMeetingId);
        this.viewPager = findViewById(R.id.meetingViewPager);

        this.pageControlLayout = findViewById(R.id.page_control);
        this.translationLayout = findViewById(R.id.translationLayout);
        this.tvTransOriginal = findViewById(R.id.tvTranslationOriginal);
        this.tvTransTrans = findViewById(R.id.tvTranslationTranslation);
        this.ivBg = findViewById(R.id.ivBg);

        this.mutingView = findViewById(R.id.layoutMuting);

        this.layoutScreenShare = findViewById(R.id.layoutPresenting);
        this.buttonStopScreenShare = findViewById(R.id.buttonStop);
        this.layoutPeerScreenShare = findViewById(R.id.layoutPeerScreenShare);

        this.meetingBackground.setOnTouchListener((view, motionEvent) -> true);

        this.notificationSound = new WTonePlayer(getResources().openRawResourceFd(R.raw.meet_sound));
        this.notificationSound.setVolume(0.5f);
    }

    private void showScreenShareView() {
        if (this.layoutScreenShare != null) {
            layoutScreenShare.setVisibility(View.VISIBLE);
            WooAnimationUtil.showView(layoutScreenShare, null);
            buttonStopScreenShare.setOnClickListener(view -> {
                if (mMeetingClient.isPresenting()) {
                    mMeetingClient.shareScreen(false, null, null);
                    disposeVideoCapturer();
                }
            });
        }
    }

    private void initAccount() {
        DatabaseBackend db = DatabaseBackend.getInstance(this);
        if (db.getAccounts() != null) {
            if (db.getAccounts().size() > 0) {
                this.account = db.getAccounts().get(0);
                this.selectedLanguageName = this.account.getLanguage();
                this.selectedLanguageCode = this.account.getLanguageCode();
                mMeetingClient.setSelectedLanguageCode(selectedLanguageCode);
                mMeetingClient.setSelectedLanguage(selectedLanguageName);
            }
        }
    }

    private void updateLanguage() {
        if (account != null) {
            mMeetingClient.setSelectedLanguage(selectedLanguageName);
            mMeetingClient.setSelectedLanguageCode(selectedLanguageCode);
            account.setLanguage(selectedLanguageName);
            account.setLanguageCode(selectedLanguageCode);
            DatabaseBackend db = DatabaseBackend.getInstance(this);
            db.updateAccount(account);

            mMeetingClient.updateTranslationLanguage(selectedLanguageCode);
        }
    }

    private void preSetup() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                askForPermission(Manifest.permission.POST_NOTIFICATIONS, PERMISSION_NOTIFICATION_CODE);
            } else {
                setup();
            }
        } else {
            setup();
        }
    }

    private void setup() {
//        pd.show();
        this.isCamEnabled = getIntent().getBooleanExtra("camOn", true);
        this.isMicEnabled = getIntent().getBooleanExtra("micOn", true);
        this.email = getIntent().getStringExtra("email");
        this.accountUniqueId = getIntent().getStringExtra("accountUniqueId");
        this.username = getIntent().getStringExtra("username");
        this.picture = getIntent().getStringExtra("picture");
        if (picture == null) {
            picture = "";
        }
        this.joining = getIntent().getBooleanExtra("joining", false);
        this.isMeetingScheduled = getIntent().getBooleanExtra(EXTRA_MEETING_SCHEDULED, false);
        this.mMeetingId = getIntent().getStringExtra("meetingId");
        this.meetingName = getIntent().getStringExtra("meetingName");
        if (meetingName != null) {
            if (meetingName.isEmpty()) {
                meetingName = Utils.getRandomString(4);
            }
        } else {
            meetingName = Utils.getRandomString(4);
        }
        meetingName = "WooooDroid-" + meetingName;
        Log.d(TAG, "<< MEETING ID [" + mMeetingId + "]");
        if (mMeetingId != null) {
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
            }
        } else {

        }
    }

    private void joinMeeting() {
        ApiManager.build(this).checkForPassword(mMeetingId, new ApiManager.ApiResult2() {
            @Override
            public void onResult(Call call, Response response) {
                if (response != null) {
                    try {
                        String resp = response.body().string();
                        Log.d(TAG, "<<< Check Password Response >>>> " + resp);
                        JSONObject obj = new JSONObject(String.valueOf(resp));
                        boolean value = obj.getBoolean("value");
                        if (value) {
                            runOnUiThread(() -> {
                                try {
                                    validatePassword();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            });
                        } else {
                            runOnUiThread(() -> {
                                initMeetingClient();
                            });
                        }
                    } catch (NullPointerException | IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call call, Object e) {
                Log.d(TAG, "<<< Check Password call failed >>> " + e);
            }
        });
    }

    private void validatePassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.TransparentBgDialogStyle);
        View v = LayoutInflater.from(this).inflate(R.layout.layout_dialog_meeting_password, null);
        TextView tvMessage = v.findViewById(R.id.tvPassword);
        final AppCompatEditText etPassword = v.findViewById(R.id.etPassword);
        final Button buttonDone = v.findViewById(R.id.buttonOk);
        final Button buttonCancel = v.findViewById(R.id.buttonCancel);
        final ProgressBar progressBar = v.findViewById(R.id.progressBar);
        tvMessage.setText("Meeting with id " + mMeetingId + " is protected by password. Kindly provide valid password to continue.");

        builder.setView(v);

        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        buttonDone.setOnClickListener(view -> {
            if (etPassword.getText().toString().isEmpty()) {
                tvMessage.setText("Password is required");
                tvMessage.setTextColor(Color.RED);
            } else {
                tvMessage.setText("Meeting with id " + mMeetingId + " is protected by password. Kindly provide valid password to continue.");
                tvMessage.setTextColor(Color.WHITE);

                buttonDone.setVisibility(View.GONE);
                buttonCancel.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                etPassword.setEnabled(false);

                ApiManager.build(MeetingActivity.this).confirmPassword(mMeetingId, etPassword.getText().toString(), new ApiManager.ApiResult2() {
                    @Override
                    public void onResult(Call call, Response response) {
                        if (response != null) {
                            try {
                                String resp = response.body().string();
                                Log.d(TAG, "<< Confirm Password Response >>> " + resp);
                                JSONObject obj = new JSONObject(resp);
                                boolean value = obj.getBoolean("value");
                                if (value) {
                                    runOnUiThread(() -> {
                                        initMeetingClient();
                                        dialog.dismiss();
                                    });
                                } else {
                                    runOnUiThread(() -> {
                                        buttonDone.setVisibility(View.VISIBLE);
                                        buttonCancel.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                        etPassword.setEnabled(true);

                                        tvMessage.setText("Provided password is incorrect. Kindly provide valid password");
                                        tvMessage.setTextColor(Color.RED);
                                    });
                                }
                            } catch (NullPointerException | JSONException | IOException e) {
                                e.printStackTrace();
                                runOnUiThread(() -> {
                                    dialog.dismiss();
                                    UIManager.showErrorDialog(MeetingActivity.this,
                                            "Error",
                                            "Failed to validate password from server. Please try again later.",
                                            "Ok",
                                            R.drawable.ic_warning,
                                            null);
                                });
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call call, Object e) {
                        runOnUiThread(() -> {
                            dialog.dismiss();
                            UIManager.showErrorDialog(MeetingActivity.this,
                                    "Error",
                                    "Failed to validate password from server. Please try again later.",
                                    "Ok",
                                    R.drawable.ic_warning,
                                    null);
                        });
                    }
                });
            }
        });

        buttonCancel.setOnClickListener(view -> {
            dialog.cancel();
            finish();
        });
    }

    private void setPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.TransparentBgDialogStyle);
        View v = LayoutInflater.from(this).inflate(R.layout.layout_dialog_meeting_add_password, null);
        final AppCompatEditText etPassword = v.findViewById(R.id.etPassword);
        final Button buttonOk = v.findViewById(R.id.buttonOk);
        final Button buttonCancel = v.findViewById(R.id.buttonCancel);
        final ProgressBar progressBar = v.findViewById(R.id.progressBar);

        builder.setView(v);

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        buttonOk.setOnClickListener(view -> {
            if (etPassword.getText().toString().isEmpty()) {
                etPassword.setHintTextColor(Color.RED);
                etPassword.setHint("* Password is required");
            } else {
                etPassword.setEnabled(false);
                buttonCancel.setVisibility(View.GONE);
                buttonOk.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                ApiManager.build(MeetingActivity.this).applyPassword(mMeetingId, etPassword.getText().toString(), new ApiManager.ApiResult2() {
                    @Override
                    public void onResult(Call call, Response response) {
                        if (response != null) {
                            try {
                                String resp = response.body().string();
                                Log.d(TAG, "<<< Add password response >>> " + resp);
                                runOnUiThread(() -> {
                                    progressBar.setVisibility(View.GONE);
                                    dialog.dismiss();
                                    try {
                                        showCommonPopup("Password added successfully", true, WooCommonPopup.VERTICAL_POSITION_TOP);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    mMeetingClient.setPasswordSet(true);
                                });
                            } catch (NullPointerException | IOException e) {
                                e.printStackTrace();
                                runOnUiThread(() -> {
                                    dialog.dismiss();
                                    UIManager.showErrorDialog(MeetingActivity.this,
                                            "Error",
                                            "Failed to set password. Please try again later.",
                                            "Ok",
                                            R.drawable.ic_warning,
                                            null);
                                });
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call call, Object e) {
                        runOnUiThread(() -> {
                            dialog.dismiss();
                            UIManager.showErrorDialog(MeetingActivity.this,
                                    "Error",
                                    "Failed to set password. Please try again later.",
                                    "Ok",
                                    R.drawable.ic_warning,
                                    null);
                        });
                    }
                });
            }
        });

        buttonCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });
    }

    /**
     * Custom Drawer Layout
     */
    private void createDrawer() {
        if (mSideMenuOpened) {
            WooAnimationUtil.setLeftMenuClosed(this.drawerContainer, this.drawerLayout.getLayoutParams().width, null);
            mSideMenuOpened = false;
        }
        this.chatAdapter = new MeetingChatAdapter(this, chatList);
        this.listViewMeetingChat.setAdapter(chatAdapter);

        this.transcriptAdapter = new MeetingTranscriptAdapter(this);
        this.listViewMeetingTranscript.setAdapter(transcriptAdapter);

        this.tabChat.setOnClickListener(view -> {
            switchChatTabs(true);
        });

        this.tabTranscript.setOnClickListener(view -> {
            switchChatTabs(false);
        });

        this.buttonLangSelect.setOnClickListener(v -> {
            if (!langSelected) {
                buttonLangSelect.setImageResource(R.drawable.ic_lang_enabled);
                langSelected = true;
                WDirector.getInstance().setChatTranslationEnabled(true);

                try {
                    this.showLanguageSelectionSheet();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                langSelected = false;
                WDirector.getInstance().setChatTranslationEnabled(false);
                buttonLangSelect.setImageResource(R.drawable.ic_lang_disable);
            }
        });
    }

    private void switchChatTabs(boolean chat) {
        if (chatSelected && !chat) {
            this.tabTranscript.setBackgroundResource(R.drawable.bg_meeting_menu_tab);
            this.tabChat.setBackgroundResource(R.drawable.bg_meeting_menu_tab_disabled);
            this.layoutEtChat.setVisibility(View.GONE);
            WooAnimationUtil.hideView(this.listViewMeetingChat, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    listViewMeetingChat.setVisibility(View.GONE);
                }
            });
            this.listViewMeetingTranscript.setVisibility(View.VISIBLE);
            WooAnimationUtil.showView(this.listViewMeetingTranscript, null);
            chatSelected = false;
        } else if (!chatSelected && chat) {
            this.tabTranscript.setBackgroundResource(R.drawable.bg_meeting_menu_tab_disabled);
            this.tabChat.setBackgroundResource(R.drawable.bg_meeting_menu_tab);
            this.layoutEtChat.setVisibility(View.VISIBLE);
            WooAnimationUtil.hideView(this.listViewMeetingTranscript, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    listViewMeetingTranscript.setVisibility(View.GONE);
                }
            });

            this.listViewMeetingChat.setVisibility(View.VISIBLE);
            WooAnimationUtil.showView(this.listViewMeetingChat, null);

            chatSelected = true;
        }
    }

    private void closeDrawer() {
        if (mSideMenuOpened) {
            WooAnimationUtil.closeLeftMenu(this.drawerContainer, this.drawerLayout.getWidth(), new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    meetingBackground.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    drawerButton.setAlpha(0.4f);
                }
            });
            mSideMenuOpened = false;
            hideKeyboard();
        }
    }

    private void openDrawer() {
        if (!mSideMenuOpened) {
            WooAnimationUtil.openLeftMenu(drawerContainer, drawerLayout.getWidth(), new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    Bitmap blurredBg = uiManager.blur(MeetingActivity.this, uiManager.createBitmap(mainContainer));
                    meetingBackground.setBackground(new BitmapDrawable(blurredBg));
                    meetingBackground.setVisibility(View.VISIBLE);
                    drawerButton.setAlpha(1.0f);
                }
            });
            mSideMenuOpened = true;
        }
    }

    private void showLanguageSelectionSheet() {
        try {
//            closeDrawer();
            View view = LayoutInflater.from(this).inflate(R.layout.layout_dialog_language_select, null);
            Button buttCancel = view.findViewById(R.id.buttonLangSelectCancel);
            TextView tvNone = view.findViewById(R.id.tvLangNone);
            ListView listView = view.findViewById(R.id.listViewLangSelect);
            listView.setAdapter(new LangSelectionAdapter(this, langs.getLanguages(), selectedLanguageName));

            BottomSheetDialog sheet = new BottomSheetDialog(this, R.style.SheetDialog);
            sheet.setContentView(view);
            sheet.show();

            listView.setOnItemClickListener((parent, view1, position, id) -> {
                selectedLanguageName = langs.getLanguages().get(position).getName();
                selectedLanguageCode = langs.getLanguages().get(position).getCode();
                updateLanguage();
                sheet.dismiss();
            });

            tvNone.setOnClickListener(view3 -> {
                langSelected = false;
                WDirector.getInstance().setChatTranslationEnabled(false);
                buttonLangSelect.setImageResource(R.drawable.ic_lang_disable);
                sheet.dismiss();
            });

            buttCancel.setOnClickListener(view2 -> {
                sheet.dismiss();
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void fetchRoomData() {
        ApiManager.build(this).fetchRoomData2(mMeetingId, new ApiManager.ApiResult2() {
            @Override
            public void onResult(Call call, Response response) {
                if (response != null) {
                    if (response.body() != null) {
                        try {
                            String resp = response.body().string();
                            Log.d(TAG, "ROOM DATA RESPONSE >>> " + resp);
                            roomData = RoomData.fromJson(resp);
                            WDirector.getInstance().setRoomData(roomData);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        runOnUiThread(() -> {
                            initMeetingClient();
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call call, Object error) {
                Log.e(TAG, "Error while fetching room data " + error.toString());
            }
        });
    }

    private void fetchRoomData2() {
        ApiManager.build(this).fetchRoomData2(mMeetingId, new ApiManager.ApiResult2() {
            @Override
            public void onResult(Call call, Response response) {
                try {
                    String json = response.body().string();
                    roomData = RoomData.fromJson(json);
                    WDirector.getInstance().setRoomData(roomData);
                    if (mMeetingClient != null && accountUniqueId != null) {
                        WDirector.getInstance().updateRole(mMeetingClient, accountUniqueId);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, Object e) {

            }
        });
    }

    /**
     * @param body
     */
    private void createMeeting(CreateMeetingBody body) {
        try {
            ApiManager.build(this).fetchCreatePreMeeting(body.toJson(), new ApiManager.ApiResult2() {
                @Override
                public void onResult(Call call, Response response) {
                    if (response != null) {
                        if (response.body() != null) {
                            try {
                                String resp = response.body().string();
                                Log.d(TAG, "Pre Meeting Response >>> " + resp);
                                createMeetingResponse = CreateMeetingResponse.fromJson(resp);
                                fetchRoomData();
                            } catch (IOException e) {
                                e.printStackTrace();
                                showNetworkErrorDialog();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call call, Object error) {
                    Log.e(TAG, "Error on createMeeting()" + error.toString());
                    showNetworkErrorDialog();
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
        body.setScheduled(this.isMeetingScheduled);
        body.setLanguage(this.selectedLanguageCode);

        Log.d(TAG, "<< PUT Member >>> " + body.toString());

        ApiManager.build(this).
                putMembersData(body.toJson(),
                        mMeetingId,
                        new ApiManager.ApiResult2() {
                            @Override
                            public void onResult(Call call, Response response) {
                                if (response != null) {
                                    try {
                                        String resp = response.body().string();
                                        Log.d(TAG, "RESPONSE PUT Member >>> " + resp);
                                        putMembersDataResponse = PutMembersDataResponse.fromJson(resp);
                                        memberAdded = true;
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        showNetworkErrorDialog();
                                    } finally {
                                        fetchRoomData2();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call call, Object e) {
                                Log.e(TAG, "Error while calling [" + call.request().url() + "] -> " + e.toString());
                                showNetworkErrorDialog();
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
//        this.mMeetingClient.setCamOn(this.isCamEnabled);
//        this.mMeetingClient.setMicOn(this.isMicEnabled);
        if (!joining) this.mMeetingClient.setRole(MeetingClient.Role.ADMIN);

        initAccount();

        getViewModelStore().clear();
        initViewModel();
        WDirector.getInstance().generateMeetingId();
    }

    private void initViewModel() {
        RoomProps roomProps = new RoomProps(getApplication(), mRoomStore);
        roomProps.connect(this);

        // Set Meeting ID Link
        this.tvMeetingId.setText(mMeetingId);

        // PeerView
        this.peersPagerAdapter = new MeetingPagerAdapter(getSupportFragmentManager(), this, mRoomStore, mMeetingClient, this);
        this.peersPagerAdapter.setBottomBarHeight(bottomBarMeeting.getLayoutParams().height);
        this.viewPager.setAdapter(peersPagerAdapter);
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                updatePageControl(position);
            }

            @Override
            public void onPageSelected(int position) {
                updatePageControl(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        final MeetingPageFragment f1 = new MeetingPageFragment(1, this, mRoomStore, mMeetingClient, this);
//        pageFragments.add(f1);
//        final MeetingPageFragment f2 = new MeetingPageFragment(2, this, mRoomStore, mMeetingClient, this);
//        final MeetingPageFragment f3 = new MeetingPageFragment(3, this, mRoomStore, mMeetingClient, this);
//        final MeetingPageFragment f4 = new MeetingPageFragment(4, this, mRoomStore, mMeetingClient, this);
//        final MeetingPageFragment f5 = new MeetingPageFragment(5, this, mRoomStore, mMeetingClient, this);
//        final MeetingPageFragment f6 = new MeetingPageFragment(6, this, mRoomStore, mMeetingClient, this);
//        final MeetingPageFragment f7 = new MeetingPageFragment(7, this, mRoomStore, mMeetingClient, this);
//        final MeetingPageFragment f8 = new MeetingPageFragment(8, this, mRoomStore, mMeetingClient, this);  // 48 including ME

        final MeetingPageFragment2 f1 = new MeetingPageFragment2(this, 1, this, mRoomStore, mMeetingClient);
        pageFragments.add(f1);
        peersPagerAdapter.notifyDataSetChanged();
        final MeetingPageFragment2 f2 = new MeetingPageFragment2(this, 2, this, mRoomStore, mMeetingClient);
        final MeetingPageFragment2 f3 = new MeetingPageFragment2(this, 3, this, mRoomStore, mMeetingClient);
        final MeetingPageFragment2 f4 = new MeetingPageFragment2(this, 4, this, mRoomStore, mMeetingClient);
        final MeetingPageFragment2 f5 = new MeetingPageFragment2(this, 5, this, mRoomStore, mMeetingClient);
        final MeetingPageFragment2 f6 = new MeetingPageFragment2(this, 6, this, mRoomStore, mMeetingClient);
        final MeetingPageFragment2 f7 = new MeetingPageFragment2(this, 7, this, mRoomStore, mMeetingClient);
        final MeetingPageFragment2 f8 = new MeetingPageFragment2(this, 8, this, mRoomStore, mMeetingClient);
        final MeetingPageFragment2 f9 = new MeetingPageFragment2(this, 9, this, mRoomStore, mMeetingClient);
        final MeetingPageFragment2 f10 = new MeetingPageFragment2(this, 10, this, mRoomStore, mMeetingClient);

        mRoomStore
                .getPeers()
                .observe(
                        this,
                        peers -> {
                            peersList = peers.getAllPeers();
                            List<List<Peer>> paginatedList = WDirector.getInstance().getPeerPaginatedLists(peersList);
                            Log.d(TAG, "<<< Paginated List size >>> " + paginatedList.size());
                            if (paginatedList.size() > 0) {
                                for (int i = 0; i < paginatedList.size(); i++) {
                                    if (i == 0) {
                                        List<Peer> pList = paginatedList.get(0);
                                        List<GridPeer> gList = createGridPeers2(pList, true);
                                        for (GridPeer g : gList) {
                                            f1.addPeerItem(g);
                                        }
                                    } else {
                                        if (pageFragments.size() == 1) {
                                            if (i == 1) {
                                                pageFragments.add(f2);
                                                List<Peer> pList2 = paginatedList.get(1);
                                                List<GridPeer> gList2 = createGridPeers2(pList2, false);
                                                for (GridPeer g : gList2) {
                                                    f2.addPeerItem(g);
                                                }
                                            } else {
                                                pageFragments.add(f3);
                                                peersPagerAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    }
                                }
//                                switch (paginatedList.size()) {
//                                    case 1:
//                                    List<Peer> pList = paginatedList.get(0);
//                                    List<GridPeer> gList = createGridPeers2(pList, true);
//                                    for (GridPeer g : gList) {
//                                        Log.d(TAG, "Added GridPeer of type " + (g.getViewType() == GridPeer.VIEW_TYPE_ME ? " Me" : " Peer"));
//                                        f1.addPeerItem(g);
//                                    }
//                                    break;
//                                    case 2:
//                                        if (pageFragments.size() == 1) {
//                                            pageFragments.add(f2);
//                                            peersPagerAdapter.notifyDataSetChanged();
//                                        }
//                                        List<Peer> pList1 = paginatedList.get(0);
//                                        List<GridPeer> gList1 = createGridPeers2(pList1, true);
//                                        for (GridPeer g : gList1) {
//                                            Log.d(TAG, "Added GridPeer of type " + (g.getViewType() == GridPeer.VIEW_TYPE_ME ? " Me" : " Peer"));
//                                            f1.addPeerItem(g);
//                                        }
//                                        List<Peer> pList2 = paginatedList.get(1);
//                                        List<GridPeer> gList2 = createGridPeers2(pList2, false);
//                                        for (GridPeer g : gList2) {
//                                            Log.d(TAG, "Added GridPeer of type " + (g.getViewType() == GridPeer.VIEW_TYPE_ME ? " Me" : " Peer"));
//                                            f2.addPeerItem(g);
//                                        }
//                                        break;
//                                    default:
//                                        break;
//                                }
                            }


//                            listGridPeer = new LinkedList<>();
//                            List<MeetingPage> pageList = new LinkedList<>();
//                            if (peersList.size() <= 5) {
//                                List<ListGridPeer> pl1 = createListGridPeers(peersList, true);
//                                MeetingPage page1 = new MeetingPage(1, pl1);
//                                pageList.add(page1);
//                                pageFragments.get(0).replacePage(page1);
//                                if (pageFragments.size() > 1) {
//                                    for (int i = 0; i < pageFragments.size(); i++) {
//                                        if (i > 0) {
//                                            pageFragments.remove(i);
//                                        }
//                                    }
//                                }
//                            } else if (peersList.size() > 5 && peersList.size() <= 12) {
//                                List<ListGridPeer> pl2 = createListGridPeers(peersList.subList(0, 5), true);
//                                List<ListGridPeer> pl3 = new ArrayList<>();
//                                if (peersList.size() == 6) {
//                                    pl3.add(new ListGridPeer(ListGridPeer.VIEW_TYPE_PEER_PEER, peersList.get(5), null));
//                                } else {
//                                    pl3 = createListGridPeers(peersList.subList(5, peersList.size()), false);
//                                }
//                                MeetingPage page1 = new MeetingPage(1, pl2);
//                                MeetingPage page2 = new MeetingPage(2, pl3);
//                                pageList.add(page1);
//                                pageList.add(page2);
//                                pageFragments.get(0).replacePage(page1);
//                                if (pageFragments.size() < 2) {
//                                    pageFragments.add(f2);
//                                    f2.replacePage(page2);
//                                } else {
//                                    pageFragments.get(1).replacePage(page2);
//                                }
//                                if (pageFragments.size() > 2) {
//                                    for (int i = 0; i < pageFragments.size(); i++) {
//                                        if (i > 1) {
//                                            pageFragments.remove(i);
//                                        }
//                                    }
//                                }
//                            } else if (peersList.size() > 11 && peersList.size() <= 18) {
//                                List<ListGridPeer> pl4 = createListGridPeers(peersList.subList(0, 5), true);
//                                List<ListGridPeer> pl5 = createListGridPeers(peersList.subList(6, 11), false);
//                                List<ListGridPeer> pl6 = new ArrayList<>();
//                                if (peersList.size() == 12) {
//                                    pl6.add(new ListGridPeer(ListGridPeer.VIEW_TYPE_PEER_PEER, peersList.get(11), null));
//                                } else {
//                                    pl6 = createListGridPeers(peersList.subList(11, peersList.size() - 1), false);
//                                }
//                                MeetingPage page1 = new MeetingPage(1, pl4);
//                                MeetingPage page2 = new MeetingPage(2, pl5);
//                                MeetingPage page3 = new MeetingPage(3, pl6);
//                                pageList.add(page1);
//                                pageList.add(page2);
//                                pageList.add(page3);
//                                pageFragments.get(0).replacePage(page1);
//                                pageFragments.get(1).replacePage(page2);
//                                if (pageFragments.size() < 3) {
//                                    pageFragments.add(f3);
//                                    f3.replacePage(page3);
//                                } else {
//                                    pageFragments.get(2).replacePage(page3);
//                                }
//                            }
//                            Log.d(TAG, "Peer list size[" + peersList.size() + "]");
//                            Log.d(TAG, "Pages Size >>> " + pageList.size());
                            peersPagerAdapter.replaceFragments(pageFragments);

                            // Page Control
                            if (pageFragments.size() > 1) {
                                createPageControl(0);
                                updatePageControl(0);
                            } else {
                                hidePageControl();
                            }
                        });
    }

    /**
     * @param peerList
     * @return
     */
    private List<GridPeer> createGridPeers(List<Peer> peerList) {
//        gridPeerList.clear();
        List<GridPeer> peers = new LinkedList<>();
        peers.add(new GridPeer(GridPeer.VIEW_TYPE_ME, null));
        for (Peer p : peerList) {
            peers.add(new GridPeer(GridPeer.VIEW_TYPE_PEER, p));
        }
        return peers;
    }

    /**
     * @param peersList
     * @param includeMe
     * @return
     */
    private List<GridPeer> createGridPeers2(@NonNull List<Peer> peersList, boolean includeMe) {
        List<GridPeer> peers = new LinkedList<>();
        if (includeMe) {
            peers.add(new GridPeer(GridPeer.VIEW_TYPE_ME, null));
        }
        for (Peer p : peersList) {
            peers.add(new GridPeer(GridPeer.VIEW_TYPE_PEER, p));
        }
        return peers;
    }

    /**
     * @param peers
     * @param adapter2
     * @return
     */
    private View createPageView(@NonNull List<ListGridPeer> peers, @NonNull PeerAdapter2 adapter2) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.custom_peer_view_page, null);
        ListView lv = contentView.findViewById(R.id.peersListView);
        lv.setEnabled(false);
        lv.setAdapter(adapter2);
        return contentView;
    }

    /**
     * @param peersList
     * @param addMe
     * @return
     */
    private List<ListGridPeer> createListGridPeers(List<Peer> peersList, boolean addMe) {
        listGridPeer.clear();
        List<ListGridPeer> peers = new LinkedList<>();
        if (addMe) {
            ListGridPeer mePeer = new ListGridPeer(ListGridPeer.VIEW_TYPE_ME_PEER, null, null);
            mePeer.setPeer1CamOn(true);
            mePeer.setPeer1MicOn(true);
            peers.add(mePeer);
            for (int i = 0; i < peersList.size(); i++) {
                Peer p = peersList.get(i);
                if (i == 0 || i == 3) {
                    peers.add(new ListGridPeer(ListGridPeer.VIEW_TYPE_PEER_PEER, p, null));
                } else if (i == 1) {
                    peers.get(0).setPeer2(p);
                } else if (i == 2) {
                    peers.get(1).setPeer2(p);
                } else if (i == 4) {
                    peers.get(2).setPeer2(p);
                }
            }
        } else {
            for (int i = 0; i < peersList.size(); i++) {
                Peer p = peersList.get(i);
                if (i == 0 || i == 1 || i == 4) {
                    peers.add(new ListGridPeer(ListGridPeer.VIEW_TYPE_PEER_PEER, p, null));
                } else if (i == 2) {
                    peers.get(0).setPeer2(p);
                } else if (i == 3) {
                    peers.get(1).setPeer2(p);
                } else if (i == 5) {
                    peers.get(2).setPeer2(p);
                }
            }
        }
        return peers;
    }

    private void askForPermission(String permission, int reqCode) {
        if (shouldShowRequestPermissionRationale(permission)) {
            UIManager.showInfoDialog(this, "Permission Required", "Meeting requires " + permission + " to function properly. Kinldy grant it to continue",
                    new UIManager.DialogCallback() {
                        @Override
                        public void onPositiveButton(@Nullable Object sender, @Nullable Object data) {
                            requestPermissions(new String[]{permission}, reqCode);
                        }

                        @Override
                        public void onNeutralButton(@Nullable Object sender, @Nullable Object data) {

                        }

                        @Override
                        public void onNegativeButton(@Nullable Object sender, @Nullable Object data) {

                        }
                    });
        } else {
            requestPermissions(new String[]{permission}, reqCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_MIC_CODE) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    preSetup();
                } else {
                    UIManager.showInfoDialog(MeetingActivity.this,
                            "Permission Required",
                            "Meeting at least requires Record Audio Microphone permission to function properly." +
                                    " Kindly grant it from settings to continue",
                            new UIManager.DialogCallback() {
                                @Override
                                public void onPositiveButton(@Nullable Object sender, @Nullable Object data) {
                                    finish();
                                }

                                @Override
                                public void onNeutralButton(@Nullable Object sender, @Nullable Object data) {

                                }

                                @Override
                                public void onNegativeButton(@Nullable Object sender, @Nullable Object data) {

                                }
                            });
                }
            }
        } else if (requestCode == PERMISSION_CAMERA_CODE) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mMeetingClient != null) {
                        if (!mMeetingClient.isCamOn()) {
                            mMeetingClient.setCamOn(true);
                        }
                    }
                }
            }
        } else if (requestCode == PERMISSION_EXTERNAL_STORAGE_CODE) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImageChooser();
                }
            }
        } else if (requestCode == PERMISSION_NOTIFICATION_CODE) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    preSetup();
                }
            }
        } else if (requestCode == PERMISSION_MIC_BUTTON_CODE) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mMeetingClient != null) {
                        mMeetingClient.setMicOn(true);
                    }
                }
            }
        }
    }

    private void startScreenCapture() {
        if (mediaProjectionManager == null) {
            mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        }
        startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), PERMISSION_CAPTURE_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "Request Code >>> [" + requestCode + "] ** Result Code [" + resultCode + "]");
        if (requestCode != PERMISSION_CAPTURE_RESULT_CODE) {
            return;
        }
        mMediaProjectionPermissionResultCode = resultCode;
        mMediaProjectionPermissionResultData = data;

        DisplayMetrics dm = new DisplayMetrics();
        if (resultCode == Activity.RESULT_OK && mMeetingClient != null) {
            getWindowManager().getDefaultDisplay().getRealMetrics(dm);
            if (this.capturer == null) {
//                if (capturer != null) {
                Log.d(TAG, "<< Starting Screen Share Service");
                Intent serviceIntent = new Intent(this, ScreenCaptureService.class);
                startService(serviceIntent);
                bindService(serviceIntent, new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        Log.d(TAG, "<<< Service Connected ... Enabling Screen Share ...");
                        capturer = createScreenCapturer();
                        if (capturer != null) {
                            mMeetingClient.shareScreen(true, capturer, dm);
                        }
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {
                        Log.d(TAG, "<< SERVICE DISCONNECTED >>>");
                    }
                }, BIND_AUTO_CREATE);
            } else {
                Log.w(TAG, "Capturer already exist ...");
                mMeetingClient.shareScreen(true, capturer, dm);
            }
        }
    }

    private VideoCapturer createScreenCapturer() {
        if (mMediaProjectionPermissionResultCode != Activity.RESULT_OK) {
            Log.d(TAG, "User didn't give permission to capture the screen.");
            return null;
        }
        return new ScreenCapturerAndroid(mMediaProjectionPermissionResultData, new MediaProjection.Callback() {
            @Override
            public void onStop() {
                super.onStop();
                Log.w(TAG, "Media Projection stopped.");
            }
        });
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        Log.d(TAG, "<< Handler Event >>> " + msg.what);
        switch (msg.what) {
            case WEvents.EVENT_TYPE_SOCKET_ID:
                Log.d(TAG, "<< Handler Event SOCKET_ID Received >> " + msg.obj);
                // Drawer
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                });
//                if (!joining) {
//                    try {
//                        putMember();
//                    } catch (JsonProcessingException e) {
//                        e.printStackTrace();
//                    }
//                }
                if (!memberAdded) {
                    if (mMeetingClient != null) {
                        if (mMeetingClient.getSocketId() != null) {
                            try {
                                memberAdded = true;
                                putMember();
                                runOnUiThread(() -> {
                                    String txt = "New Meeting Started ";
                                    if (joining) {
                                        txt = "Joined Meeting ";
                                    }
                                    if (!mSideMenuOpened) {
                                        showCommonPopup(txt + " with id " + mMeetingId, true, WooCommonPopup.VERTICAL_POSITION_TOP);
                                        try {
                                            notificationSound.play();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    WooAnimationUtil.hideView(tvMeetingId, new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                            tvMeetingId.setVisibility(View.GONE);
                                        }
                                    });
                                    WooAnimationUtil.showView(bottomBarMeeting, new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                            bottomBarMeeting.setVisibility(View.VISIBLE);
                                        }
                                    });
//                    mBinding.tvMeetingId.setVisibility(View.GONE);
//                    mBinding.bottomBarMeeting.setVisibility(View.VISIBLE);
                                });
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                        } else {
                            runOnUiThread(() -> {
                                try {
                                    showErrorDialog("Socket Error", "There was an error during network communication. Please try again lager.", true);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            });
                        }
                    } else {
                        runOnUiThread(() -> {
                            try {
                                showErrorDialog("Internal Error", "There was an error during network communication. Please try again later", true);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        });
                    }
                }
                return true;
            case WEvents.EVENT_TYPE_PRODUCER_CREATED:
                Log.d(TAG, "<< Handler Event PRODUCER CREATED [" + msg.obj + "]");
                return true;
            case WEvents.EVENT_TYPE_CONSUMER_CREATED:
                Log.d(TAG, "<< Handler Event CONSUMER CREATED [" + msg.obj + "]");
                fetchRoomData2();
                return true;
            case WEvents.EVENT_TYPE_CONSUME_BACK_CREATED:
                Log.d(TAG, "<< Handler Event CONSUME BACK CREATED [" + msg.obj + "]");
                fetchRoomData2();
                return true;
            case WEvents.EVENT_TYPE_PEER_DISCONNECTED:
                if (msg.obj != null) {
                    JSONObject p = (JSONObject) msg.obj;
                    try {
                        String username = p.getString("username");
                        Log.d(TAG, "<< Handler Event Peer Disconnected with name >> " + username);
                        runOnUiThread(() -> {
                            showCommonPopup(username + " left", true, WooCommonPopup.VERTICAL_POSITION_TOP);
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            case WEvents.EVENT_TYPE_SOCKET_DISCONNECTED:
                Log.d(TAG, "<< Handler Event Socket disconnected with socket id: [" + msg.obj + "]");
                runOnUiThread(() -> {
                    if (droidAudioManager != null) {
                        droidAudioManager.setSpeakerphoneOn(false);
                        droidAudioManager.setMode(AudioManager.MODE_NORMAL);
                    }
                });
//                if (peersList.isEmpty()) {
//                    // Destroy and finish()
//                    onBackPressed();
//                }
                return true;
            case WEvents.EVENT_RECEIVED_MESSAGE:
                Log.d(TAG, "<< Handler Event : Chat Message Received in handler");
                com.woooapp.meeting.net.models.Message message = (com.woooapp.meeting.net.models.Message) msg.obj;
                if (message != null) {
                    runOnUiThread(() -> {
                        showCommonPopup("From " + message.getName() + "\n" + message.getMessage(), true, WooCommonPopup.VERTICAL_POSITION_CENTER);
                        chatList.add(new Chat(Chat.MESSAGE_TYPE_RECV, message));
                        chatAdapter.notifyDataSetChanged();
                        try {
                            notificationSound.play();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    setChatTranslation(message);
                }
                return true;
            case WEvents.EVENT_SENT_MESSAGE:
                Log.d(TAG, "Message " + msg.obj + " Sent successfully");
                com.woooapp.meeting.net.models.Message message1 = (com.woooapp.meeting.net.models.Message) msg.obj;
                if (message1 != null) {
                    runOnUiThread(() -> {
                        chatList.add(new Chat(Chat.MESSAGE_TYPE_SENT, message1));
                        chatAdapter.notifyDataSetChanged();
                    });
                }
                return true;
            case WEvents.EVENT_FAILURE_MESSAGE:
                Log.w(TAG, "Failed to send chat message " + msg.obj);

                return true;
            case WEvents.EVENT_ME_MIC_TURNED_ON:
                runOnUiThread(() -> {
                    buttonMic.setImageResource(R.drawable.baseline_mic_34);
                });
                if (mMeetingClient != null) {
                    if (mMeetingClient.getSocketId() != null) {
                        ApiManager.build(this).putStates(ApiManager.URL_MIC_UN_MUTED, mMeetingId, mMeetingClient.getSocketId(), new ApiManager.ApiResult2() {
                            @Override
                            public void onResult(Call call, Response response) {
                                try {
                                    Log.d(TAG, "<< MIC UN MUTED RESPONSE >>> " + response.body().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call call, Object e) {

                            }
                        });
                        if (mRoomStore.getMe() != null) {
                            mRoomStore.getMe().postValue(me -> {
                                me.setMicOn(true);
                            });
                        }
                    }
                }
                return true;
            case WEvents.EVENT_ME_MIC_TURNED_OFF:
                runOnUiThread(() -> {
                    buttonMic.setImageResource(R.drawable.icon_mic_white_off);
                });
                if (mMeetingClient == null) {
                    return true;
                }
                if (mMeetingClient.getSocketId() == null) {
                    return true;
                }
                ApiManager.build(this).putStates(ApiManager.URL_MIC_MUTED, mMeetingId, mMeetingClient.getSocketId(), new ApiManager.ApiResult2() {
                    @Override
                    public void onResult(Call call, Response response) {
                        try {
                            Log.d(TAG, "<< MIC MUTED RESPONSE >>> " + response.body().string());
                        } catch (NullPointerException | IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call call, Object e) {

                    }
                });
                if (mRoomStore.getMe() != null) {
                    mRoomStore.getMe().postValue(me -> {
                        me.setMicOn(false);
                    });
                }
                return true;
            case WEvents.EVENT_ME_CAM_TURNED_ON:
                runOnUiThread(() -> {
                    buttonCam.setImageResource(R.drawable.ic_video_camera_white);
                });
                if (mRoomStore != null) {
                    if (mRoomStore.getMe() != null) {
                        mRoomStore.getMe().postValue(me -> {
                            me.setCamOn(true);
                        });
                    }
                }
                return true;
            case WEvents.EVENT_ME_CAM_TURNED_OFF:
                runOnUiThread(() -> {
                    buttonCam.setImageResource(R.drawable.ic_camera_off_gray);
                });
                if (mRoomStore != null) {
                    if (mRoomStore.getMe() != null) {
                        mRoomStore.getMe().postValue(me -> {
                            me.setCamOn(false);
                        });
                    }
                }
                return true;
            case WEvents.EVENT_ON_TEXT_TRANSLATION_RECV:
                if (msg.obj != null) {
                    try {
                        JSONObject obj = new JSONObject(String.valueOf(msg.obj));
                        String translation = obj.getString("translation");
                        String original = obj.getString("original");
                        String socketId = obj.getString("socketId");
                        boolean isFinal = obj.getBoolean("isFinal");

                        Transcript transcript = new Transcript(translation, original, socketId);
                        if (mRoomStore != null) {
                            mRoomStore.getPeers().postValue(peers -> {
                                if (peers.getPeer(socketId) != null) {
                                    transcript.setSenderName(peers.getPeer(socketId).getDisplayName());
                                }

                                transcriptList.add(transcript);
                                if (transcriptAdapter != null)
                                    transcriptAdapter.replaceList(transcriptList);
                                if (mMeetingClient.isTextTranslationOn()) {
                                    onTranslation(original, translation);
                                }
                            });
                        } else {
                            transcriptList.add(transcript);
                            if (transcriptAdapter != null)
                                transcriptAdapter.replaceList(transcriptList);
                            if (mMeetingClient.isTextTranslationOn()) {
                                onTranslation(original, translation);
                            }
                        }
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
                return true;
            case WEvents.EVENT_VOICE_TRANSLATION_RECEIVED:
                if (msg.obj != null) {
                    JSONObject obj = (JSONObject) msg.obj;
                    if (obj != null) {
                        try {
                            byte[] translatedVoice = (byte[]) obj.get("translatedVoice");
                            byte[] originalVoice = (byte[]) obj.get("originalVoice");
                            String socketId = obj.getString("socketId");

                            runOnUiThread(() -> {
                                try {
                                    new WVoicePlayback(new VoiceDataSource(translatedVoice), -1f);
                                    new Handler().postDelayed(() -> {
                                        new WVoicePlayback(new VoiceDataSource(originalVoice), 0.5f).setVolume(0.5f);
                                    }, 1000);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            });
                            mRoomStore.getPeers().postValue(peers -> {
                                Peer p = peers.getPeer(socketId);
                                if (p != null) {
                                    String peerName = p.getDisplayName();
                                    if (peerName != null) {
                                        runOnUiThread(() -> {
                                            showCommonPopup(peerName + " speaking", true, WooCommonPopup.VERTICAL_POSITION_TOP);
                                        });
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                return true;
            case WEvents.EVENT_CLICKED_LANGUAGE_SELECT:
                showLanguageSelectionSheet();
                return true;
            case WEvents.EVENT_NEW_PEER_JOINED:
                if (msg.obj != null) {
                    runOnUiThread(() -> {
//                        showCommonPopup(msg.obj + " joined", true, WooCommonPopup.VERTICAL_POSITION_TOP);
////                        try {
////                            notificationSound.play();
////                        } catch (Exception e) {
////                            e.printStackTrace();
////                        }
                    });
                }
                return true;
            case WEvents.EVENT_PEER_EXITED:
                if (msg.obj != null) {
                    runOnUiThread(() -> {
                        showCommonPopup(String.valueOf(msg.obj) + " left!", true, WooCommonPopup.VERTICAL_POSITION_TOP);
                        try {
                            notificationSound.play();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    return true;
                }
                return false;
            case WEvents.EVENT_ME_HAND_RAISED:
                if (mMeetingClient != null) {
                    if (mMeetingClient.getSocketId() != null) {
                        ApiManager.build(this).putStates(ApiManager.URL_HAND_RAISED, mMeetingId, mMeetingClient.getSocketId(), new ApiManager.ApiResult2() {
                            @Override
                            public void onResult(Call call, Response response) {
                                try {
                                    Log.d(TAG, "<< ME HAND RAISED RESPONSE >>> " + response.body().string());
                                    mMeetingClient.emitHandRaisedState(true);
                                } catch (IOException | JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call call, Object e) {

                            }
                        });
                        if (mRoomStore.getMe() != null) {
                            mRoomStore.getMe().postValue(me -> {
                                me.setHandRaised(true);
                            });
                        }
                        return true;
                    }
                }
                return false;
            case WEvents.EVENT_ME_HAND_LOWERED:
                if (mMeetingClient != null) {
                    if (mMeetingClient.getSocketId() != null) {
                        ApiManager.build(this).putStates(ApiManager.URL_HAND_LOWERED, mMeetingId, mMeetingClient.getSocketId(), new ApiManager.ApiResult2() {
                            @Override
                            public void onResult(Call call, Response response) {
                                try {
                                    Log.d(TAG, "<< ME HAND LOWERED RESPONSE >>> " + response.body().string());
                                    mMeetingClient.emitHandRaisedState(false);
                                } catch (IOException | JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call call, Object e) {

                            }
                        });
                        if (mRoomStore.getMe() != null) {
                            mRoomStore.getMe().postValue(me -> {
                                me.setHandRaised(false);
                            });
                        }
                        return true;
                    }
                }
                return false;
            case WEvents.EVENT_TRANSLATION_ENABLED:
                enableTranslations();
                return true;
            case WEvents.EVENT_TRANSLATION_DISABLED:
                disableTranslation();
                return true;
            case WEvents.EVENT_PEER_HAND_LOWERED:
                JSONObject handLowered = (JSONObject) msg.obj;
                if (handLowered != null) {
                    mRoomStore.getPeers().postValue(peers -> {
                        try {
                            Peer p = peers.getPeer(handLowered.getString("socketId"));
                            if (p != null) {
                                showCommonPopup(p.getDisplayName() + " Lowered Hand", true, WooCommonPopup.VERTICAL_POSITION_TOP);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                }
                return true;
            case WEvents.EVENT_PEER_HAND_RAISED:
                JSONObject handRaised = (JSONObject) msg.obj;
                if (handRaised != null) {
                    mRoomStore.getPeers().postValue(peers -> {
                        try {
                            Peer p = peers.getPeer(handRaised.getString("socketId"));
                            if (p != null) {
                                showCommonPopup(p.getDisplayName() + " wants to say something!", true, WooCommonPopup.VERTICAL_POSITION_TOP);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                }
                return true;
            case WEvents.EVENT_SELECT_BACKGROUND:
                openImageChooser();
                return true;
            case WEvents.EVENT_SHOW_MEMBERS:
                showMembersSheet();
                return true;
            case WEvents.EVENT_NETWORK_CONNECTIVITY_CHANGED:
                if (msg.obj != null) {
                    Intent intent = (Intent) msg.obj;
                    if (intent != null) {
                        String action = intent.getAction();
                        Log.d(TAG, "<< Receiver Action >>>> " + action);
                    }
                }
                return true;
            case WEvents.EVENT_ADD_PASSWORD:
                try {
                    setPassword();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return true;
            case WEvents.EVENT_RECEIVED_MUTE_EVERYONE:
                if (mMeetingClient != null) {
                    if (mMeetingClient.isMicOn()) {
                        mMeetingClient.setMicOn(false);
                    }
                    try {
                        runOnUiThread(() -> {
                            showCommonPopup("Admin has muted everyone! Your mic has been turned off.", true, WooCommonPopup.VERTICAL_POSITION_TOP);
                        });
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return true;
                }
                return false;
            case WEvents.EVENT_RECEIVED_MUTE_MEMBER:
                if (mMeetingClient != null) {
                    if (mMeetingClient.isMicOn()) {
                        mMeetingClient.setMicOn(false);
                    }
                    runOnUiThread(() -> {
                        try {
                            showCommonPopup("You have been muted by admin.", true, WooCommonPopup.VERTICAL_POSITION_TOP);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                    return true;
                }
                return false;
            case WEvents.EVENT_RECEIVED_CAM_OFF_MEMBER:
                if (mMeetingClient != null) {
                    if (mMeetingClient.isCamOn()) {
                        mMeetingClient.setCamOn(false);
                    }
                    runOnUiThread(() -> {
                        try {
                            showCommonPopup("Your camera has been turned off by admin.", true, WooCommonPopup.VERTICAL_POSITION_TOP);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                    return true;
                }
                return false;
            case WEvents.EVENT_RECEIVED_KICKOUT:
                if (mMeetingClient != null) {
                    runOnUiThread(() -> {
                        try {
                            kickmeOutImpl();
                            UIManager.showInfoDialog(MeetingActivity.this,
                                    "Removed",
                                    "You have been removed from meeting by admin.",
                                    new UIManager.DialogCallback() {
                                        @Override
                                        public void onPositiveButton(@Nullable Object sender, @Nullable Object data) {
                                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                                            finish();
                                        }

                                        @Override
                                        public void onNeutralButton(@Nullable Object sender, @Nullable Object data) {

                                        }

                                        @Override
                                        public void onNegativeButton(@Nullable Object sender, @Nullable Object data) {

                                        }
                                    });
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                    return true;
                }
                return false;
            case WEvents.EVENT_NEW_ADMIN_CREATED:
                fetchRoomData2();
                try {
                    runOnUiThread(() -> {
                        showCommonPopup("New Admin added successfully", true, WooCommonPopup.VERTICAL_POSITION_TOP);
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return true;
            case WEvents.EVENT_ON_NEW_ADMIN:
                JSONObject obj = (JSONObject) msg.obj;
                if (obj != null) {
                    try {
                        String accountUniqueId = obj.getString("accountUniqueId");
                        if (accountUniqueId.equals(this.accountUniqueId)) {
                            if (mMeetingClient != null) {
                                if (mMeetingClient.getRole() != MeetingClient.Role.ADMIN) {
                                    mMeetingClient.setRole(MeetingClient.Role.ADMIN);
                                    try {
                                        runOnUiThread(() -> {
                                            showCommonPopup("You have been made admin", true, WooCommonPopup.VERTICAL_POSITION_TOP);
                                        });
                                        fetchRoomData2();
                                        WEvents.getInstance().notify(WEvents.EVENT_PEER_ADAPTER_NOTIFY, "Update");
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            case WEvents.EVENT_CONNECTION_STATE_FAILED:
                if (uiManager != null) {
                    try {
                        showCommonPopup("Network not available ...", true, WooCommonPopup.VERTICAL_POSITION_TOP);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                return true;
            case WEvents.EVENT_CONNECTION_STATE_CONNECTING:
                if (uiManager != null) {
                    try {
                        showCommonPopup("Connecting ...", true, WooCommonPopup.VERTICAL_POSITION_TOP);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                return true;
            case WEvents.EVENT_ENABLE_SCREEN_SHARE:
                if (mMeetingClient != null) {
                    if (mMeetingClient.isPresenting()) {
                        mMeetingClient.shareScreen(false, null, null);
                    } else {
                        startScreenCapture();
                    }
                }
                return false;
            case WEvents.EVENT_PRESENTING:
                if (msg.obj != null) {
                    boolean presenting = (boolean) msg.obj;
                    if (presenting) {
                        showScreenShareView();
                    } else {
//                        disposeVideoCapturer();
                    }
                }
                return false;
            case WEvents.EVENT_REMOTE_SCREEN_SHARE_ON:
                if (msg.obj != null && mMeetingClient != null) {
                    JSONObject data = (JSONObject) msg.obj;
                    try {
                        String socketId = data.getString("socketId");
                        runOnUiThread(() -> {
                            showCommonPopup(username + " is Presenting now.", true, WooCommonPopup.VERTICAL_POSITION_TOP);
                            createPeerScreenShareView(socketId);
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            case WEvents.EVENT_REMOTE_SCREEN_SHARE_OFF:
                if (msg.obj != null && mMeetingClient != null) {
                    try {
                        JSONObject data = (JSONObject) msg.obj;
                        final String socketId = data.getString("socketId");
                        final String roomId = data.getString("roomId");
                        final String username = data.getString("username");

                        stopPeerScreenShare(socketId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    /**
     *
     */
    private void createPeerScreenShareView(@NonNull String peerId) {
        if (mRoomStore.getPeers() != null) {
            mRoomStore.getPeers().postValue(peers -> {
                Peer peer = peers.getPeer(peerId);
                if (layoutPeerScreenShare != null && mMeetingClient != null && peer != null) {
                    layoutPeerScreenShare.setVisibility(View.VISIBLE);
                    View contentView = LayoutInflater.from(MeetingActivity.this).inflate(R.layout.layout_screen_share_peer_view, null);
                    if (contentView != null) {
                        ScreenSharePeerView pv = contentView.findViewById(R.id.screenSharePeerView);
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                                (int) SP.valueOf(MeetingActivity.this, 500));
                        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                        TextView tvTitle = contentView.findViewById(R.id.screenShareTv);
                        tvTitle.setText(peer.getDisplayName() + " is presenting");
                        PeerProps props = new PeerProps(getApplication(), mRoomStore);
                        pv.setProps(props, mMeetingClient, peer.getId());
                        layoutPeerScreenShare.addView(contentView, params);
                        props.connect(MeetingActivity.this, peer.getId());
                    }
                }
            });
        }
    }

    private void stopPeerScreenShare(@NonNull String peerId) {
        if (mRoomStore.getPeers() != null) {
            mRoomStore.getPeers().postValue(peers -> {
                Peer peer = peers.getPeer(peerId);
                if (layoutPeerScreenShare != null && mMeetingClient != null && peer != null) {
                    WooAnimationUtil.hideView(layoutPeerScreenShare, new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            layoutPeerScreenShare.setVisibility(View.GONE);
                            layoutPeerScreenShare.removeAllViews();
                            mMeetingClient.closeConsumer(peerId);
                            mRoomStore.removePeer(peerId);
                        }
                    });
                }
            });
        }
    }

    private void showNetworkErrorDialog() {
        runOnUiThread(() -> {
            try {
                progressDialog.dismiss();
                UIManager.showErrorDialog(
                        MeetingActivity.this,
                        "Connection Error",
                        "Failed to fetch remote data. Please try again later",
                        "OK",
                        R.drawable.ic_connection_error_34,
                        new UIManager.DialogCallback() {
                            @Override
                            public void onPositiveButton(@Nullable Object sender, @Nullable Object data) {
                                finish();
                            }

                            @Override
                            public void onNeutralButton(@Nullable Object sender, @Nullable Object data) {

                            }

                            @Override
                            public void onNegativeButton(@Nullable Object sender, @Nullable Object data) {

                            }
                        });
                closeDrawer();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void openImageChooser() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSION_EXTERNAL_STORAGE_CODE);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            launcher.launch(intent);
        }
    }

    private void setChatTranslation(@NonNull final com.woooapp.meeting.net.models.Message message) {
        if (langSelected) {
            ApiManager.build(this).getTranslation(message.getMessage(), selectedLanguageCode, new ApiManager.ApiResult2() {
                @Override
                public void onResult(Call call, Response response) {
                    if (response != null) {
                        try {
                            String resp = response.body().string();
                            Log.d(TAG, "<< Chat Translation Response >>> " + resp);
                            if (resp != null) {
                                ChatTranslation trans = ChatTranslation.fromJson(resp);
                                if (trans != null) {
                                    if (trans.isSuccess() && trans.getData() != null) {
                                        for (int i = 0; i < chatList.size(); i++) {
                                            if (chatList.get(i).getMessage().getMessage().equals(message.getMessage())) {
                                                chatList.get(i).setTranslation(trans.getData().getText());
                                                runOnUiThread(() -> {
                                                    chatAdapter.notifyDataSetChanged();
                                                });
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call call, Object e) {

                }
            });
        }
    }

    private void showMembersSheet() {
        try {
            List<Member> members = new LinkedList<>();
            if (roomData.getMembers() != null) {
                for (RoomData.Member member : roomData.getMembers()) {
                    members.add(new Member(member.getAccountUniqueId(),
                            member.getSocketId(),
                            member.getUsername(),
                            member.getPicture(),
                            MeetingClient.Role.USER));
                }
            }

            if (roomData.getAdmins() != null) {
                for (RoomData.Admin admin : roomData.getAdmins()) {
                    for (int i = 0; i < members.size(); i++) {
                        if (admin.getAccountUniqueId().equals(members.get(i).getAccountUniqueId())) {
                            members.get(i).setRole(MeetingClient.Role.ADMIN);
                        }
                    }
                }
            }

            BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.SheetDialog);
            View v = LayoutInflater.from(this).inflate(R.layout.layout_sheet_members, null);
            TextView tvTitle = v.findViewById(R.id.tvTitle);
            tvTitle.setText(members.size() + " " + tvTitle.getText());
            ListView lv = v.findViewById(R.id.listView);
            lv.setAdapter(new MemberAdapter(this, members));
            dialog.setContentView(v);
            dialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showErrorDialog(@NonNull String title, @NonNull String message, boolean finishOnDismiss) {
        UIManager.showErrorDialog(this,
                title,
                message,
                "OK",
                R.drawable.ic_warning,
                new UIManager.DialogCallback() {
                    @Override
                    public void onPositiveButton(@Nullable Object sender, @Nullable Object data) {
                        if (finishOnDismiss) {
                            destroyMeeting();
                        }
                    }

                    @Override
                    public void onNeutralButton(@Nullable Object sender, @Nullable Object data) {

                    }

                    @Override
                    public void onNegativeButton(@Nullable Object sender, @Nullable Object data) {

                    }
                });
    }

    /**
     * @param original
     * @param translation
     */
    private void onTranslation(@NonNull String original, @NonNull String translation) {
        if (this.translationLayout.getVisibility() == View.VISIBLE) {
            tvTransOriginal.setText(original);
            tvTransOriginal.setBackgroundResource(R.drawable.bg_rounded_default);
            tvTransTrans.setText(translation);
            tvTransTrans.setBackgroundResource(R.drawable.bg_rounded_default);
        }
    }

    private void enableTranslations() {
        if (translationLayout.getVisibility() == View.GONE) {
            translationLayout.setVisibility(View.VISIBLE);
            for (MeetingPageFragment2 fragment : pageFragments) {
                fragment.enableTranslation();
            }
            showCommonPopup("Translation turned on", true, WooCommonPopup.VERTICAL_POSITION_CENTER);
        }
    }

    private void disableTranslation() {
        if (translationLayout.getVisibility() == View.VISIBLE) {
            tvTransOriginal.setText("");
            tvTransTrans.setText("");
            tvTransTrans.setBackgroundResource(android.R.color.transparent);
            tvTransOriginal.setBackgroundResource(android.R.color.transparent);
            ;
            translationLayout.setVisibility(View.GONE);
            for (MeetingPageFragment2 fragment : pageFragments) {
                fragment.disableTranslation();
            }
            showCommonPopup("Translation turned off", true, WooCommonPopup.VERTICAL_POSITION_CENTER);
        }
    }

    private void createPageControl(int selectedPosition) {
        pageControlLayout.removeAllViews();
        if (pageFragments.size() > 1) {
            for (int i = 0; i < pageFragments.size(); i++) {
                pageControlLayout.addView(createNewPageControl(i == selectedPosition, i + 1),
                        (i == selectedPosition) ? getSelectedPageControlParams() : getUnSelectedPageControlParams());
            }
        }
    }

    private void hidePageControl() {
        pageControlLayout.removeAllViews();
    }

    /**
     * @param selectedPosition
     */
    private void updatePageControl(int selectedPosition) {
        if (pageFragments.size() > 1) {
            for (int i = 0; i < pageFragments.size(); i++) {
                if (pageControlLayout.getChildAt(i) != null) {
                    if (i == selectedPosition) {
                        if (pageControlLayout.getChildAt(i) != null) {
                            pageControlLayout.getChildAt(i).setLayoutParams(getSelectedPageControlParams());
                            pageControlLayout.getChildAt(i).setAlpha(0.6f);
                        }
                    } else {
                        pageControlLayout.getChildAt(i).setLayoutParams(getUnSelectedPageControlParams());
                        pageControlLayout.getChildAt(i).setAlpha(1.0f);
                    }
                }
            }
        }
    }

    /**
     * @param selected
     * @param pageNo
     * @return
     */
    private View createNewPageControl(boolean selected, int pageNo) {
        View v = LayoutInflater.from(this).inflate(R.layout.page_control, null);
        TextView tv = v.findViewById(R.id.pageControlTv);
        tv.setTextColor(Color.BLACK);
        tv.setText(String.valueOf(pageNo));
        if (selected) {
            v.setAlpha(1f);
        } else {
            v.setAlpha(1f);
        }
        return v;
    }

    /**
     * @return
     */
    private LinearLayout.LayoutParams getSelectedPageControlParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Display.dpToPx(this, 20), Display.dpToPx(this, 20));
        params.setMargins(3, 3, 3, 3);
        return params;
    }

    /**
     * @return
     */
    private LinearLayout.LayoutParams getUnSelectedPageControlParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Display.dpToPx(this, 20), Display.dpToPx(this, 20));
        params.setMargins(1, 1, 1, 1);
        return params;
    }

    /**
     * @param message
     * @param autoDismiss
     * @param verticalAlignment
     */
    private void showCommonPopup(@NonNull String message, boolean autoDismiss, int verticalAlignment) {
        try {
            WooCommonPopup popup = new WooCommonPopup(this, this.mainContainer, message, autoDismiss, verticalAlignment);
            popup.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void sendBackgroundNotification() {
        String channelId = "Woooo_Meeting";
        NotificationChannelCompat channelCompat =
                new NotificationChannelCompat.Builder(channelId, NotificationManagerCompat.IMPORTANCE_HIGH)
                        .setName("Woooo Meeting")
                        .setDescription("Meeting in progress")
                        .build();
        notificationManager.createNotificationChannel(channelCompat);

        Intent intent = new Intent(getApplicationContext(), MeetingActivity.class);
        intent.putExtra("meetingId", mMeetingId);
        if (mMeetingClient != null) {
            intent.putExtra("isAdmin", mMeetingClient.getRole() == MeetingClient.Role.ADMIN);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0x9a, intent,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? PendingIntent.FLAG_IMMUTABLE : PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.app_logo)
                .setContentTitle("Woooo Meeting in progress")
                .setContentText("Tap to go back to room")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_logo))
                .setOngoing(true)
                .setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        notificationManager.notify(MEETING_NOTIFICATION_ID, builder.build());
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                if (data.getData() != null) {
                    Uri uri = data.getData();
                    try {
                        Bitmap selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        ivBg.setImageBitmap(selectedBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    });

    private void destroyCallbackHandler() {
        WEvents.getInstance().removeHandler(this.callbackHandler);
    }

    public void kickmeOutImpl() {
        if (this.mMeetingClient != null) {
            this.mMeetingClient.close();
            this.mMeetingClient = null;
        }
        if (mRoomStore != null) {
            mRoomStore = null;
        }
        chatList.clear();
        WDirector.getInstance().dispose();
        destroyCallbackHandler();
        if (droidAudioManager != null) {
            droidAudioManager.setSpeakerphoneOn(false);
            droidAudioManager.setMode(AudioManager.MODE_NORMAL);
        }
    }

    /**
     * @param serviceClz
     * @return boolean
     */
    public boolean isServiceRunning(Class<?> serviceClz) {
        ActivityManager m = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : m.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClz.getName().equalsIgnoreCase(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void disposeVideoCapturer() {
        if (capturer != null) {
            try {
                if (isServiceRunning(ScreenCaptureService.class)) {
                    Intent intent = new Intent(MeetingActivity.this, ScreenCaptureService.class);
                    stopService(intent);
                    Log.d(TAG, "<< Stopped Screen Capture Service ....");
                }
//                capturer.stopCapture();
//                capturer.dispose();
                capturer = null;
                Log.d(TAG, "<< Stopped VideoCapturer ...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (mediaProjectionManager != null) {
            mediaProjectionManager = null;
        }

        runOnUiThread(() -> {
            if (layoutScreenShare != null) {
                if (layoutScreenShare.getVisibility() == View.VISIBLE) {
                    WooAnimationUtil.hideView(layoutScreenShare, new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            layoutScreenShare.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }

    public void destroyMeeting() {
        if (this.mMeetingClient != null) {
            this.mMeetingClient.close();
            this.mMeetingClient = null;
        }
        if (mRoomStore != null) {
            mRoomStore = null;
        }

        disposeVideoCapturer();
        chatList.clear();
        WDirector.getInstance().dispose();
        destroyCallbackHandler();
        if (droidAudioManager != null) {
            droidAudioManager.setSpeakerphoneOn(false);
            droidAudioManager.setMode(AudioManager.MODE_NORMAL);
        }
        new Handler().postDelayed(() -> {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            finish();
        }, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (notificationManager != null) {
            notificationManager.cancel(MEETING_NOTIFICATION_ID);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (notificationManager != null && !isFinishing()) {
            moveTaskToBack(false);
            sendBackgroundNotification();
        }
    }

    @Override
    protected void onDestroy() {
//        destroyMeeting();
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            Log.d(TAG, "New Intent Received!");
            Log.d(TAG, "Meeting ID -> " + intent.getStringExtra("meetingId"));
            Log.d(TAG, "isAdmin -> " + intent.getBooleanExtra("isAdmin", false));
            isNewIntent = true;
        }
    }

    @Override
    public void onBackPressed() {
        if (mSideMenuOpened) {
            closeDrawer();
        } else {
            if (notificationManager != null && !isFinishing()) {
                moveTaskToBack(false);
                sendBackgroundNotification();
            }
        }
    }

} // end class
