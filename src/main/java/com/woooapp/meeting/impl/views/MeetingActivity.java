package com.woooapp.meeting.impl.views;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.woooapp.meeting.device.Display;
import com.woooapp.meeting.impl.utils.WooDirector;
import com.woooapp.meeting.impl.utils.WooEvents;
import com.woooapp.meeting.impl.utils.WooMediaPlayer;
import com.woooapp.meeting.impl.views.adapters.LangSelectionAdapter;
import com.woooapp.meeting.impl.views.adapters.MeetingChatAdapter;
import com.woooapp.meeting.impl.views.adapters.MeetingPagerAdapter;
import com.woooapp.meeting.impl.views.adapters.MeetingTranscriptAdapter;
import com.woooapp.meeting.impl.views.adapters.MemberAdapter;
import com.woooapp.meeting.impl.views.adapters.PeerAdapter2;
import com.woooapp.meeting.impl.views.animations.WooAnimationUtil;
import com.woooapp.meeting.impl.views.fragments.MeetingPageFragment;
import com.woooapp.meeting.impl.views.fragments.MeetingPageFragment2;
import com.woooapp.meeting.impl.views.models.Chat;
import com.woooapp.meeting.impl.views.models.GridPeer;
import com.woooapp.meeting.impl.views.models.Languages;
import com.woooapp.meeting.impl.views.models.ListGridPeer;
import com.woooapp.meeting.impl.views.models.MeetingPage;
import com.woooapp.meeting.impl.views.models.Member;
import com.woooapp.meeting.impl.views.models.Transcript;
import com.woooapp.meeting.impl.views.popups.MeetingMorePopup;
import com.woooapp.meeting.impl.views.popups.WooCommonPopup;
import com.woooapp.meeting.impl.views.popups.WooProgressDialog;
import com.woooapp.meeting.impl.vm.EdiasProps;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import eu.siacs.conversations.R;
import eu.siacs.conversations.WooApplication;
import eu.siacs.conversations.entities.Account;
import eu.siacs.conversations.persistance.DatabaseBackend;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 6:36 pm 11/09/2023
 * <code>class</code> MeetingActivity.java
 */
public class MeetingActivity extends AppCompatActivity implements Handler.Callback {
    private static final String TAG = MeetingActivity.class.getSimpleName() + ".java";
    private static final int PERMISSIONS_REQ_CODE = 0x7b;
    private final String[] permissions = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    //    private ActivityMeetingBinding mBinding;
    private MeetingClient mMeetingClient;
    private CreateMeetingResponse createMeetingResponse;
    private PutMembersDataResponse putMembersDataResponse;
    private RoomData roomData;
    private RoomOptions mOptions;
    private RoomStore mRoomStore;
    private boolean joining = false;
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
    private List<MeetingPageFragment> pageFragments = new LinkedList<>();
    private WooMediaPlayer notificationSound;
    private LinearLayout pageControlLayout;
    private LinearLayout translationLayout;
    private TextView tvTransOriginal;
    private TextView tvTransTrans;
    private ImageView ivBg;
    private boolean isCamEnabled;
    private boolean isMicEnabled;
    private AudioManager droidAudioManager;
    private boolean memberAdded = false;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.uiManager = UIManager.getUIManager(this);
        WooEvents.getInstance().addHandler(callbackHandler);
        deviceWidthDp = Display.getDisplayWidth(this) - 50;
        deviceHeightDp = Display.getDisplayHeight(this);
        this.initComponents();
        this.bottomBarMeeting.setVisibility(View.GONE);

        this.droidAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        this.droidAudioManager.setSpeakerphoneOn(true);

        progressDialog = WooProgressDialog.make(this, "Setting up ...");
        progressDialog.show();
        createDrawer();

        this.langsJson = WooDirector.getInstance().readFileFromAssets(getAssets(), "languages.json");
        try {
            this.langs = Languages.fromJson(langsJson);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkForPermissions(permissions, PERMISSIONS_REQ_CODE);
        } else {
            setup();
        }

