package com.woooapp.meeting.impl.views;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.woogroup.woooo_app.woooo.di.WooApplication;
import com.woooapp.meeting.device.Display;
import com.woooapp.meeting.impl.utils.WooDirector;
import com.woooapp.meeting.impl.utils.WooEvents;
import com.woooapp.meeting.impl.utils.WooMediaPlayer;
import com.woooapp.meeting.impl.views.adapters.LangSelectionAdapter;
import com.woooapp.meeting.impl.views.adapters.MeetingChatAdapter;
import com.woooapp.meeting.impl.views.adapters.MeetingPagerAdapter;
import com.woooapp.meeting.impl.views.adapters.MeetingPeersAdapter;
import com.woooapp.meeting.impl.views.adapters.MeetingTranscriptAdapter;
import com.woooapp.meeting.impl.views.adapters.PeerAdapter2;
import com.woooapp.meeting.impl.views.animations.WooAnimationUtil;
import com.woooapp.meeting.impl.views.fragments.MeetingPageFragment;
import com.woooapp.meeting.impl.views.models.Chat;
import com.woooapp.meeting.impl.views.models.GridPeer;
import com.woooapp.meeting.impl.views.models.Languages;
import com.woooapp.meeting.impl.views.models.ListGridPeer;
import com.woooapp.meeting.impl.views.models.MeetingPage;
import com.woooapp.meeting.impl.views.models.MeetingPage2;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import eu.siacs.conversations.R;
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
    private final Handler callbackHandler = new Handler(this);
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
            this.morePopup = new MeetingMorePopup(
                    this, this.mainContainer,
                    this.bottomBarMeeting.getHeight(),
                    mMeetingClient);
            morePopup.show();
        });

        this.buttonAI.setOnClickListener(view -> {
            View contentView = LayoutInflater.from(this).inflate(R.layout.layout_bottom_sheet_translation, null);
            Button buttonTextTrans = contentView.findViewById(R.id.buttonTransSheetText);
            Button buttonVoiceTrans = contentView.findViewById(R.id.buttonTransSheetVoice);
            Button buttonCancel = contentView.findViewById(R.id.buttonTransSheetCancel);

            BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.SheetDialog);
            dialog.setContentView(contentView);
            dialog.show();

            buttonTextTrans.setOnClickListener(v -> {
                if (mMeetingClient != null)
                    mMeetingClient.setTextTranslation(!mMeetingClient.isTextTranslationOn());
                dialog.dismiss();
            });

            buttonVoiceTrans.setOnClickListener(v -> {
                if (mMeetingClient != null) mMeetingClient.setVoiceTranslation(true);
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
        }
    }

    private void setup() {
//        pd.show();

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
                picture = "https://picsum.photos/200";
        } else {
            picture = "https://picsum.photos/200";
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
    }

    private void fetchRoomData() {
        ApiManager.build(this).fetchRoomData2(mMeetingId, new ApiManager.ApiResult2() {
            @Override
            public void onResult(Call call, Response response) {
                if (response != null) {
                    if (response.body() != null) {
                        try {
                            Log.d(TAG, "ROOM DATA RESPONSE >>> " + response.body().string());
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
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call call, Object error) {
                    Log.e(TAG, "Error on createMeeting()" + error.toString());
                    runOnUiThread(() -> {
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
                    });
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
                        new ApiManager.ApiResult2() {
                            @Override
                            public void onResult(Call call, Response response) {
                                if (response != null) {
                                    try {
                                        String resp = response.body().string();
                                        Log.d(TAG, "RESPONSE PUT Member >>> " + resp);
                                        putMembersDataResponse = PutMembersDataResponse.fromJson(resp);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call call, Object e) {
                                Log.e(TAG, "Error while calling [" + call.request().url() + "] -> " + e.toString());
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

        initAccount();

        getViewModelStore().clear();
        initViewModel();
    }

    private void initViewModel() {
        EdiasProps.Factory factory = new EdiasProps.Factory(WooApplication.Companion.getSharedInstance(), mRoomStore);

        RoomProps roomProps = new ViewModelProvider(this, factory).get(RoomProps.class);
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

        final MeetingPageFragment f1 = new MeetingPageFragment(1, this, mRoomStore, mMeetingClient, this);
        pageFragments.add(f1);
        final MeetingPageFragment f2 = new MeetingPageFragment(2, this, mRoomStore, mMeetingClient, this);
        final MeetingPageFragment f3 = new MeetingPageFragment(3, this, mRoomStore, mMeetingClient, this);

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
                            } else if (peersList.size() > 5 && peersList.size() <= 12) {
                                List<ListGridPeer> pl2 = createListGridPeers(peersList.subList(0, 2), false);
                                List<ListGridPeer> pl3 = createListGridPeers(peersList.subList(3, listGridPeer.size() - 1), false);
                                MeetingPage page1 = new MeetingPage(1, pl2);
                                MeetingPage page2 = new MeetingPage(2, pl3);
                                pageList.add(page1);
                                pageList.add(page2);
                                pageFragments.get(0).replacePage(page1);
                                if (pageFragments.get(1) != null) {
                                    f2.replacePage(page2);
                                    pageFragments.add(f2);
                                } else {
                                    pageFragments.get(1).replacePage(page2);
                                }
                            } else if (peersList.size() > 12 && peersList.size() <= 18) {
                                List<ListGridPeer> pl4 = createListGridPeers(peersList.subList(0, 2), false);
                                List<ListGridPeer> pl5 = createListGridPeers(peersList.subList(3, 5), false);
                                List<ListGridPeer> pl6 = createListGridPeers(peersList.subList(6, listGridPeer.size() - 1), false);
                                MeetingPage page1 = new MeetingPage(1, pl4);
                                MeetingPage page2 = new MeetingPage(2, pl5);
                                MeetingPage page3 = new MeetingPage(3, pl6);
                                pageList.add(page1);
                                pageList.add(page2);
                                pageList.add(page3);
                                pageFragments.get(0).replacePage(page1);
                                pageFragments.get(1).replacePage(page2);
                                if (pageFragments.get(2) != null) {
                                    f3.replacePage(page3);
                                    pageFragments.add(f3);
                                } else {
                                    pageFragments.get(2).replacePage(page3);
                                }
                            }
                            Log.d(TAG, "Peer list size[" + peersList.size() + "]");
                            Log.d(TAG, "Pages Size >>> " + pageList.size());
                            peersPagerAdapter.replaceFragments(pageFragments, pageList);

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
            peers.add(new ListGridPeer(ListGridPeer.VIEW_TYPE_ME_PEER, null, null));
            for (int i = 0; i < peersList.size(); i++) {
                Peer p = peersList.get(i);
                if (i == 0 || i == 3) {
                    peers.add(new ListGridPeer(ListGridPeer.VIEW_TYPE_PEER_PEER, p, null));
                    Log.d(TAG, "<< Added a new peer at index " + i + " >>");
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
    public boolean handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case WooEvents.EVENT_TYPE_SOCKET_ID:
                Log.d(TAG, "<< Handler Event SOCKET_ID Received >> " + msg.obj);
                // Drawer
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                });
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
            case WooEvents.EVENT_MIC_TURNED_ON:
                runOnUiThread(() -> {
                    buttonMic.setImageResource(R.drawable.baseline_mic_34);
                });
                return true;
            case WooEvents.EVENT_MIC_TURNED_OFF:
                runOnUiThread(() -> {
                    buttonMic.setImageResource(R.drawable.ic_mic_off_gray);
                });
                return true;
            case WooEvents.EVENT_CAM_TURNED_ON:
                runOnUiThread(() -> {
                    buttonCam.setImageResource(R.drawable.ic_video_camera_white);
                });
                return true;
            case WooEvents.EVENT_CAM_TURNED_OFF:
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
                        showCommonPopup(msg.obj + " joined", true, WooCommonPopup.VERTICAL_POSITION_TOP);
                        try {
                            notificationSound.play();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
            default:
                return false;
        }
    }

    /**
     *
     * @param message
     * @param autoDismiss
     * @param verticalAlignment
     */
    private void showCommonPopup(@NonNull String message, boolean autoDismiss, int verticalAlignment) {
        WooCommonPopup popup = new WooCommonPopup(this, this.mainContainer, message, autoDismiss, verticalAlignment);
        popup.show();
    }

    @Override
    protected void onDestroy() {
        destroyMeeting();
        chatList.clear();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mSideMenuOpened) {
            closeDrawer();
        } else {
            new Handler().postDelayed(() -> {
                WooEvents.getInstance().removeHandler(callbackHandler);
                destroyMeeting();
                chatList.clear();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                super.onBackPressed();
                finish();
            }, 1000);
        }
    }

} // end class
