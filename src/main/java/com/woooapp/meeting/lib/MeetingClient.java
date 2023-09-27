package com.woooapp.meeting.lib;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

import androidx.annotation.NonNull;

import com.woooapp.meeting.lib.lv.RoomStore;
import com.woooapp.meeting.lib.socket.WooSocket;
import com.woooapp.meeting.net.models.CreateMeetingResponse;

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
public class MeetingClient {
    private static final String TAG = MeetingClient.class.getCanonicalName().toUpperCase(Locale.ROOT);
    private final Context mContext;
    private final Handler mWorkerHandler;
    private WooSocket mSocket;
    private boolean mStarted = false;
    private final RoomStore mRoomStore;
    @NonNull final Map<String, ConsumerHolder> mConsumers;
    @NonNull final Map<String, DataConsumerHolder> mDataConsumers;
    private final String mMeetingId;
    private String username;
    private String email;
    private String accountUniqueId;
    private String picture;
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
        this.mContext = context;
        this.mMeetingId = meetingId;

        this.mConsumers = new ConcurrentHashMap<>();
        this.mDataConsumers = new ConcurrentHashMap<>();

        this.mRoomStore = roomStore;

        // Worker Handler
        HandlerThread handlerThread = new HandlerThread("WooWorker");
        handlerThread.start();
        this.mWorkerHandler = new Handler(handlerThread.getLooper());
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

    public static class ConsumerHolder {

        @NonNull final String peerId;
        @NonNull final Consumer mConsumer;

        /**
         *
         * @param peerId
         * @param consumer
         */
        public ConsumerHolder(@NonNull String peerId, @NonNull Consumer consumer) {
            this.peerId = peerId;
            this.mConsumer = consumer;
        }
    } // end class

    static class DataConsumerHolder {
        @NonNull final String peerId;
        @NonNull final DataConsumer mDataConsumer;

        /**
         *
         * @param peerId
         * @param dataConsumer
         */
        public DataConsumerHolder(@NonNull String peerId, @NonNull DataConsumer dataConsumer) {
            this.peerId = peerId;
            this.mDataConsumer = dataConsumer;
        }

    } // end class

} /** end class. */
