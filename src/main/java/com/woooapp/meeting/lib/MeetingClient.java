package com.woooapp.meeting.lib;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import androidx.annotation.NonNull;

import com.woooapp.meeting.impl.utils.WooEvents;
import com.woooapp.meeting.lib.lv.RoomStore;
import com.woooapp.meeting.lib.socket.WooSocket;
import com.woooapp.meeting.net.models.CreateMeetingResponse;
import com.woooapp.meeting.net.models.Message;

import org.json.JSONException;
import org.mediasoup.droid.Consumer;
import org.mediasoup.droid.DataConsumer;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    private boolean camOn = true;
    private boolean everyoneCamOn = true;
    private boolean audioMuted = false;
    private boolean textTranslationOn = false;
    private boolean voiceTranslationOn = false;
    private String selectedLanguage = "English";
    private String selectedLanguageCode = "en";
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
            mSocket.connect();
            mStarted = true;
        });
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
            mSocket.disableAudioOnly();
        } else {
            mSocket.enableAudioOnly();
        }
        this.everyoneCamOn = everyoneCamOn;
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
    public void setAudioMuted(boolean mute) {
        if (mute && !this.audioMuted) {
            mSocket.muteAudio();
        } else {
            mSocket.unmuteAudio();
        }
        this.audioMuted = mute;
    }

    /**
     *
     * @param on
     */
    public void setTextTranslation(boolean on) {
        if (mSocket.isConnected()) {
            try {
                mSocket.emitTextTranslation(on);
                textTranslationOn = on;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void setVoiceTranslation(boolean on) {
        // TODO
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
        this.micOn = micOn;
    }

    public boolean isCamOn() {
        return camOn;
    }

    public void setCamOn(boolean camOn) {
        if (camOn && !this.camOn) {
            mSocket.disableCam();
            try {
                mSocket.emitVideoOpen();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            mSocket.enableCam();
            try {
                mSocket.emitVideoClose();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.camOn = camOn;
    }

    /**
     *
     * @param raised
     */
    public void setMeHandRaised(boolean raised) {
        mStore.getMe().postValue(me -> {
            me.setHandShaking(raised);
        });
        if (raised) {
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
    /**
     *
     * @return
     */
    public boolean isClosed() {
        return !mStarted;
    }

    public void close() {
        if (mStarted) {
            this.mWorkerHandler.post(() -> {
               mSocket.disconnect();
               mSocket = null;
               mStarted = false;
//               mWorkerHandler.getLooper().quit();
            });
        }
    }

} /** end class. */
