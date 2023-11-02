package com.woooapp.meeting.lib;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.woooapp.meeting.impl.utils.WooEvents;
import com.woooapp.meeting.impl.views.UIManager;
import com.woooapp.meeting.lib.lv.RoomStore;
import com.woooapp.meeting.lib.socket.WooSocket;
import com.woooapp.meeting.net.models.Message;

import org.json.JSONException;

import java.util.Map;

import eu.siacs.conversations.R;

/**
 * @author Muneeb Ahmad (ahmadgallian@yahoo.com)
 * <p>
 * Class MeetingClient.java
 * Created on 16/09/2023 at 1:21 am
 */
public final class MeetingClient extends RoomMessageHandler {
    private static final String TAG = MeetingClient.class.getSimpleName() + ".java";
    private final Context mContext;
    private final Handler mWorkerHandler;
    private WooSocket mSocket;
    private boolean mStarted = false;
    private final RoomStore mRoomStore;
    private final String mMeetingId;
    private String username;
    private String email;
    private String accountUniqueId;
    private String picture;
    private boolean micOn = true;
    private boolean camOn = false;
    private boolean everyoneCamOn = true;
    private boolean audioMuted = false;
    private boolean textTranslationOn = false;
    private boolean voiceTranslationOn = false;
    private String selectedLanguage = "English";
    private String selectedLanguageCode = "en";
    private boolean passwordSet = false;
    private Role role = Role.USER;

    public enum ConnectionState {
        // initial state.
        NEW,
        // connecting or reconnecting.
        CONNECTING,
        // connected.
        CONNECTED,
        // mClosed.
        CLOSED,
    }

    public enum Role {
        USER,
        ADMIN
    }

    /**
     *
     * @param context
     * @param roomStore
     * @param meetingId
     */
    public MeetingClient(
            @NonNull Context context,
            @NonNull RoomStore roomStore,
            @NonNull String meetingId) {
        super(roomStore);
        this.mContext = context;
        this.mMeetingId = meetingId;

        this.mRoomStore = roomStore;

        // Worker Handler
        HandlerThread handlerThread = new HandlerThread("WooWorker");
        handlerThread.start();
        this.mWorkerHandler = new Handler(handlerThread.getLooper());
        Log.d(TAG, "MeetingClient Initialized ...");
    }

    /**
     *
     * If permissions in activity are granted, call this to start the socket
     * for meeting.
     */
    public void start() {
        this.mWorkerHandler.post(() -> {
            mSocket = WooSocket.create(
                    mContext,
                    mRoomStore,
                    mWorkerHandler,
                    this);
            try {
                mSocket.connect();
                mStarted = true;
            } catch (Exception ex) {
                ex.printStackTrace();
                UIManager.showErrorDialog(
                        mContext,
                        "Communication Error",
                        "App encountered and internal error. Please try again later!",
                        "Okay",
                        R.drawable.ic_warning,
                        new UIManager.DialogCallback() {

                            @Override
                            public void onPositiveButton(@Nullable Object sender, @Nullable Object data) {
                                ((Activity) mContext).finish();
                            }

                            @Override
                            public void onNeutralButton(@Nullable Object sender, @Nullable Object data) {

                            }

                            @Override
                            public void onNegativeButton(@Nullable Object sender, @Nullable Object data) {

                            }
                        });
            }
        });
    }

