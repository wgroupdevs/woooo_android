package com.woooapp.meeting.lib;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import com.woooapp.meeting.lib.lv.RoomStore;
import com.woooapp.meeting.lib.socket.WebSocketTransport;

import org.json.JSONObject;
import org.mediasoup.droid.DataProducer;
import org.mediasoup.droid.Device;
import org.mediasoup.droid.Producer;
import org.mediasoup.droid.RecvTransport;
import org.mediasoup.droid.SendTransport;
import org.protoojs.droid.Message;
import org.protoojs.droid.Peer;
import org.protoojs.droid.ProtooException;
import org.webrtc.AudioTrack;
import org.webrtc.VideoTrack;

import java.util.Locale;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @author Muneeb Ahmad (ahmadgallian@yahoo.com)
 * <p>
 * Class RoomClient2.java
 * Created on 15/09/2023 at 1:44 am
 */
public class RoomClient2  extends RoomMessageHandler {

    private static final String TAG = RoomClient2.class.getName().toUpperCase(Locale.ROOT);

    public enum ConnectionStore {
        // Initial State
        NEW,
        // connecting or reconnecting
        CONNECTING,
        // connected
        CONNECTED,
        // mClosed
        CLOSED
    }

    private volatile boolean mClosed;
    private final Context mContext;

    private PeerConnectionUtils mPeerConnectionUtils;

    @NonNull private final RoomOptions mOptions;

    private String mDisplayName;
    private String mProtooUrl;
    private Protoo mProtoo;
    private Device mMediasoupDevice;
    private SendTransport mSendTransport;
    private RecvTransport mRecvTransport;
    // Local Audio Track for mic.
    private AudioTrack mLocalAudioTrack;
    // Local mic mediasoup Producer.
    private Producer mMicProducer;
    // local Video Track for cam.
    private VideoTrack mLocalVideoTrack;
    // Local cam mediasoup Producer.
    private Producer mCamProducer;
    // TODO Local share mediasoup Producer.
    private Producer mShareProducer;
    // Local chat DataProducer.
    private DataProducer mChatDataProducer;
    // Local bot DataProducer.
    private DataProducer mBotDataProducer;
    // jobs worker handler.
    private Handler mWorkHandler;
    // main looper handler.
    private Handler mMainHandler;
    // Disposable Composite. used to cancel running
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    // Share preferences
    private SharedPreferences mPreferences;

    private String mSocketId;

    private Protoo.Listener peerListener = new Protoo.Listener() {
        @Override
        public void onOpen() {

        }

        @Override
        public void onFail() {

        }

        @Override
        public void onRequest(@NonNull Message.Request request, @NonNull Peer.ServerRequestHandler handler) {

        }

        @Override
        public void onNotification(@NonNull Message.Notification notification) {

        }

        @Override
        public void onDisconnected() {

        }

        @Override
        public void onClose() {

        }
    };

    public RoomClient2(
            Context context, RoomStore roomStore, String roomId, String peerId, String displayName) {
        this(context, roomStore, roomId, peerId, displayName, false, false, null);
    }

    /**
     *
     * @param context
     * @param roomStore
     * @param roomId
     * @param peerId
     * @param displayName
     * @param options
     */
    public RoomClient2(
            Context context,
            RoomStore roomStore,
            String roomId,
            String peerId,
            String displayName,
            RoomOptions options) {
        this(context, roomStore, roomId, peerId, displayName, false, false, options);
    }

    /**
     *
     * @param context
     * @param roomStore
     * @param roomId
     * @param peerId
     * @param displayName
     * @param forceH264
     * @param forceVP9
     * @param options
     */
    public RoomClient2(
            Context context,
            RoomStore roomStore,
            String roomId,
            String peerId,
            String displayName,
            boolean forceH264,
            boolean forceVP9,
            RoomOptions options) {
        super(roomStore);

        this.mContext = context.getApplicationContext();
        this.mOptions = options == null ? new RoomOptions() : options;
        this.mDisplayName = displayName;
        this.mClosed = false;
        this.mProtooUrl = UrlFactory2.getProtooUrl();

        this.mStore.setMe(peerId, displayName, this.mOptions.getDevice());
//        this.mStore.setRoomUrl(roomId, UrlFactory2.getInvitationLink());

        // init worker handler
        HandlerThread handlerThread = new HandlerThread("woo_worker");
        handlerThread.start();
        mWorkHandler = new Handler(handlerThread.getLooper());
        mMainHandler = new Handler(Looper.getMainLooper());
        mWorkHandler.post(() -> mPeerConnectionUtils = new PeerConnectionUtils());
    }

    @Async
    public void connect() {
        Log.d(TAG, "connect() " + mProtooUrl);
        mStore.setRoomState(RoomClient.ConnectionState.CONNECTING);
        mWorkHandler.post(() -> {
            WebSocketTransport transport =  new WebSocketTransport(mProtooUrl);
            mProtoo = new Protoo(transport, peerListener);
        });
    }

} /** end class. */