        this.buttonEnd.setOnClickListener(view -> {
            onBackPressed();
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
            try {
                this.morePopup = new MeetingMorePopup(
                        this, this.mainContainer,
                        this.bottomBarMeeting.getHeight(),
                        mMeetingClient);
                morePopup.show();
                fetchRoomData2();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        this.buttonAI.setOnClickListener(view -> {
            View contentView = LayoutInflater.from(this).inflate(R.layout.layout_bottom_sheet_translation, null);
            View buttonTextTrans = contentView.findViewById(R.id.buttonTransSheetText);
            ImageView ivText = contentView.findViewById(R.id.ivText);
            ImageView ivVoice = contentView.findViewById(R.id.ivVoice);
            if (mMeetingClient.isTextTranslationOn()) {
                ivText.setVisibility(View.VISIBLE);
            } else {
                ivText.setVisibility(View.GONE);
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
                        WooEvents.getInstance().notify(WooEvents.EVENT_TRANSLATION_ENABLED, null);
                    } else {
                        WooEvents.getInstance().notify(WooEvents.EVENT_TRANSLATION_DISABLED, null);
                    }
                }
                dialog.dismiss();
            });

            buttonVoiceTrans.setOnClickListener(v -> {
                if (mMeetingClient != null) mMeetingClient.setVoiceTranslation(!mMeetingClient.isVoiceTranslationOn());
                dialog.dismiss();
            });

            buttonCancel.setOnClickListener(v -> {
                dialog.dismiss();
            });
        });

        this.buttonMic.setOnClickListener(view -> {
            // Mic
            if (mMeetingClient.isMicOn()) {
                mMeetingClient.setMicOn(false);
            } else {
                mMeetingClient.setMicOn(true);
            }
        });