    /**
     *
     * @return
     */
    @Nullable
    public RoomStore getRoomStore() {
        return this.mRoomStore;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSocketId() {
        return mSocket.getSocketId();
    }

    public String getMeetingId() {
        return this.mMeetingId;
    }

    @NonNull
    public Map<String, ConsumerHolder> getConsumers() {
        return mConsumers;
    }

    /**
     *
     * @param peerId
     */
    public void removeVideoConsumer(@NonNull String peerId) {
        if (mSocket != null) {
            mSocket.removeVideoConsumer(peerId);
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountUniqueId() {
        return accountUniqueId;
    }

    public void setAccountUniqueId(String accountUniqueId) {
        this.accountUniqueId = accountUniqueId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getSelectedLanguage() {
        return selectedLanguage;
    }

    public void setSelectedLanguage(String selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
    }

    public String getSelectedLanguageCode() {
        return selectedLanguageCode;
    }

    public void setSelectedLanguageCode(String selectedLanguageCode) {
        this.selectedLanguageCode = selectedLanguageCode;
    }

    public boolean isEveryoneCamOn() {
        return everyoneCamOn;
    }

    public void setEveryoneCamOn(boolean everyoneCamOn) {
        if (everyoneCamOn && !this.everyoneCamOn) {
//            mSocket.disableAudioOnly();
        } else {
//            mSocket.enableAudioOnly();
        }
        this.everyoneCamOn = everyoneCamOn;
    }

    /**
     *
     * @param pId
     */
    public void pausePeerCam(@NonNull String pId) {
        if (mSocket != null) {
//            mSocket.pauseVideo(pId);
        }
    }

    public void updateTranslationLanguage(@NonNull String langCode) {
        try {
            mSocket.emitUpdateLanguage(langCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isAudioMuted() {
        return audioMuted;
    }

    /**
     *
     * @param mute
     */
    public void setEveryonesAudioMuted(boolean mute) {
        if (mute) {
            try {
                mSocket.emitMuteEveryone();
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            mSocket.muteAudio();
        } else {

//            mSocket.unmuteAudio();
        }
//        this.audioMuted = mute;
    }

    public void muteEveryoneLocally() {
        if (mSocket != null) {
            try {
                mSocket.muteAudio();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void unMuteEveryoneLocally() {
        if (mSocket != null) {
            try {
                mSocket.unmuteAudio();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     *
     * @param on
     */
    public void setTextTranslation(boolean on) {
        if (mSocket.isConnected()) {
            try {
                mSocket.emitTextTranslation(on, false);
                textTranslationOn = on;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param on
     */
    public void setVoiceTranslation(boolean on) {
        if (mSocket.isConnected()) {
            try {
                mSocket.emitTextTranslation(on, true);
                voiceTranslationOn = on;
                if (on) {
                    muteEveryoneLocally();
                } else {
                    unMuteEveryoneLocally();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @return
     */
    public boolean isTextTranslationOn() {
        return textTranslationOn;
    }

    /**
     *
     * @return
     */
    public boolean isVoiceTranslationOn() {
        return voiceTranslationOn;
    }

    public boolean isMicOn() {
        return micOn;
    }

    public void setMicOn(boolean micOn) {
        if (mSocket != null) {
            if (micOn && !this.micOn) {
                mSocket.unmuteMic();
                try {
                    mSocket.emitMicOpen();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                mSocket.muteMic();
                try {
                    mSocket.emitMicClosed();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        this.micOn = micOn;
    }

    public boolean isCamOn() {
        return camOn;
    }

    public void setCamOn(boolean camOn) {
        if (mSocket != null) {
            if (!camOn && this.camOn) {
                mSocket.disableCam();
                try {
                    mSocket.emitVideoClose();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                mSocket.enableCam();
                try {
                    mSocket.emitVideoOpen();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        this.camOn = camOn;
    }

    /**
     *
     * @param raised
     */
    public void setMeHandRaised(boolean raised) {
        if (!raised) {
            WooEvents.getInstance().notify(WooEvents.EVENT_ME_HAND_RAISED, true);
        } else {
            WooEvents.getInstance().notify(WooEvents.EVENT_ME_HAND_LOWERED, true);
        }
    }

    public void emitHandRaisedState(boolean raised) throws JSONException {
        if (raised) {
            mSocket.emitHandRaised();
        } else {
            mSocket.emitHandLowered();
        }
    }

    /**
     * Send chat message
     *
     * @param message {@link Message}
     */
    public void sendMessage(@NonNull final Message message) {
        if (mSocket != null) {
            if (mSocket.isConnected()) {
                mSocket.emitSendMessage(message);
            }
        }
    }

    public boolean isPasswordSet() {
        return passwordSet;
    }

    public void setPasswordSet(boolean passwordSet) {
        this.passwordSet = passwordSet;
    }

    /**
     *
     * @return
     */
    public boolean isClosed() {
        return !mStarted;
    }

    public void close() {
        if (mStarted) {
            try {
                this.mWorkerHandler.post(() -> {
                    try {
                        mSocket.disconnect();
                        mSocket = null;
                        mStarted = false;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
//                mWorkerHandler.getLooper().quit();
            }
        }
    }

} /** end class. */