        this.buttonCam.setOnClickListener(view -> {
            // Cam
            if (mMeetingClient.isCamOn()) {
                mMeetingClient.setCamOn(false);
            } else {
                mMeetingClient.setCamOn(true);
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

        this.meetingBackground.setOnTouchListener((view, motionEvent) -> true);

        this.notificationSound = new WooMediaPlayer(getResources().openRawResourceFd(R.raw.meet_sound));
        this.notificationSound.setVolume(0.5f);
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

    private void setup() {
//        pd.show();
        this.isCamEnabled = getIntent().getBooleanExtra("camOn", true);
        this.isMicEnabled = getIntent().getBooleanExtra("micOn", true);
        this.email = getIntent().getStringExtra("email");
        this.accountUniqueId = getIntent().getStringExtra("accountUniqueId");
        this.username = getIntent().getStringExtra("username");
        this.picture = getIntent().getStringExtra("picture");
        if (picture != null) {
            if (picture.isEmpty())
                picture = "https://picsum.photos/200";
        } else {
            picture = "https://picsum.photos/200";
        }
        this.joining = getIntent().getBooleanExtra("joining", false);
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
    }

    private void joinMeeting() {
        initMeetingClient();
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
            }
            this.showLanguageSelectionSheet();
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
                }
            });
            mSideMenuOpened = true;
        }
    }

    private void showLanguageSelectionSheet() {
        try {
            closeDrawer();
            View view = LayoutInflater.from(this).inflate(R.layout.layout_dialog_language_select, null);
            Button buttCancel = view.findViewById(R.id.buttonLangSelectCancel);
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
    }

    private void initViewModel() {
//        EdiasProps.Factory factory = new EdiasProps.Factory(getApplication(), mRoomStore);

//        RoomProps roomProps = new ViewModelProvider(this, factory).get(RoomProps.class);
        RoomProps roomProps = new RoomProps(getApplication(), mRoomStore);
        roomProps.connect(this);

//        mBinding.setMeetProps(roomProps);

        // Set Meeting ID Link
        this.tvMeetingId.setText(mMeetingId);

        // PeerView
//        peerGridAdapter = new MeetingGridAdapter(this, this, mRoomStore, mMeetingClient);
//        peerGridAdapter.setTabBarHeight(mBinding.bottomBarMeeting.getHeight());
//        mBinding.gridViewMeeting.setAdapter(peerGridAdapter);
//        mBinding.gridViewMeeting.setEnabled(false);
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

        final MeetingPageFragment f1 = new MeetingPageFragment(1, this, mRoomStore, mMeetingClient, this);
        pageFragments.add(f1);
        final MeetingPageFragment f2 = new MeetingPageFragment( 2, this, mRoomStore, mMeetingClient, this);
        final MeetingPageFragment f3 = new MeetingPageFragment( 3, this, mRoomStore, mMeetingClient, this);

        mRoomStore
                .getPeers()
                .observe(
                        this,
                        peers -> {
                            peersList = peers.getAllPeers();
                            listGridPeer = new LinkedList<>();
                            List<MeetingPage> pageList = new LinkedList<>();
                            if (peersList.size() <= 5) {
                                List<ListGridPeer> pl1 = createListGridPeers(peersList, true);
                                MeetingPage page1 = new MeetingPage(1, pl1);
                                pageList.add(page1);
                                pageFragments.get(0).replacePage(page1);
                                if (pageFragments.size() > 1) {
                                    for (int i = 0; i < pageFragments.size(); i++) {
                                        if (i > 0) {
                                            pageFragments.remove(i);
                                        }
                                    }
                                }
                            } else if (peersList.size() > 5 && peersList.size() <= 12) {
                                List<ListGridPeer> pl2 = createListGridPeers(peersList.subList(0, 5), true);
                                List<ListGridPeer> pl3 = new ArrayList<>();
                                if (peersList.size() == 6) {
                                    pl3.add(new ListGridPeer(ListGridPeer.VIEW_TYPE_PEER_PEER, peersList.get(5), null));
                                } else {
                                    pl3 = createListGridPeers(peersList.subList(5, peersList.size()), false);
                                }
                                MeetingPage page1 = new MeetingPage(1, pl2);
                                MeetingPage page2 = new MeetingPage(2, pl3);
                                pageList.add(page1);
                                pageList.add(page2);
                                pageFragments.get(0).replacePage(page1);
                                if (pageFragments.size() < 2) {
                                    pageFragments.add(f2);
                                    f2.replacePage(page2);
                                } else {
                                    pageFragments.get(1).replacePage(page2);
                                }
                                if (pageFragments.size() > 2) {
                                    for (int i = 0; i < pageFragments.size(); i++) {
                                        if (i > 1) {
                                            pageFragments.remove(i);
                                        }
                                    }
                                }
                            } else if (peersList.size() > 11 && peersList.size() <= 18) {
                                List<ListGridPeer> pl4 = createListGridPeers(peersList.subList(0, 5), true);
                                List<ListGridPeer> pl5 = createListGridPeers(peersList.subList(6, 11), false);
                                List<ListGridPeer> pl6 = new ArrayList<>();
                                if (peersList.size() == 12) {
                                    pl6.add(new ListGridPeer(ListGridPeer.VIEW_TYPE_PEER_PEER, peersList.get(11), null));
                                } else {
                                    pl6 = createListGridPeers(peersList.subList(11, peersList.size() - 1), false);
                                }
                                MeetingPage page1 = new MeetingPage(1, pl4);
                                MeetingPage page2 = new MeetingPage(2, pl5);
                                MeetingPage page3 = new MeetingPage(3, pl6);
                                pageList.add(page1);
                                pageList.add(page2);
                                pageList.add(page3);
                                pageFragments.get(0).replacePage(page1);
                                pageFragments.get(1).replacePage(page2);
                                if (pageFragments.size() < 3) {
                                    pageFragments.add(f3);
                                    f3.replacePage(page3);
                                } else {
                                    pageFragments.get(2).replacePage(page3);
                                }
                            }
                            Log.d(TAG, "Peer list size[" + peersList.size() + "]");
                            Log.d(TAG, "Pages Size >>> " + pageList.size());
                            peersPagerAdapter.replaceFragments(pageFragments, pageList);
                            updatePageControl(0);

                            if (pageFragments.size() > 1) {
                                createPageControl(0);
                            } else {
                                hidePageControl();
                            }

//                            if (mBinding.meetingViewPager.findViewWithTag(pageList.get(0).getPageNo()) != null) {
//                                peersPagerAdapter.notifyDataSetChanged();
//                                mBinding.meetingViewPager.findViewWithTag(pageList.get(0).getPageNo()).invalidate();
//                            }
//                            PeersFragment fragment1 = new PeersFragment(MeetingActivity.this,
//                                    MeetingActivity.this,
//                                    mRoomStore,
//                                    mMeetingClient);
//                            View view = fragment1.createView(LayoutInflater.from(MeetingActivity.this), null);
//                            fragment1.viewCreated(view);
//                            mNavigationLayout.addView(view);
//                            if (pageList.size() > 0) {
//                                fragment1.replacePeers(pageList.get(0).getmPeers());
//                            }
//                            for (MeetingPage page : pageList) {
//                                View view = fragment1.createView(LayoutInflater.from(MeetingActivity.this), null);
//                                fragment1.viewCreated(view);
//                                mNavigationLayout.addView(view);
//
//                                fragment1.replacePeers(page.getmPeers());
//                            }
//                            mNavigationLayout.invalidate();
//                            mBinding.meetingFrameLayout.invalidate();
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
            if (grantResults.length >= 2) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
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
    protected void onResume() {
        super.onResume();
        this.callbackHandler = new Handler(this);
//        WooEvents.getInstance().addHandler(this.callbackHandler);
    }

    @Override
    protected void onPause() {
        super.onPause();
        destroyCallbackHandler();
    }

    @Override
    protected void onStop() {
        super.onStop();
        destroyCallbackHandler();
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        Log.d(TAG, "<< Handler Event >>> " + msg.what);
        switch (msg.what) {
            case WooEvents.EVENT_TYPE_SOCKET_ID:
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
            case WooEvents.EVENT_TYPE_PRODUCER_CREATED:
                Log.d(TAG, "<< Handler Event PRODUCER CREATED [" + msg.obj + "]");
//                if (joining) {
//                }
                return true;
            case WooEvents.EVENT_TYPE_CONSUMER_CREATED:
                Log.d(TAG, "<< Handler Event CONSUMER CREATED [" + msg.obj + "]");
                return true;
            case WooEvents.EVENT_TYPE_CONSUME_BACK_CREATED:
                Log.d(TAG, "<< Handler Event CONSUME BACK CREATED [" + msg.obj + "]");
                return true;
            case WooEvents.EVENT_TYPE_PEER_DISCONNECTED:
                Log.d(TAG, "<< Handler Event Peer Disconnected with socket id >> " + msg.obj);
                runOnUiThread(() -> {
                    showCommonPopup(msg.obj + " left", true, WooCommonPopup.VERTICAL_POSITION_TOP);
                });
                return true;
            case WooEvents.EVENT_TYPE_SOCKET_DISCONNECTED:
                Log.d(TAG, "<< Handler Event Socket disconnected with socket id: [" + msg.obj + "]");
                runOnUiThread(() -> {
                    if (droidAudioManager != null) {
                        droidAudioManager.setSpeakerphoneOn(false);
                        droidAudioManager.setMode(AudioManager.MODE_NORMAL);
                    }
                });
                if (peersList.isEmpty()) {
                    // Destroy and finish()
                    onBackPressed();
                }
                return true;
            case WooEvents.EVENT_RECEIVED_MESSAGE:
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
                }
                return true;
            case WooEvents.EVENT_SENT_MESSAGE:
                Log.d(TAG, "Message " + msg.obj + " Sent successfully");
                com.woooapp.meeting.net.models.Message message1 = (com.woooapp.meeting.net.models.Message) msg.obj;
                if (message1 != null) {
                    runOnUiThread(() -> {
                        chatList.add(new Chat(Chat.MESSAGE_TYPE_SENT, message1));
                        chatAdapter.notifyDataSetChanged();
                    });
                }
                return true;
            case WooEvents.EVENT_FAILURE_MESSAGE:
                Log.w(TAG, "Failed to send chat message " + msg.obj);

                return true;
            case WooEvents.EVENT_ME_MIC_TURNED_ON:
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
                    }
                }
                return true;
            case WooEvents.EVENT_ME_MIC_TURNED_OFF:
                runOnUiThread(() -> {
                    buttonMic.setImageResource(R.drawable.ic_mic_off_gray);
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
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call call, Object e) {

                    }
                });
                return true;
            case WooEvents.EVENT_ME_CAM_TURNED_ON:
                runOnUiThread(() -> {
                    buttonCam.setImageResource(R.drawable.ic_video_camera_white);
                });
                return true;
            case WooEvents.EVENT_ME_CAM_TURNED_OFF:
                runOnUiThread(() -> {
                    buttonCam.setImageResource(R.drawable.ic_camera_off_gray);
                });
                return true;
            case WooEvents.EVENT_ON_TEXT_TRANSLATION_RECV:
                if (msg.obj != null) {
                    try {
                        JSONObject obj = new JSONObject(String.valueOf(msg.obj));
                        String translation = obj.getString("translation");
                        String original = obj.getString("original");
                        String producerId = obj.getString("producerId");

                        Transcript transcript = new Transcript(translation, original, producerId);
                        transcriptList.add(transcript);
                        if (transcriptAdapter != null)
                            transcriptAdapter.replaceList(transcriptList);
                        if (mMeetingClient.isTextTranslationOn()) {
                            onTranslation(original, translation);
                        }
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
                return true;
            case WooEvents.EVENT_CLICKED_LANGUAGE_SELECT:
                showLanguageSelectionSheet();
                return true;
            case WooEvents.EVENT_NEW_PEER_JOINED:
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
            case WooEvents.EVENT_PEER_EXITED:
                if (msg.obj != null) {
                    runOnUiThread(() -> {
                        showCommonPopup(String.valueOf(msg.obj) + " left!", true, WooCommonPopup.VERTICAL_POSITION_TOP);
                        try {
                            notificationSound.play();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
                return true;
            case WooEvents.EVENT_ME_HAND_RAISED:
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
                return true;
            case WooEvents.EVENT_ME_HAND_LOWERED:
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
                return true;
            case WooEvents.EVENT_TRANSLATION_ENABLED:
                enableTranslations();
                return true;
            case WooEvents.EVENT_TRANSLATION_DISABLED:
                disableTranslation();
                return true;
            case WooEvents.EVENT_PEER_HAND_LOWERED:
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
            case WooEvents.EVENT_PEER_HAND_RAISED:
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
            case WooEvents.EVENT_SELECT_BACKGROUND:
                openImageChooser();
                return true;
            case WooEvents.EVENT_SHOW_MEMBERS:
                showMembersSheet();
                return true;
            case WooEvents.EVENT_NETWORK_CONNECTIVITY_CHANGED:
                if (msg.obj != null) {
                    Intent intent = (Intent) msg.obj;
                    if (intent != null) {
                        String action = intent.getAction();
                        Log.d(TAG, "<< Receiver Action >>>> " + action);
                    }
                }
                return true;
            default:
                return false;
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

    private void updatePageFragments() {
        for (MeetingPageFragment f : pageFragments) {
            f.notifyUpdate();
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        launcher.launch(intent);
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
                            onBackPressed();
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
     *
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
            for (MeetingPageFragment fragment : pageFragments) {
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
            tvTransOriginal.setBackgroundResource(android.R.color.transparent);;
            translationLayout.setVisibility(View.GONE);
            for (MeetingPageFragment fragment : pageFragments) {
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
     *
     * @param selectedPosition
     */
    private void updatePageControl(int selectedPosition) {
       if (pageFragments.size() > 1) {
           for (int i = 0; i < pageFragments.size(); i++) {
               if (pageControlLayout.getChildAt(i) != null) {
                   if (i == selectedPosition) {
                       if (pageControlLayout.getChildAt(i) != null) {
                           pageControlLayout.getChildAt(i).setLayoutParams(getSelectedPageControlParams());
                       }
                   } else {
                       pageControlLayout.getChildAt(i).setLayoutParams(getUnSelectedPageControlParams());
                   }
               }
           }
        }
    }

    /**
     *
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
            v.setAlpha(0.6f);
        }
        return v;
    }

    /**
     *
     * @return
     */
    private LinearLayout.LayoutParams getSelectedPageControlParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Display.dpToPx(this, 20), Display.dpToPx(this, 20));
        params.setMargins(3, 3, 3, 3);
        return params;
    }

    /**
     *
     * @return
     */
    private LinearLayout.LayoutParams getUnSelectedPageControlParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Display.dpToPx(this, 20), Display.dpToPx(this, 20));
        params.setMargins(1, 1, 1, 1);
        return params;
    }

    /**
     *
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
        WooEvents.getInstance().removeHandler(this.callbackHandler);
    }

    @Override
    protected void onDestroy() {
        destroyMeeting();
        chatList.clear();
        destroyCallbackHandler();
        if (droidAudioManager != null) {
            droidAudioManager.setSpeakerphoneOn(false);
            droidAudioManager.setMode(AudioManager.MODE_NORMAL);
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mSideMenuOpened) {
            closeDrawer();
        } else {
            new Handler().postDelayed(() -> {
                destroyMeeting();
                chatList.clear();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//                super.onBackPressed();
                finish();
            }, 1000);
        }
    }

} // end class
