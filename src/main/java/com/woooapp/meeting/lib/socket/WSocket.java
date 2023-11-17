package com.woooapp.meeting.lib.socket;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import com.woooapp.meeting.impl.utils.WDirector;
import com.woooapp.meeting.impl.utils.WEvents;
import com.woooapp.meeting.lib.Async;
import com.woooapp.meeting.lib.MeetingClient;
import com.woooapp.meeting.lib.PeerConnectionUtils;
import com.woooapp.meeting.lib.RoomClient;
import com.woooapp.meeting.lib.RoomMessageHandler;
import com.woooapp.meeting.lib.lv.RoomStore;
import com.woooapp.meeting.lib.lv.SupplierMutableLiveData;
import com.woooapp.meeting.lib.model.ConsumerData;
import com.woooapp.meeting.lib.model.Consumers;
import com.woooapp.meeting.lib.model.Peer;
import com.woooapp.meeting.lib.model.StorageIdsData;
import com.woooapp.meeting.net.ApiManager;
import com.woooapp.meeting.net.models.Message;
import com.woooapp.meeting.net.models.RoomData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mediasoup.droid.Consumer;
import org.mediasoup.droid.Device;
import org.mediasoup.droid.Logger;
import org.mediasoup.droid.MediasoupException;
import org.mediasoup.droid.Producer;
import org.mediasoup.droid.RecvTransport;
import org.mediasoup.droid.SendTransport;
import org.mediasoup.droid.Transport;
import org.webrtc.AudioTrack;
import org.webrtc.CameraVideoCapturer;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoTrack;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.disposables.CompositeDisposable;
import io.socket.client.IO;
import io.socket.client.Socket;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author Muneeb Ahmad (ahmadgallian@yahoo.com)
 * <p>
 * Class WooSocket.java
 * Created on 15/09/2023 at 4:43 am
 */
@WorkerThread
public class WSocket {
    private static final String TAG = WSocket.class.getSimpleName();
    private static final String SOCKET_URL = "https://wmediasoup.watchblock.net/";
    private static WSocket sInstance = null;
    private final Context mContext;
    private final RoomStore mStore;
    private Socket mSocket;
    private String mSocketId;
    private boolean mConnected = false;
    private Device mMediaSoupDevice;
    private SendTransport mSendTransport;
    //    private RecvTransport mRecvTransport;
    private final Map<String, RecvTransport> mRecvTransports = new HashMap<>();
    //    private final Map<String, String> mStorageSockIdsMap = new LinkedHashMap<>();
    private final List<StorageIdsData> storageIdsList = new LinkedList<>();
    private final Set<String> consumerStorageIds = new HashSet<>();
    private AudioTrack mLocalAudioTrack;
    private VideoTrack mLocalVideoTrack;
    private VideoTrack mScreenVideoTrack;
    private Producer mMicProducer;
    private Producer mCamProducer;
    private Producer mScreenProducer;
    private final PeerConnectionUtils mPeerConnectionUtils;
    private final Handler mMainHandler;
    private final Handler mWorkHandler;
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private final String deviceUUID;
    private String consumeBackDeviceUUID;
    private List<String> audioProducersIds = new ArrayList<>();
    private final Set<String> producerIds = new HashSet<>();
    private final Map<String, String> producerIdMap = new HashMap<>();
    private String videoProducerId = null;
    private final MeetingClient mMeetingClient;
    private final Map<String, String> producerSockIds = new LinkedHashMap<>();
    private final Set<String> consumeBackDeviceUUIDs = new HashSet<>();
    private boolean destroyed = false;
    private RoomData mRoomData;
    private boolean mMicEnabled = false;
    private boolean mCamEnabled = false;
    private boolean isPresenting = false;
    private boolean produceScreen = false;
    private boolean consumeScreen = false;
    private Consumer screenConsumer;
    private boolean destroying = false;
    private final List<ConsumerData> mConsumerData = new LinkedList<>();

    /**
     * @param context
     * @param roomStore
     * @param workHandler
     * @param meetingClient
     */
    private WSocket(
            @NonNull Context context,
            @NonNull RoomStore roomStore,
            @NonNull Handler workHandler,
            @NonNull MeetingClient meetingClient) {
        this.mContext = context;
        this.mStore = roomStore;
        this.mMeetingClient = meetingClient;
        this.mPeerConnectionUtils = new PeerConnectionUtils();

        // Thread Handlers
        this.mMainHandler = new Handler(Looper.getMainLooper());
        this.mWorkHandler = workHandler;

        // Created once per session
        this.deviceUUID = WDirector.getInstance().getDeviceUUID();

        // Clean up first
        this.audioProducersIds.clear();
        this.producerSockIds.clear();
        this.consumeBackDeviceUUIDs.clear();
        consumerStorageIds.clear();
        producerIdMap.clear();
        this.mConsumerData.clear();

        mStore.setRoomState(RoomClient.ConnectionState.CONNECTING);

        mMainHandler.post(() -> {
            Logger.setLogLevel(Logger.LogLevel.LOG_DEBUG);
            Logger.setDefaultHandler();
        });
    }

    public void connect() {
        mStore.setRoomState(RoomClient.ConnectionState.CONNECTING);
        try {
            mSocket = IO.socket(SOCKET_URL);
            mSocket.connect();
            Log.d(TAG, "Connected to Socket @" + SOCKET_URL);
            listenSocketEvents();
        } catch (URISyntaxException e) {
            Log.e(TAG, "Socket connection to " + SOCKET_URL + " failed");
            e.printStackTrace();
        }
    }

    @WorkerThread
    public void disconnect() {
        // Say Bye
//        mStore.setRoomState(RoomClient.ConnectionState.CLOSED);
        emitClose();
        if (mSocket != null && mSocket.isActive()) {
            destroying = true;
//            if (disposeTransport()) {
//                mWorkHandler.postDelayed(() -> {
            this.disposeCamMic();

            // Remove the peer
            for (Map.Entry<String, String> entry : producerSockIds.entrySet()) {
                mStore.removePeer(entry.getValue());
            }

            try {
                disposeTransport();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (mMediaSoupDevice != null) {
                try {
                    mMediaSoupDevice.dispose();
                    mMediaSoupDevice = null;
                    Log.d(TAG, "Disposed Mediasoup device");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            // dispose audio track.
            if (mLocalAudioTrack != null) {
                try {
                    mLocalAudioTrack.setEnabled(false);
                    mLocalAudioTrack.dispose();
                    mLocalAudioTrack = null;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            // dispose video track.
            try {
                if (mLocalVideoTrack != null) {
                    mLocalVideoTrack.setEnabled(false);
                    mLocalVideoTrack.dispose();
                    mLocalVideoTrack = null;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                this.mPeerConnectionUtils.dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            this.audioProducersIds.clear();
            this.videoProducerId = null;

//            mStorageSockIdsMap.clear();
            storageIdsList.clear();
            producerSockIds.clear();
            consumerStorageIds.clear();
            consumeBackDeviceUUIDs.clear();
            producerIdMap.clear();
            mConsumerData.clear();

            if (mRecvTransports.size() > 0) {
                mRecvTransports.clear();
            }

            destroyed = true;
            sInstance = null;

            mSocket.disconnect();
            mSocketId = null;
            mConnected = false;
            Log.d(TAG, "<< Socket Disconnected! >>");
            WEvents.getInstance().notify(WEvents.EVENT_TYPE_SOCKET_DISCONNECTED, mSocketId);

            mMainHandler.postDelayed(() -> {
                Logger.freeHandler();
                Log.d(TAG, "WebRTC Logger released ....");
            }, 3000);

            // Deliberate call to GC
            Runtime.getRuntime().gc();
        }
    }

    private boolean disposeCamMic() {
        // Disabling mic and cam
        try {
            mMainHandler.post(this::disableMic);
            mMainHandler.post(this::disableCam);
            Log.d(TAG, "Mic & Camera Released ... >>");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }

    private void disposeTransport() {
        removeConsumers();
        if (mSendTransport != null) {
            try {
                mSendTransport.close();
                mSendTransport.dispose();
                mSendTransport = null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (mRecvTransports.size() > 0) {
            for (Map.Entry<String, RecvTransport> entry : mRecvTransports.entrySet()) {
                if (entry.getValue() != null) {
                    if (!entry.getValue().isClosed()) {
                        entry.getValue().close();
                        entry.getValue().dispose();
                        Log.d(TAG, "<< Disposed Recv Transport with storageId, " + entry.getKey());
                    }
                }
            }
        }
    }

    private void removeConsumers() {
        try {
            for (MeetingClient.ConsumerHolder holder : mMeetingClient.getConsumers().values()) {
                mStore.removeConsumer(holder.peerId, holder.mConsumer.getId());
            }
            mConsumerData.clear();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @return
     */
    @Nullable
    public Socket getSocket() {
        return this.mSocket;
    }

    /**
     * @return
     */
    @Nullable
    public String getSocketId() {
        return mSocketId;
    }

    /**
     * @return
     */
    public boolean isConnected() {
        return this.mConnected;
    }

    /**
     * @param storageId
     * @param producerSockId
     */
    private void addToStorageIds(@NonNull String storageId, @NonNull String producerSockId) {
        for (int i = 0; i < storageIdsList.size(); i++) {
            StorageIdsData data = storageIdsList.get(i);
            if (data != null) {
                if (data.getStorageId().equals(storageId)) {
                    Log.d(TAG, "<< Entry with Storage ID >>> " + storageId + " already exist. Skipping this one ...");
                    return;
                }
            }
        }
        storageIdsList.add(new StorageIdsData(producerSockId, storageId));
    }

    /**
     * @param storageId
     * @return
     */
    @Nullable
    private String getProducerSockId(@NonNull String storageId) {
        for (StorageIdsData data : storageIdsList) {
            if (data.getStorageId().equals(storageId)) {
                return data.getProducerSockId();
            }
        }
        return null;
    }

    /**
     * @param producerSockId
     */
    public void removeStorageId(@NonNull String producerSockId) {
        for (int i = 0; i < storageIdsList.size(); i++) {
            StorageIdsData data = storageIdsList.get(i);
            if (data != null) {
                if (data.getProducerSockId().equals(producerSockId)) {
                    storageIdsList.remove(i);
                    break;
                }
            }
        }
    }

    /**
     * @param producerSockId
     * @return
     */
    public List<String> getStorageId(@NonNull String producerSockId) {
        List<String> list = new LinkedList<>();
        for (StorageIdsData data : storageIdsList) {
            if (data.getProducerSockId().equals(producerSockId)) {
                list.add(data.getStorageId());
            }
        }
        return list;
    }

    public boolean isPresenting() {
        return this.isPresenting;
    }

    public void produceScreen() {
        this.produceScreen = true;
    }

    @Nullable
    public Consumer getScreenConsumer() {
        return this.screenConsumer;
    }

    @Nullable
    public void disposeScreenPeer() {
        if (screenConsumer != null && mStore.getScreenPeer() != null) {
            try {
                mStore.getScreenPeer().getConsumers().remove(screenConsumer.getId());
                screenConsumer.getTrack().setEnabled(false);
                screenConsumer.getTrack().dispose();
                screenConsumer.close();
                screenConsumer = null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void listenSocketEvents() {
        // Test event
        this.mSocket.on("testdata", args -> {
            if (args != null) {
                Log.d(TAG, "<-- TEST DATA >> " + Arrays.toString(args));
            }
        });

        this.mSocket.on("connect", args -> {
            Log.d(TAG, "<-- Event connect -->");
            mConnected = true;
        });

        this.mSocket.on("myData", args -> {
            if (args[0] != null) {
                mSocketId = args[0].toString();
                Log.d(TAG, "SOCKET ID >>> " + mSocketId);
                try {
                    mMainHandler.post(() -> mStore.setMe(mSocketId, mMeetingClient.getUsername(), null));
                    emitRTPCaps();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mStore.setRoomState(RoomClient.ConnectionState.CONNECTED);
                WEvents.getInstance().notify(WEvents.EVENT_TYPE_SOCKET_ID, mSocketId);
            }
        });

        this.mSocket.on("RTPCaps", args -> {
            if (args[0] != null) {
                String jsonRTPCaps = String.valueOf(args[0]);
                Log.d(TAG, "<< EVENT RTPCaps >>> " + jsonRTPCaps);
                try {
                    JSONObject obj = new JSONObject(jsonRTPCaps);
                    JSONObject obj2 = obj.getJSONObject("capabilities");
                    JSONArray codecs = obj2.getJSONArray("codecs");
                    JSONArray headerExtensions = obj2.getJSONArray("headerExtensions");
                    JSONObject result = new JSONObject();
                    result.put("codecs", codecs);
                    result.put("headerExtensions", headerExtensions);
                    String jsonStr = result.toString();
                    Log.d(TAG, "RTPCaps Converted >>> " + jsonStr);

                    // Create Device
                    createMediaSoupDevice(jsonStr);

                    // Emit Create Transport
                    emitCreateTransport();
                } catch (JSONException | MediasoupException e) {
                    e.printStackTrace();
                }
            }
        });

        this.mSocket.on("transportCreated", args -> {
            if (args[0] != null) {
                String jsonStr = String.valueOf(args[0]);
                Log.d(TAG, "EVENT TRANSPORT CREATED >>> " + jsonStr);
                try {
                    JSONObject obj = new JSONObject(jsonStr);
                    JSONObject data = obj.getJSONObject("data");
                    String id = data.getString("id");
                    JSONObject iceParams = data.getJSONObject("iceParameters");
                    JSONArray iceCandidates = data.getJSONArray("iceCandidates");
                    JSONObject dtlsParameters = data.getJSONObject("dtlsParameters");

                    Log.d(TAG, "<<< Creating Send Transport (TX) >>> ");
                    Log.d(TAG, "iceParams >>> " + iceParams);
                    Log.d(TAG, "iceCandidates >>> " + iceCandidates);
                    Log.d(TAG, "dtlsParams >>> " + dtlsParameters);

                    Log.d(TAG, "device#createSendTransport()");
                    this.mSendTransport = mMediaSoupDevice.createSendTransport(
                            sendListener,
                            id,
                            iceParams.toString(),
                            iceCandidates.toString(),
                            dtlsParameters.toString());

                    createProducers();
                } catch (JSONException | MediasoupException e) {
                    Log.e(TAG, e.toString());
                    e.printStackTrace();
                }
            }
        });

        // ----> CONSUMER EVENTS
        mSocket.on("consumeProducer", args -> {
            if (args[0] != null) {
                Log.d(TAG, "CONSUME PRODUCER >> " + args[0]);
                try {
                    JSONObject obj = new JSONObject(String.valueOf(args[0]));
                    String producerId = obj.getString("producerId");
                    String producerSockId = obj.getString("producerSockId");
                    consumeScreen = obj.getBoolean("screenShare");
                    producerSockIds.put(producerId, producerSockId);
                    emitCreateConsumeTransport(producerId, producerSockId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mSocket.on("consumeTransportCreated", args -> {
            Log.d(TAG, "<<< Event CONSUME TRANSPORT CREATED >>>");
            if (args != null) {
                Log.d(TAG, String.valueOf(args[0]));

                // Create RecvTransport.
                try {
                    JSONObject obj = new JSONObject(String.valueOf(args[0]));
                    JSONObject data = obj.getJSONObject("data");
                    String id = data.getString("id");
                    JSONObject iceParameters = data.getJSONObject("iceParameters");
                    JSONArray iceCandidates = data.getJSONArray("iceCandidates");
                    JSONObject dtlsParameters = data.getJSONObject("dtlsParameters");
                    String producerId = obj.getString("producerId");
                    final String storageId = obj.getString("storageId");
                    final String producerSockid = obj.getString("producerSockId");

//                    if (mRecvTransports.containsKey(storageId)) {
//                        RecvTransport rT = mRecvTransports.get(storageId);
//                        if (rT != null) {
//                            try {
//                                if (!rT.isClosed()) {
//                                    rT.close();
//                                    rT.dispose();
//                                }
//                                mRecvTransports.remove(storageId);
//                                Log.d(TAG, "<< Disposed and Removed Recv. Transport with storageId >>> " + storageId);
//                            } catch (Exception ex) {
//                                ex.printStackTrace();
//                            }
//                        }
//                    }

                    Log.d(TAG, "Consumer ICE Parameters >>> " + iceParameters);
                    Log.d(TAG, "Consumer ICE Candidates >>> " + iceCandidates.toString());

                    mRecvTransports.put(storageId, mMediaSoupDevice.createRecvTransport(
                            new RecvTransport.Listener() {
                                @Override
                                public void onConnect(Transport transport, String dtlsParameters) {
                                    Log.d(TAG, "<<< RecvTransport.Listener#onConnect() >>>");
                                    Log.d(TAG, "RecvTransport.Listener#onConnect() dtlsParametes -> " + dtlsParameters);
                                    try {
                                        emitConsumeTransportConnect(dtlsParameters, storageId);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    mSocket.on("consumerTransportConnected", args -> {
                                        Log.d(TAG, " << CONSUMER TRANSPORT CONNECTED >>");
                                        if (args != null) {
                                            Log.d(TAG, "CONSUMER TRANSPORT CONNECTED >> " + args[0]);
                                        }
                                        // callback();
                                    });
                                }

                                @Override
                                public void onConnectionStateChange(Transport transport, String connectionState) {
                                    Log.d(TAG, "<<< RecvTransport.Listener#onConnectionStateChanged() >>>");
                                }
                            },
                            id,
                            iceParameters.toString(),
                            iceCandidates.toString(),
                            dtlsParameters.toString()));

//                    RecvTransport recvTransport = mMediaSoupDevice.createRecvTransport(
//                            new RecvTransport.Listener() {
//                                @Override
//                                public void onConnect(Transport transport, String dtlsParameters) {
//                                    Log.d(TAG, "<<< RecvTransport.Listener#onConnect() >>>");
//                                    Log.d(TAG, "RecvTransport.Listener#onConnect() dtlsParametes -> " + dtlsParameters);
//                                    try {
//                                        emitConsumeTransportConnect(dtlsParameters, storageId);
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                    mSocket.on("consumerTransportConnected", args -> {
//                                        Log.d(TAG, " << CONSUMER TRANSPORT CONNECTED >>");
//                                        if (args != null) {
//                                            Log.d(TAG, "CONSUMER TRANSPORT CONNECTED >> " + args[0]);
//                                        }
//                                        // callback();
//                                    });
//                                }
//
//                                @Override
//                                public void onConnectionStateChange(Transport transport, String connectionState) {
//                                    Log.d(TAG, "<<< RecvTransport.Listener#onConnectionStateChanged() >>>");
//                                }
//                            },
//                            id,
//                            iceParameters.toString(),
//                            iceCandidates.toString(),
//                            dtlsParameters.toString());

//                    mStorageSockIdsMap.put(producerSockid, storageId);
                    addToStorageIds(storageId, producerSockid);
                    consumerStorageIds.add(storageId);
                    Log.d(TAG, "1 Recv. Transports size >>> " + mRecvTransports.size());

                    emitStartConsuming(producerId, storageId);
                } catch (JSONException | MediasoupException e) {
                    e.printStackTrace();
                }
            }
        });

        mSocket.on("consumerCreated", args -> {
            Log.d(TAG, "<< Event Consumer Created");
            if (args != null) {
                Log.d(TAG, "Created Consumer -> " + String.valueOf(args[0]));
                try {
                    JSONObject obj = new JSONObject(String.valueOf(args[0]));
                    final String producerId = obj.getString("producerId");
                    final String kind = obj.getString("kind");
                    final String id = obj.getString("id");
                    final String type = obj.getString("type");
                    JSONObject rtpParams = obj.getJSONObject("rtpParameters");
                    boolean producerPaused = obj.getBoolean("producerPaused");
                    String producerSockId = obj.getString("producerSockId");
                    String storageId = obj.getString("storageId");

                    Log.d(TAG, "Consumer Created of Kind [" + kind + "]");
                    Log.d(TAG, "Consumer RTP Params >>> " + rtpParams);

                    ApiManager.build(mContext).fetchRoomData2(mMeetingClient.getMeetingId(), new ApiManager.ApiResult2() {
                        @Override
                        public void onResult(Call call, Response response) {
                            if (response != null) {
                                if (response.body() != null) {
                                    try {
                                        String resp = response.body().string();
                                        Log.d(TAG, "<< Room Data Response >> " + resp);
                                        final RoomData roomData = RoomData.fromJson(resp);
                                        if (roomData.getMembers() != null) {
                                            for (final RoomData.Member member : roomData.getMembers()) {
                                                if (member.getSocketId().equals(producerSockId)) {
                                                    Log.d(TAG, "<< Members has producerSocketId >> " + producerSockId);
                                                    final String username = member.getUsername();
                                                    final String email = member.getEmail();
                                                    final String finalUsername = username != null ? username : "Unknown";
                                                    // TODO Mark Check
                                                    try {
                                                        JSONObject peer = new JSONObject();
                                                        peer.put("id", producerSockId);
                                                        peer.put("screenShare", consumeScreen);
                                                        if (finalUsername != null) {
                                                            peer.put("displayName", finalUsername.isEmpty() ? "" : finalUsername);
                                                        } else {
                                                            peer.put("displayName", "?");
                                                        }
                                                        peer.put("device", null);
                                                        mStore.addPeer(producerSockId, peer);
                                                        if (finalUsername != null) {
                                                            WEvents.getInstance().notify(WEvents.EVENT_NEW_PEER_JOINED, finalUsername.isEmpty() ? "" : finalUsername);
                                                        } else {
                                                            WEvents.getInstance().notify(WEvents.EVENT_NEW_PEER_JOINED, "Unknown");
                                                        }
                                                        Log.d(TAG, "<< Created Peer >> " + peer);

                                                        // Peer id sent update peer form there.
//                                                    WooEvents.getInstance().notify(WooEvents.EVENT_TYPE_FETCH_ROOM_DATA, producerSockId);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    // TODO End Mark

                                                    RecvTransport recvTransport = mRecvTransports.get(storageId);
                                                    Consumer consumer = recvTransport
                                                            .consume(
                                                                    consumer1 -> mMeetingClient.getConsumers().remove(consumer1.getId()),
                                                                    id,
                                                                    producerId,
                                                                    kind,
                                                                    rtpParams.toString(),
                                                                    "{}");
                                                    mMeetingClient.getConsumers().put(consumer.getId(),
                                                            new MeetingClient.ConsumerHolder(producerSockId, consumer));
                                                    mStore.addConsumer(producerSockId, type, consumer, producerPaused);
                                                    mConsumerData.add(new ConsumerData(consumer.getId(), producerSockId, kind));
                                                    // Notify
                                                    if (!consumeScreen) {
                                                        WEvents.getInstance().notify(WEvents.EVENT_TYPE_CONSUMER_CREATED, id);
                                                    } else {
                                                        WEvents.getInstance().notify(WEvents.EVENT_REMOTE_SCREEN_CONSUMER_CREATED, consumer.getId());
                                                        consumeScreen = false;
                                                    }

                                                    // CONSUME BACK Starts here
                                                    emitConsumeBack(producerSockId);
                                                }
                                            }
                                        }

                                        if (roomData.getMicClosedUsers() != null) {
                                            for (String pId : roomData.getMicClosedUsers()) {
                                                mStore.getPeers().postValue(peers -> {
                                                    if (peers.getPeer(pId) != null) {
                                                        peers.getPeer(pId).setMicOn(false);
                                                    }
                                                });
                                            }
                                        }

                                        if (roomData.getHandRaisedUsers() != null) {
                                            for (String pId : roomData.getHandRaisedUsers()) {
                                                mStore.getPeers().postValue(peers -> {
                                                    if (peers.getPeer(pId) != null) {
                                                        peers.getPeer(pId).setHandRaised(true);
                                                    }
                                                });
                                            }
                                        }
                                    } catch (IOException | JSONException |
                                             MediasoupException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call call, Object error) {
                            Log.d(TAG, "Error while fetching room data " + error);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // Consume Back Events
        mSocket.on("consumeProducerBack", args -> {
            Log.d(TAG, "<< Event CONSUME PRODUCER BACK >>");
            if (args != null) {
                Log.d(TAG, "<< ARGS >> " + args[0]);
                try {
                    JSONObject obj = new JSONObject(String.valueOf(args[0]));
                    String producerId = obj.getString("producerId");
                    String producerSockId = obj.getString("producerSockId");
                    boolean screenShare = obj.getBoolean("screenShare");
                    consumeBackDeviceUUID = WDirector.getInstance().getDeviceUUID();
                    consumeBackDeviceUUIDs.add(consumeBackDeviceUUID);

                    producerSockIds.put(producerId, producerSockId);

                    emitCreateBackConsumeTransport(producerId, producerSockId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mSocket.on("consumeBackTransportCreated", args -> {
            Log.d(TAG, "<< Event Consume Back Transport Created");
            if (args != null) {
                Log.d(TAG, "<< ARGS >> " + args[0]);
                try {
                    JSONObject obj = new JSONObject(String.valueOf(args[0]));
                    JSONObject data = obj.getJSONObject("data");
                    String id = data.getString("id");
                    JSONObject iceParameters = data.getJSONObject("iceParameters");
                    JSONArray iceCandidates = data.getJSONArray("iceCandidates");
                    JSONObject dtlsParameters = data.getJSONObject("dtlsParameters");
                    String producerId = obj.getString("producerId");
                    String storageId = obj.getString("storageId");
                    String producerSockId = obj.getString("producerSockId");

//                    RecvTransport recvTransport = mMediaSoupDevice
//                            .createRecvTransport(new RecvTransport.Listener() {
//                                                     @Override
//                                                     public void onConnect(Transport transport, String dtlsParameters) {
//                                                         Log.d(TAG, "RecvTransport#onConnect() >> dtlsParameters > " + dtlsParameters);
//                                                         try {
//                                                             emitConsumeTransportConnectBack(dtlsParameters);
//                                                         } catch (JSONException e) {
//                                                             e.printStackTrace();
//                                                         }
//                                                         mSocket.on("consumerTransportConnectedBack", args1 -> {
//                                                             Log.d(TAG, "<< Event Consumer Transport Connected Back >>");
//                                                             if (args != null) {
//                                                                 Log.d(TAG, "<< EVENT CONSUMER TRANSPORT CONNECTED BACK >>> " + Arrays.toString(args1));
//                                                             }
//                                                             // callback()
//                                                         });
//                                                     }
//
//                                                     @Override
//                                                     public void onConnectionStateChange(Transport transport, String connectionState) {
//
//                                                     }
//                                                 },
//                                    id,
//                                    iceParameters.toString(),
//                                    iceCandidates.toString(),
//                                    dtlsParameters.toString());
                    mRecvTransports.put(storageId, mMediaSoupDevice
                            .createRecvTransport(new RecvTransport.Listener() {
                                                     @Override
                                                     public void onConnect(Transport transport, String dtlsParameters) {
                                                         Log.d(TAG, "RecvTransport#onConnect() >> dtlsParameters > " + dtlsParameters);
                                                         try {
                                                             emitConsumeTransportConnectBack(dtlsParameters);
                                                         } catch (JSONException e) {
                                                             e.printStackTrace();
                                                         }
                                                         mSocket.on("consumerTransportConnectedBack", args1 -> {
                                                             Log.d(TAG, "<< Event Consumer Transport Connected Back >>");
                                                             if (args != null) {
                                                                 Log.d(TAG, "<< EVENT CONSUMER TRANSPORT CONNECTED BACK >>> " + Arrays.toString(args1));
                                                             }
                                                             // callback()
                                                         });
                                                     }

                                                     @Override
                                                     public void onConnectionStateChange(Transport transport, String connectionState) {

                                                     }
                                                 },
                                    id,
                                    iceParameters.toString(),
                                    iceCandidates.toString(),
                                    dtlsParameters.toString()));
                    Log.d(TAG, "2 Recv. Transports size >>> " + mRecvTransports.size());
//                    mStorageSockIdsMap.put(producerSockId, storageId);
                    addToStorageIds(storageId, producerSockId);
                    consumerStorageIds.add(storageId);

                    emitStartConsumingBack(producerId, producerSockId);
                } catch (JSONException | MediasoupException e) {
                    e.printStackTrace();
                }
            }
        });

        mSocket.on("consumerCreatedBack", args -> {
            Log.d(TAG, "<< Event Consumer Created Back >>");
            if (args != null) {
                Log.d(TAG, "<< ARGS >> " + args[0]);
                try {
                    JSONObject obj = new JSONObject(String.valueOf(args[0]));
                    String producerId = obj.getString("producerId");
                    String kind = obj.getString("kind");
                    String id = obj.getString("id");
                    String type = obj.getString("type");
                    JSONObject rtpParams = obj.getJSONObject("rtpParameters");
                    boolean producerPaused = obj.getBoolean("producerPaused");
                    String producerSockId = obj.getString("producerSockId");
                    String storageId = obj.getString("storageId");

                    Log.d(TAG, "Consume Back RTP Params >>> " + rtpParams);

                    ApiManager.build(mContext).fetchRoomData2(mMeetingClient.getMeetingId(), new ApiManager.ApiResult2() {
                        @Override
                        public void onResult(Call call, Response response) {
                            if (response != null) {
                                if (response.body() != null) {
                                    try {
                                        String resp = response.body().string();
                                        Log.d(TAG, "<< Room Data Response >> " + resp);
                                        final RoomData roomData = RoomData.fromJson(resp);
                                        if (roomData.getMembers() != null) {
                                            for (final RoomData.Member member : roomData.getMembers()) {
                                                if (member.getSocketId().equals(producerSockId)) {
                                                    Log.d(TAG, "<< Members has producerSocketId >> " + producerSockId);
                                                    final String username = member.getUsername();
                                                    final String email = member.getEmail();
                                                    final String finalUsername = username != null ? username : email;
                                                    // TODO Mark Check
                                                    try {
                                                        JSONObject peer = new JSONObject();
                                                        peer.put("id", producerSockId);
                                                        if (finalUsername != null) {
                                                            peer.put("displayName", finalUsername.isEmpty() ? "" : finalUsername);
                                                        } else {
                                                            peer.put("displayName", "Unknown");
                                                        }
                                                        peer.put("device", null);
                                                        mStore.addPeer(producerSockId, peer);
                                                        if (finalUsername != null) {
                                                            WEvents.getInstance().notify(WEvents.EVENT_NEW_PEER_JOINED, finalUsername.isEmpty() ? "" : finalUsername);
                                                        } else {
                                                            WEvents.getInstance().notify(WEvents.EVENT_NEW_PEER_JOINED, "Unknown");
                                                        }
                                                        Log.d(TAG, "<< Created Peer >> " + peer);

                                                        // Peer id sent update peer form there.
//                                                    WooEvents.getInstance().notify(WooEvents.EVENT_TYPE_FETCH_ROOM_DATA, producerSockId);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    // TODO End Mark

                                                    RecvTransport recvTransport = mRecvTransports.get(storageId);
                                                    Consumer consumer = recvTransport
                                                            .consume(
                                                                    consumer1 -> mMeetingClient.getConsumers().remove(consumer1.getId()),
                                                                    id,
                                                                    producerId,
                                                                    kind,
                                                                    rtpParams.toString(),
                                                                    "{}");

                                                    mMeetingClient.getConsumers().put(consumer.getId(),
                                                            new MeetingClient.ConsumerHolder(producerSockId, consumer));
                                                    mStore.addConsumer(producerSockId, type, consumer, producerPaused);
                                                    mConsumerData.add(new ConsumerData(consumer.getId(), producerSockId, kind));
                                                    WEvents.getInstance().notify(WEvents.EVENT_TYPE_CONSUME_BACK_CREATED, obj);
                                                }
                                            }
                                        }

                                        if (roomData.getMicClosedUsers() != null) {
                                            for (String pId : roomData.getMicClosedUsers()) {
                                                mStore.getPeers().postValue(peers -> {
                                                    if (peers.getPeer(pId) != null) {
                                                        peers.getPeer(pId).setMicOn(false);
                                                    }
                                                });
                                            }
                                        }

                                        if (roomData.getHandRaisedUsers() != null) {
                                            for (String pId : roomData.getHandRaisedUsers()) {
                                                mStore.getPeers().postValue(peers -> {
                                                    if (peers.getPeer(pId) != null) {
                                                        peers.getPeer(pId).setHandRaised(true);
                                                    }
                                                });
                                            }
                                        }
                                    } catch (IOException | MediasoupException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call call, Object error) {
                            Log.d(TAG, "Error while fetching room data " + error);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mSocket.on("check", args -> {
            if (args != null) {
                Log.d(TAG, "<< Event Check -> " + args[0]);
                try {
                    JSONObject obj = new JSONObject(String.valueOf(args[0]));
                    String id = obj.getString("id");
                    String username = obj.getString("username");
                    boolean disconnected = obj.getBoolean("disconnect");
                    if (disconnected && !destroying) {
                        for (int i = 0; i < mConsumerData.size(); i++) {
                            if (mConsumerData.get(i).getPeerId().equals(id)) {
                                String consumerId = mConsumerData.get(i).getId();
                                RoomMessageHandler.ConsumerHolder holder = mMeetingClient.mConsumers.remove(consumerId);
                                if (holder == null) {
                                    break;
                                }
                                holder.mConsumer.close();
//                                mMeetingClient.mConsumers.remove(consumerId);
                                mStore.removeConsumer(holder.peerId, holder.mConsumer.getId());
                                mStore.removePeer(id);
                                mConsumerData.remove(i);
                            }
                        }
                        List<String> storageIds = getStorageId(id);
                        if (storageIds.size() > 0) {
                            for (String storageId : storageIds) {
                                if (mRecvTransports.size() > 0) {
                                    for (int i = 0; i < mRecvTransports.size(); i++) {
                                        RecvTransport r = mRecvTransports.get(storageId);
                                        if (r != null) {
                                            if (!r.isClosed()) {
                                                try {
                                                    r.close();
//                                                    r.dispose();
                                                } catch (Exception ex) {
                                                    ex.printStackTrace();
                                                } finally {
                                                    mRecvTransports.remove(storageId);
                                                    removeStorageId(id);
                                                    Log.d(TAG, "<< Removed & Disposed Recv Transport with id : " + storageId + " [size " + mRecvTransports.size() + "]");
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
//                        try {
//                            if (mRecvTransports.size() > 0) {
//                                String storageId = mStorageSockIdsMap.get(id);
//                                if (mRecvTransports.containsKey(storageId)) {
//                                    RecvTransport r = mRecvTransports.get(storageId);
//                                    if (r != null) {
//                                        if (!r.isClosed()) {
//                                            try {
//                                                r.close();
//                                                r.dispose();
//                                                Log.d(TAG, "<<< Disposed Recv Transport with storage ID >>> " + storageId);
//                                            } catch (Exception ex) {
//                                                ex.printStackTrace();
//                                            } finally {
//                                                mRecvTransports.remove(storageId);
//                                                Log.d(TAG, "<< Removed Recv Transport with storage ID >>> " + storageId);
//                                                Log.d(TAG, "<< Recv. Transport Map size >>> " + mRecvTransports.size());
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        } catch (Exception ex) {
//                            ex.printStackTrace();
//                        }
                        WEvents.getInstance().notify(WEvents.EVENT_TYPE_PEER_DISCONNECTED, obj);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mSocket.on("messageSent", args -> {
            if (args != null) {
                Log.d(TAG, "<< EVENT Message Sent " + args[0]);
                try {
                    Message message = Message.fromJson(String.valueOf(args[0]));
                    WEvents.getInstance().notify(WEvents.EVENT_RECEIVED_MESSAGE, message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mSocket.on("getTranslation", args -> {
            if (args != null) {
                Log.d(TAG, "<< EVENT Get Translation >> " + args[0]);
                WEvents.getInstance().notify(WEvents.EVENT_ON_TEXT_TRANSLATION_RECV, args[0]);
            }
        });

        mSocket.on("getVoiceTranslation", args -> {
            if (args != null) {
                if (args.length > 0) {
                    Log.d(TAG, "<< Event Voice Translation >>> " + args[0]);
                    WEvents.getInstance().notify(WEvents.EVENT_VOICE_TRANSLATION_RECEIVED, args[0]);
                }
            }
        });

        mSocket.on("micClosed", args -> {
            if (args != null) {
                Log.d(TAG, "<< ON EVENT MIC CLOSED >>> " + args[0]);
                try {
                    JSONObject obj = new JSONObject(String.valueOf(args[0]));
                    String pSId = obj.getString("socketId");
                    if (pSId != null) {
                        mStore.getPeers().postValue(peers -> {
                            Peer p = peers.getPeer(pSId);
                            if (p != null) {
                                p.setMicOn(false);
                            }
                        });
                    }
                    WEvents.getInstance().notify(WEvents.EVENT_PEER_MIC_MUTED, obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mSocket.on("micOpen", args -> {
            if (args != null) {
                Log.d(TAG, "<< ON EVENT MIC OPEN >>> " + args[0]);
                try {
                    JSONObject obj = new JSONObject(String.valueOf(args[0]));
                    String pSId = obj.getString("socketId");
                    if (pSId != null) {
                        mStore.getPeers().postValue(peers -> {
                            Peer p = peers.getPeer(pSId);
                            if (p != null) {
                                p.setMicOn(true);
                            }
                        });
                    }
                    WEvents.getInstance().notify(WEvents.EVENT_PEER_MIC_UNMUTED, obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mSocket.on("handRaised", args -> {
            if (args != null) {
                Log.d(TAG, "<< ON EVENT HAND RAISED >>> " + args[0]);
                try {
                    JSONObject obj = new JSONObject(String.valueOf(args[0]));
                    String pSId = obj.getString("socketId");
                    if (pSId != null) {
                        mStore.getPeers().postValue(peers -> {
                            Peer p = peers.getPeer(pSId);
                            if (p != null) {
                                p.setHandRaised(true);
                            }
                        });
                    }
                    WEvents.getInstance().notify(WEvents.EVENT_PEER_HAND_RAISED, obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mSocket.on("handLowered", args -> {
            if (args != null) {
                Log.d(TAG, "<< ON EVENT HAND LOWERED >>> " + args[0]);
                try {
                    JSONObject obj = new JSONObject(String.valueOf(args[0]));
                    String pSId = obj.getString("socketId");
                    if (pSId != null) {
                        mStore.getPeers().postValue(peers -> {
                            Peer p = peers.getPeer(pSId);
                            if (p != null) {
                                p.setHandRaised(false);
                            }
                        });
                    }
                    WEvents.getInstance().notify(WEvents.EVENT_PEER_HAND_LOWERED, obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mSocket.on("videoOpen", args -> {
            if (args != null) {
                Log.d(TAG, "<< ON EVENT VIDEO OPEN >>> " + args[0]);
                try {
                    JSONObject videoOn = new JSONObject(String.valueOf(args[0]));
                    String pSId = videoOn.getString("socketId");
                    if (pSId != null) {
                        mStore.getPeers().postValue(peers -> {
                            Peer p = peers.getPeer(pSId);
                            if (p != null) {
                                p.setCamOn(true);
                            }
                        });
                    }
                    WEvents.getInstance().notify(WEvents.EVENT_PEER_CAM_TURNED_ON, videoOn);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mSocket.on("videoClosed", args -> {
            if (args != null) {
                Log.d(TAG, "<< ON EVENT VIDEO CLOSE >>> " + args[0]);
                try {
                    JSONObject videoClosed = new JSONObject(String.valueOf(args[0]));
                    String pSId = videoClosed.getString("socketId");
                    if (pSId != null) {
                        mStore.getPeers().postValue(peers -> {
                            Peer p = peers.getPeer(pSId);
                            if (p != null) {
                                p.setCamOn(false);
                                for (int i = 0; i < mConsumerData.size(); i++) {
                                    if (mConsumerData.get(i).getPeerId().equals(pSId) && mConsumerData.get(i).getKind().equals("video")) {
                                        String consumerId = mConsumerData.get(i).getId();
                                        RoomMessageHandler.ConsumerHolder holder = mMeetingClient.mConsumers.remove(consumerId);
                                        if (holder == null) {
                                            break;
                                        }
                                        holder.mConsumer.close();
                                        mMeetingClient.mConsumers.remove(consumerId);
                                        mStore.removeConsumer(holder.peerId, holder.mConsumer.getId());
                                        mConsumerData.remove(i);
                                    }
                                }

                                List<String> storageIds = getStorageId(pSId);
                                if (storageIds.size() > 0) {
                                    for (String storageId : storageIds) {
                                        if (mRecvTransports.size() > 0) {
                                            for (int i = 0; i < mRecvTransports.size(); i++) {
                                                RecvTransport r = mRecvTransports.get(storageId);
                                                if (r != null) {
                                                    if (!r.isClosed()) {
                                                        try {
                                                            r.close();
//                                                            r.dispose();
                                                        } catch (Exception ex) {
                                                            ex.printStackTrace();
                                                        } finally {
                                                            mRecvTransports.remove(storageId);
                                                            removeStorageId(pSId);
                                                            Log.d(TAG, "<< Removed & Disposed Recv Transport with id : " + storageId + " [size " + mRecvTransports.size() + "]");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }
                    WEvents.getInstance().notify(WEvents.EVENT_PEER_CAM_TURNED_OFF, videoClosed);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // ADMIN EVENTS
        mSocket.on("muteEveryone", args -> {
            if (args != null) {
                Log.d(TAG, "<<< ON EVENT MUTE EVERYONE >>> " + args.toString());
                WEvents.getInstance().notify(WEvents.EVENT_RECEIVED_MUTE_EVERYONE, true);
            }
        });

        mSocket.on("muteMember", args -> {
            WEvents.getInstance().notify(WEvents.EVENT_RECEIVED_MUTE_MEMBER, true);
        });

        mSocket.on("CloseMemberVideo", args -> {
            WEvents.getInstance().notify(WEvents.EVENT_RECEIVED_CAM_OFF_MEMBER, true);
        });

        mSocket.on("kickout", args -> {
            WEvents.getInstance().notify(WEvents.EVENT_RECEIVED_KICKOUT, true);
        });

        mSocket.on("newAdmin", args -> {
            if (args != null) {
                if (args.length > 0) {
                    Log.d(TAG, "<<< Event newAdmin with payload -> " + args[0]);
                    try {
                        JSONObject obj = new JSONObject(String.valueOf(args[0]));
                        WEvents.getInstance().notify(WEvents.EVENT_ON_NEW_ADMIN, obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mSocket.on("screenShareOn", args -> {
            if (args != null) {
                if (args.length > 0) {
                    Log.d(TAG, "<< Event ScreenShareOn >> " + args[0]);
                    try {
                        JSONObject obj = new JSONObject(String.valueOf(args[0]));
                        WEvents.getInstance().notify(WEvents.EVENT_REMOTE_SCREEN_SHARE_ON, obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mSocket.on("screenShareOff", args -> {
            if (args != null) {
                if (args.length > 0) {
                    Log.d(TAG, "<< Event ScreenShareOff >> " + args[0]);
                    try {
                        JSONObject obj = new JSONObject(String.valueOf(args[0]));
                        WEvents.getInstance().notify(WEvents.EVENT_REMOTE_SCREEN_SHARE_OFF, obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    // Emitters
    private void emitRTPCaps() throws JSONException {
        JSONObject data = new JSONObject();
        data.put("id", this.mSocketId);
        Log.d(TAG, "Emitting RTP Caps event with data: " + data);
        mSocket.emit("getRTPCaps", data);
    }

    private void emitCreateTransport() throws JSONException, MediasoupException {
        JSONObject rtpCaps = new JSONObject(mMediaSoupDevice.getRtpCapabilities());
        JSONObject obj = new JSONObject();
        obj.put("id", this.mSocketId);
        obj.put("rtpCapabilities", rtpCaps);
        Log.d(TAG, "Emitting Create Transport >>> " + obj);
        mSocket.emit("createTransport", obj);
    }

    /**
     * @param dtlsParameters
     */
    private void emitConnectTransport(@NonNull final String dtlsParameters) throws JSONException {
        JSONObject dtls = new JSONObject(dtlsParameters);
        JSONObject obj = new JSONObject();
        obj.put("dtlsParameters", dtls);
        obj.put("id", mSocketId);
        Log.d(TAG, "Emitting Connect Transport >>> " + obj);
        mSocket.emit("connectTransport", obj);
    }

    /**
     * @param kind
     * @param rtpParams
     * @param screenShare
     * @throws JSONException
     */
    private void emitProduce(String kind, String rtpParams, boolean screenShare) throws JSONException {
        JSONObject rtpParameters = new JSONObject(rtpParams);
        JSONObject obj = new JSONObject();
        obj.put("kind", kind);
        obj.put("rtpParameters", rtpParameters);
        obj.put("id", mSocketId);
        obj.put("roomId", mMeetingClient.getMeetingId());
        if (screenShare) {
            JSONObject appData = new JSONObject();
            appData.put("type", "screenShare");
            obj.put("appData", appData);
        }
        Log.d(TAG, "Emitting Produce >>> " + obj);
        mSocket.emit("produce", obj);
    }

    /**
     * @param producerId
     * @param producerSockId
     * @throws JSONException
     */
    private void emitCreateConsumeTransport(@NonNull String producerId, @NonNull String producerSockId) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("producerId", producerId);
        obj.put("storageId", deviceUUID);
        obj.put("id", mSocketId);
        obj.put("producerSockId", producerSockId);
        Log.d(TAG, "Emitting createConsumeTransport >>> " + obj);
        mSocket.emit("createConsumeTransport", obj);
    }

    /**
     * @param dtlsParams
     * @param storageId
     * @throws JSONException
     */
    private void emitConsumeTransportConnect(@NonNull String dtlsParams, @NonNull String storageId) throws JSONException {
        JSONObject dtls = new JSONObject(dtlsParams);
        JSONObject obj = new JSONObject();
        obj.put("dtlsParameters", dtls);
        obj.put("id", mSocketId);
        obj.put("storageId", storageId);
        Log.d(TAG, "Emitting Consume Transport Connect >>> " + obj);
        mSocket.emit("consumeTransportConnect", obj);
    }

    /**
     * @param producerId
     * @param storageId
     * @throws MediasoupException
     * @throws JSONException
     */
    private void emitStartConsuming(@NonNull String producerId,
                                    @NonNull String storageId) throws MediasoupException, JSONException {
        JSONObject rtpCaps = new JSONObject(mMediaSoupDevice.getRtpCapabilities());
        JSONObject obj = new JSONObject();
        obj.put("rtpCapabilities", rtpCaps);
        obj.put("producerId", producerId);
        obj.put("id", mSocketId);
        obj.put("storageId", storageId);
        obj.put("producerSockId", producerSockIds.get(producerId));
        Log.d(TAG, "<<< Emitting Start Consuming >>> " + obj);
        mSocket.emit("startConsuming", obj);
    }

    /**
     * @param producerSockId
     * @throws JSONException
     */
    private void emitConsumeBack(@NonNull String producerSockId) throws JSONException {
        Log.d(TAG, "PRODUCER IDs SiZE >>> " + producerIdMap.size());
        for (Map.Entry<String, String> entry : producerIdMap.entrySet()) {
            JSONObject obj = new JSONObject();
            obj.put("producerId", entry.getValue());
            obj.put("producerSockId", mSocketId);
            obj.put("id", producerSockId);
            Log.d(TAG, "Emitting, Consume Back >>> " + obj);
            mSocket.emit("consumeBack", obj);
        }
    }

    /**
     * @param producerId
     * @param producerSockId
     * @throws JSONException
     */
    private void emitCreateBackConsumeTransport(@NonNull String producerId,
                                                @NonNull String producerSockId) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("producerId", producerId);
//        obj.put("storageId", consumeBackDeviceUUID);
        obj.put("storageId", consumeBackDeviceUUID);
        obj.put("id", mSocketId);
        obj.put("producerSockId", producerSockId);
        Log.d(TAG, "Emitting, Create Back Consume Transport >>> " + obj);
        mSocket.emit("createBackConsumeTransport", obj);
    }

    /**
     * @param dtlsParams
     * @throws JSONException
     */
    private void emitConsumeTransportConnectBack(@NonNull String dtlsParams) throws JSONException {
        JSONObject dtls = new JSONObject(dtlsParams);
        JSONObject obj = new JSONObject();
        obj.put("dtlsParameters", dtls);
        obj.put("id", mSocketId);
        obj.put("storageId", consumeBackDeviceUUID);
        Log.d(TAG, "Emitting, Consume Transport Connect Back >>> " + obj);
        mSocket.emit("consumeTransportConnectBack", obj);
    }

    /**
     * @param producerId
     * @param producerSockId
     * @throws JSONException
     * @throws MediasoupException
     */
    private void emitStartConsumingBack(@NonNull String producerId,
                                        @NonNull String producerSockId) throws JSONException, MediasoupException {
        JSONObject rtpCaps = new JSONObject(mMediaSoupDevice.getRtpCapabilities());
        JSONObject obj = new JSONObject();
        obj.put("rtpCapabilities", rtpCaps);
        obj.put("producerId", producerId);
        obj.put("id", mSocketId);
        obj.put("storageId", consumeBackDeviceUUID);
        obj.put("producerSockId", producerSockId);
        Log.d(TAG, "Emitting, Start Consuming Back" + obj);
        mSocket.emit("startConsumingBack", obj);
    }

    // Close Consumer
    private void emitClose() {
        JSONArray storageIds = new JSONArray();
        storageIds.put(deviceUUID);
        for (String uuid : consumeBackDeviceUUIDs) {
            storageIds.put(uuid);
        }
        Log.d(TAG, "Emitting Close Consumer >> " + storageIds);
        mSocket.emit("closeConsumer", storageIds);
    }

    /**
     * @param message
     */
    public void emitSendMessage(@NonNull Message message) {
        mWorkHandler.post(() -> {
            try {
                JSONObject obj = new JSONObject();
                obj.put("meetingId", message.getMeetingId());
                obj.put("name", message.getName());
                obj.put("socketId", message.getSocketId());
                obj.put("message", message.getMessage());
                obj.put("profileImage", message.getProfileImage());
                Log.d(TAG, "Emitting Message >> " + obj);
                mSocket.emit("messageSent", obj);
                WEvents.getInstance().notify(WEvents.EVENT_SENT_MESSAGE, message);
            } catch (JSONException e) {
                e.printStackTrace();
                WEvents.getInstance().notify(WEvents.EVENT_FAILURE_MESSAGE, message.getMessage());
            }
        });
    }

    /**
     * @param enableTranslation
     * @param voice
     * @throws JSONException
     */
    public void emitTextTranslation(boolean enableTranslation, boolean voice) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("meetingId", mMeetingClient.getMeetingId());
        obj.put("socketId", mSocketId);
        obj.put("value", enableTranslation);
        obj.put("type", voice ? "voice" : "text");
        Log.d(TAG, "Emitting textTranslation >>> " + obj);
        mSocket.emit("textTranslation", obj);
    }

    /**
     * @throws JSONException
     */
    public void emitMicClosed() throws JSONException {
        if (audioProducersIds.size() > 0) {
            JSONObject obj = new JSONObject();
            obj.put("socketId", mSocketId);
            obj.put("roomId", mMeetingClient.getMeetingId());
            obj.put("producerId", audioProducersIds.get(0));
            Log.d(TAG, "Emitting micClosed >>> " + obj);
            mSocket.emit("micClosed", obj);
        }
    }

    /**
     * @throws JSONException
     */
    public void emitMicOpen() throws JSONException {
        if (audioProducersIds.size() > 0) {
            JSONObject obj = new JSONObject();
            obj.put("socketId", mSocketId);
            obj.put("roomId", mMeetingClient.getMeetingId());
            obj.put("producerId", audioProducersIds.get(0));
            Log.d(TAG, "Emitting micOpen >>> " + obj);
            mSocket.emit("micOpen", obj);
        }
    }

    /**
     * @throws JSONException
     */
    public void emitVideoOpen() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("socketId", mSocketId);
        obj.put("roomId", mMeetingClient.getMeetingId());
        Log.d(TAG, "Emitting videoOpen >>> " + obj);
        mSocket.emit("videoOpen", obj);
    }

    /**
     * @throws JSONException
     */
    public void emitVideoClose() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("socketId", mSocketId);
        obj.put("roomId", mMeetingClient.getMeetingId());
        obj.put("producerId", videoProducerId);
        Log.d(TAG, "Emitting videoClose >>> " + obj);
        mSocket.emit("videoClosed", obj);
    }

    /**
     * @throws JSONException
     */
    public void emitHandRaised() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("socketId", mSocketId);
        obj.put("roomId", mMeetingClient.getMeetingId());
        Log.d(TAG, "Emitting handRaised >>> " + obj);
        mSocket.emit("handRaised", obj);
    }

    /**
     * @throws JSONException
     */
    public void emitHandLowered() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("socketId", mSocketId);
        obj.put("roomId", mMeetingClient.getMeetingId());
        Log.d(TAG, "Emitting handLowered >>> " + obj);
        mSocket.emit("handLowered", obj);
    }

    /**
     * @param langCode
     * @throws JSONException
     */
    public void emitUpdateLanguage(@NonNull String langCode) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("socketId", mSocketId);
        obj.put("language", langCode);
        Log.d(TAG, "Emitting Update Language >>> " + obj);
        mSocket.emit("updateLanguage", obj);
    }

    // Admin Events
    public void emitMuteEveryone() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("meetingId", mMeetingClient.getMeetingId());
        Log.d(TAG, "Emitting Mute Everyone >>> " + obj);
        mSocket.emit("muteEveryone", obj);
    }

    /**
     * @param memberSocketId
     */
    public void emitMuteMember(@NonNull String memberSocketId) {
        Log.d(TAG, "Emitting Mute Member >>> " + memberSocketId);
        mSocket.emit("muteMember", memberSocketId);
    }

    /**
     * @param memberSocketId
     */
    public void emitCloseMemberVideo(@NonNull String memberSocketId) {
        Log.d(TAG, "Emitting Close Member Video >>> " + memberSocketId);
        mSocket.emit("CloseMemberVideo", memberSocketId);
    }

    /**
     * @param memberSocketId
     */
    public void emitKickOutMember(@NonNull String memberSocketId) {
        Log.d(TAG, "Emitting Kick Out Memeber >>> " + memberSocketId);
        mSocket.emit("kickout", memberSocketId);
    }

    /**
     * @param accountUniqueId
     * @param username
     * @param email
     * @param picture
     */
    public void emitNewAdmin(@NonNull String accountUniqueId,
                             @NonNull String username,
                             @NonNull String email,
                             @Nullable String picture) throws JSONException {
        if (mMeetingClient != null) {
            JSONObject obj = new JSONObject();
            obj.put("meetingId", mMeetingClient.getMeetingId());
            JSONObject newAdmin = new JSONObject();
            newAdmin.put("accountUniqueId", accountUniqueId);
            newAdmin.put("username", username);
            newAdmin.put("email", email);
            newAdmin.put("picture", picture != null ? picture : "");
            obj.put("newAdmin", newAdmin);
            Log.d(TAG, "Emitting newAdmin with payload >>> " + obj);
            mSocket.emit("newAdmin", obj);
            WEvents.getInstance().notify(WEvents.EVENT_NEW_ADMIN_CREATED, true);
        }
    }

    /**
     * @throws JSONException
     */
    public void emitScreenShareOn() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("socketId", mSocketId);
        obj.put("roomId", mMeetingClient.getMeetingId());
        obj.put("username", mMeetingClient.getUsername());
        Log.d(TAG, "<< Emitting screenShareOn with payload >>> " + obj);
        mSocket.emit("screenShareOn", obj);
    }

    /**
     * @param producerId
     * @throws JSONException
     */
    public void emitScreenShareOff(@NonNull String producerId) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("socketId", mSocketId);
        obj.put("roomId", mMeetingClient.getMeetingId());
        obj.put("producerId", producerId);
        obj.put("username", mMeetingClient.getUsername());
        Log.d(TAG, "<< Emitting screenShareOff with payload >>> " + obj);
        mSocket.emit("screenShareOff", obj);
    }

    // End Emitters

    /**
     * @param rtpCaps
     */
    private void createMediaSoupDevice(String rtpCaps) {
        try {
            this.mMediaSoupDevice = new Device();
            this.mMediaSoupDevice.load(rtpCaps, null);
        } catch (MediasoupException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @param audProducerId
     */
    private void addAudioProducerId(@NonNull final String audProducerId) {
        for (String ids : audioProducersIds) {
            if (ids.equals(audProducerId)) {
                Log.d(TAG, "Audio Producer ID already exist. Skipping this one > [" + audProducerId + "]");
                return;
            }
        }
        audioProducersIds.add(audProducerId);
        Log.d(TAG, "<--------- AUDIO PRODUCERS IDs ----------->");
        for (String ids : audioProducersIds) {
            Log.d(TAG, "[" + ids + "]");
        }
    }

    /**
     *
     * @param peerId
     */
    public void closeVideoConsumer(@NonNull String peerId) {
        mStore.getPeers().postValue(peers -> {
            Peer p = peers.getPeer(peerId);
            if (p != null) {
                p.setCamOn(false);
                for (int i = 0; i < mConsumerData.size(); i++) {
                    if (mConsumerData.get(i).getPeerId().equals(peerId)) {
                        String consumerId = mConsumerData.get(i).getId();
                        RoomMessageHandler.ConsumerHolder holder = mMeetingClient.mConsumers.remove(consumerId);
                        if (holder == null) {
                            break;
                        }
                        holder.mConsumer.close();
//                        mMeetingClient.mConsumers.remove(consumerId);
//                        mStore.removeConsumer(holder.peerId, holder.mConsumer.getId());
                        mConsumerData.remove(i);
                    }
                }

                List<String> storageIds = getStorageId(peerId);
                if (storageIds.size() > 0) {
                    for (String storageId : storageIds) {
                        if (mRecvTransports.size() > 0) {
                            for (int i = 0; i < mRecvTransports.size(); i++) {
                                RecvTransport r = mRecvTransports.get(storageId);
                                if (r != null) {
                                    if (!r.isClosed()) {
                                        try {
                                            r.close();
                                            r.dispose();
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        } finally {
                                            mRecvTransports.remove(storageId);
                                            removeStorageId(peerId);
                                            Log.d(TAG, "<< Removed & Disposed Recv Transport with id : " + storageId + " [size " + mRecvTransports.size() + "]");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    // Send Transport Listener
    private SendTransport.Listener sendListener = new SendTransport.Listener() {

        @Override
        public String onProduce(Transport transport, String kind, String rtpParameters, String appData) {
            Log.d(TAG, "SendTransport.Listen.onProduce(transport, kind: " + kind + ", rtpParameters, appData: " + appData + ")");
            AtomicReference<String> producerId = new AtomicReference<>(null);
            try {
                emitProduce(kind, rtpParameters, produceScreen);
                produceScreen = false;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mSocket.on("producing", args -> {
                if (args != null) {
                    if (args.length > 0) {
                        try {
                            Log.d(TAG, "<< Event Producing >>> " + args[0]);
                            JSONObject obj = new JSONObject(String.valueOf(args[0]));
                            producerId.set(obj.getString("producerId"));
                            String kind1 = obj.getString("kind");
//                            JSONObject appData1 = obj.getJSONObject("appData");
                            if ("audio".equals(kind1)) {
                                addAudioProducerId(producerId.get());
                            } else if ("video".equals(kind1)) {
                                videoProducerId = producerId.get();
                            }
                            producerIdMap.put(kind1.equals("video") ? "video" : "audio", producerId.get());
                            Log.d(TAG, "<< Producer ID Map size >>> " + producerIdMap.size());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            while (producerId.get() == null) {
            }

            String pId = producerId.get();
            Log.d(TAG, "Producer ID >>> " + pId);
            Log.d(TAG, "returning producer id [" + pId + "]");
            return pId;
        }

        @Override
        public String onProduceData(Transport transport, String sctpStreamParameters, String label, String protocol, String appData) {
            Log.d(TAG, "SendTransport.Listen.onProduceData()");
            return null;
        }

        @Override
        public void onConnect(Transport transport, String dtlsParameters) {
            Log.d(TAG, "SendTransport.Listen.onConnect() >> dtlsParameters >>> " + dtlsParameters);
            try {
                emitConnectTransport(dtlsParameters);

                mSocket.on("transportConnected", args -> {
                    Log.d(TAG, "<<< Event > onTransportConnected >>> ");
                    if (args != null) {
                        if (args.length > 0) {
                            Log.d(TAG, "Transport Connected ARGS >> " + args[0]);
                        }
                    }
                    // callback();
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConnectionStateChange(Transport transport, String connectionState) {
            Log.d(TAG, "SendTransport.Listen.onConnectionStateChange(transport, connectionState: " + connectionState + " )");
            if ("failed".equals(connectionState)) {
                WEvents.getInstance().notify(WEvents.EVENT_CONNECTION_STATE_FAILED, true);
            } else if ("connected".equals(connectionState)) {
                WEvents.getInstance().notify(WEvents.EVENT_CONNECTION_STATE_CONNECTED, true);
            } else if ("disconnected".equals(connectionState)) {
                WEvents.getInstance().notify(WEvents.EVENT_CONNECTION_STATE_CONNECTING, true);
            }
        }
    };

    /**
     * Create and enable producers.
     *
     * @throws MediasoupException
     */
    private void createProducers() throws MediasoupException {
        Log.d(TAG, "<< Creating Mic & Camera Producers >>> ");
        mStore.setMediaCapabilities(true, true);
        mMainHandler.post(this::enableMic);
//        mMainHandler.post(this::enableCam);

        // Api Call For Put Member
        WEvents.getInstance().notify(WEvents.EVENT_TYPE_PRODUCER_CREATED, "");
    }

    @WorkerThread
    private void pauseConsumer(Consumer consumer) {
        Logger.d(TAG, "pauseConsumer() " + consumer.getId());
        if (consumer.isPaused()) {
            return;
        }

        consumer.pause();
        mStore.setConsumerPaused(consumer.getId(), "local");
    }

    @WorkerThread
    private void resumeConsumer(Consumer consumer) {
        Logger.d(TAG, "resumeConsumer() " + consumer.getId());
        if (!consumer.isPaused()) {
            return;
        }
        consumer.resume();
        mStore.setConsumerResumed(consumer.getId(), "local");
    }

    /**
     * @param pId
     */
    @Async
    public void removeVideoConsumer(@NonNull String pId) {
        mWorkHandler.post(() -> {
            for (MeetingClient.ConsumerHolder holder : mMeetingClient.getConsumers().values()) {
                if (holder.peerId.equals(pId)) {
                    if (holder.mConsumer.getKind().equals("video")) {
                        holder.mConsumer.getTrack().setEnabled(false);
                        holder.mConsumer.getTrack().dispose();
                        holder.mConsumer.close();
                    }
                    mStore.removeConsumer(pId, holder.mConsumer.getId());
                }
            }
        });
    }

    @Async
    public void enableAudioOnly() {
        Logger.d(TAG, "enableAudioOnly()");
        mStore.setAudioOnlyInProgress(true);

        disableCam();
        mWorkHandler.post(
                () -> {
                    for (MeetingClient.ConsumerHolder holder : mMeetingClient.getConsumers().values()) {
                        if (!"video".equals(holder.mConsumer.getKind())) {
                            continue;
                        }
                        pauseConsumer(holder.mConsumer);
                    }
                    mStore.setAudioOnlyState(true);
                    mStore.setAudioOnlyInProgress(false);
                });
    }

    @Async
    public void disableAudioOnly() {
        Logger.d(TAG, "disableAudioOnly()");
        mStore.setAudioOnlyInProgress(true);

        if (mCamProducer == null) {
//            enableCam();
        }
        mWorkHandler.post(
                () -> {
                    for (MeetingClient.ConsumerHolder holder : mMeetingClient.getConsumers().values()) {
                        if (!"video".equals(holder.mConsumer.getKind())) {
                            continue;
                        }
                        resumeConsumer(holder.mConsumer);
                    }
                    mStore.setAudioOnlyState(false);
                    mStore.setAudioOnlyInProgress(false);
                });
    }

    @Async
    public void enableMic() {
        Log.d(TAG, "enableMic()");
        mWorkHandler.post(this::enableMicImpl);
    }

    @Async
    public void disableMic() {
        Log.d(TAG, "disableMic()");
        mWorkHandler.post(this::disableMicImpl);
    }

    @Async
    public void muteMic() {
        Log.d(TAG, "muteMic()");
        mWorkHandler.post(this::muteMicImpl);
    }

    @Async
    public void unmuteMic() {
        Log.d(TAG, "unmuteMic()");
        mWorkHandler.post(this::unmuteMicImpl);
    }

    @Async
    public void muteAudio() {
        Logger.d(TAG, "muteAudio()");
        mStore.setAudioMutedState(true);
        mWorkHandler.post(
                () -> {
                    for (MeetingClient.ConsumerHolder holder : mMeetingClient.mConsumers.values()) {
                        if (!"audio".equals(holder.mConsumer.getKind())) {
                            continue;
                        }
                        pauseConsumer(holder.mConsumer);
                    }
                });
    }

    @Async
    public void unmuteAudio() {
        Logger.d(TAG, "unmuteAudio()");
        mStore.setAudioMutedState(false);
        mWorkHandler.post(
                () -> {
                    for (MeetingClient.ConsumerHolder holder : mMeetingClient.getConsumers().values()) {
                        if (!"audio".equals(holder.mConsumer.getKind())) {
                            continue;
                        }
                        resumeConsumer(holder.mConsumer);
                    }
                });
    }

    @Async
    public void pauseMyCam() {
        Log.d(TAG, "pauseMyCam()");
        mStore.setCamInProgress(false);
        mWorkHandler.post(() -> {
            mCamProducer.pause();
            mStore.setProducerPaused(mCamProducer.getId());
            mStore.setCamInProgress(false);
            mCamEnabled = false;
            mStore.getMe().postValue(me -> {
                me.setCamInProgress(false);
            });
            WEvents.getInstance().notify(WEvents.EVENT_ME_CAM_TURNED_OFF, true);
        });
    }

    public void resumeMyCam() {
        Log.d(TAG, "resumeMyCam()");
        mStore.setCamInProgress(true);
        mWorkHandler.post(() -> {
            mCamProducer.resume();
            mStore.setProducerResumed(mCamProducer.getId());
            mStore.setCamInProgress(true);
            mCamEnabled = true;
            mStore.getMe().postValue(me -> {
                me.setCamInProgress(true);
            });
            WEvents.getInstance().notify(WEvents.EVENT_ME_CAM_TURNED_ON, true);
        });
    }

    @Async
    public void enableCam() {
        Log.d(TAG, "enableCam()");
        mStore.setCamInProgress(true);
        mWorkHandler.post(() -> {
            enableCamImpl();
            mStore.setCamInProgress(false);
        });
    }

    @Async
    public void disableCam() {
        Log.d(TAG, "disableCam()");
        mWorkHandler.post(this::disableCamImpl);
    }

    @Async
    public void enableScreenShare(@NonNull VideoCapturer capturer, @NonNull DisplayMetrics dm) {
        Log.d(TAG, "enableScreenShare()");
        mWorkHandler.post(() -> enableScreenShareImpl(capturer, dm));
    }

    @Async
    public void disableScreenShare() {
        Log.d(TAG, "disableScreenShare()");
        mWorkHandler.post(this::disableScreenShareImpl);
    }

    @Async
    public void changeCam() {
        Log.d(TAG, "changeCam()");
        mStore.setCamInProgress(true);
        mWorkHandler.post(() ->
                mPeerConnectionUtils.switchCam(
                        new CameraVideoCapturer.CameraSwitchHandler() {
                            @Override
                            public void onCameraSwitchDone(boolean b) {
                                mStore.setCamInProgress(false);
                            }

                            @Override
                            public void onCameraSwitchError(String s) {
                                Logger.w(TAG, "changeCam() | failed: " + s);
                                mStore.addNotify("error", "Could not change cam: " + s);
                                mStore.setCamInProgress(false);
                            }
                        })
        );
    }

    @WorkerThread
    private void enableMicImpl() {
        Log.d(TAG, "enableMicImpl()");
        try {
            if (mMicProducer != null) {
                return;
            }
            if (!mMediaSoupDevice.isLoaded()) {
                Log.w(TAG, "enableMic() | device not loaded");
                return;
            }
            if (!mMediaSoupDevice.canProduce("audio")) {
                Log.w(TAG, "enableMic() | cannot produce audio");
                return;
            }
            if (mSendTransport == null) {
                Log.w(TAG, "enableMic() | mSendTransport isn't ready");
                return;
            }
            if (mLocalAudioTrack == null) {
                mLocalAudioTrack = mPeerConnectionUtils.createAudioTrack(mContext, "mic");
                mLocalAudioTrack.setEnabled(true);
                mMicEnabled = true;
            }
            mMicProducer = mSendTransport.produce(
                    producer -> {
                        Log.e(TAG, "onTransportClose(), micProducer");
                        if (mMicProducer != null) {
                            mStore.removeProducer(mMicProducer.getId());
                            mMicProducer = null;
                            mMicEnabled = false;
                        }
                    },
                    mLocalAudioTrack,
                    null,
                    null,
                    null);
            mStore.addProducer(mMicProducer);
            WEvents.getInstance().notify(WEvents.EVENT_ME_MIC_TURNED_ON, true);
        } catch (MediasoupException ex) {
            ex.printStackTrace();
        }
    }

    @WorkerThread
    private void disableMicImpl() {
        Logger.d(TAG, "disableMicImpl()");
        if (mMicProducer == null) {
            return;
        }

        mMicProducer.close();
        mStore.removeProducer(mMicProducer.getId());
        mMicProducer = null;
        mMicEnabled = false;
    }

    @WorkerThread
    private void muteMicImpl() {
        Logger.d(TAG, "muteMicImpl()");
        if (mMicProducer != null) {
            mMicProducer.pause();
            mStore.setProducerPaused(mMicProducer.getId());
            mStore.getMe().postValue(me -> {
                me.setAudioMuted(true);
            });
            WEvents.getInstance().notify(WEvents.EVENT_ME_MIC_TURNED_OFF, true);
        }
    }

    @WorkerThread
    private void unmuteMicImpl() {
        Logger.d(TAG, "unmuteMicImpl()");
        mMicProducer.resume();
        mStore.setProducerResumed(mMicProducer.getId());
        mStore.getMe().postValue(me -> {
            me.setAudioMuted(false);
        });
        WEvents.getInstance().notify(WEvents.EVENT_ME_MIC_TURNED_ON, true);
    }

    @WorkerThread
    private void enableCamImpl() {
        Logger.d(TAG, "enableCamImpl()");
        try {
            if (mCamProducer != null) {
                return;
            }
            if (!mMediaSoupDevice.isLoaded()) {
                Logger.w(TAG, "enableCam() | not loaded");
                return;
            }
            if (!mMediaSoupDevice.canProduce("video")) {
                Logger.w(TAG, "enableCam() | cannot produce video");
                return;
            }
            if (mSendTransport == null) {
                Logger.w(TAG, "enableCam() | mSendTransport isn't ready");
                return;
            }

            if (mLocalVideoTrack == null) {
                mLocalVideoTrack = mPeerConnectionUtils.createVideoTrack(mContext, "cam");
                mLocalVideoTrack.setEnabled(true);
                mCamEnabled = true;
            }
            mCamProducer =
                    mSendTransport.produce(
                            producer -> {
                                Logger.e(TAG, "onTransportClose(), camProducer");
                                if (mCamProducer != null) {
                                    mStore.removeProducer(mCamProducer.getId());
                                    mCamProducer = null;
                                    mCamEnabled = false;
                                }
                            },
                            mLocalVideoTrack,
                            null,
                            null,
                            null);
            mStore.addProducer(mCamProducer);
            WEvents.getInstance().notify(WEvents.EVENT_ME_CAM_TURNED_ON, true);
        } catch (MediasoupException e) {
            e.printStackTrace();
            mStore.addNotify("error", "Error enabling webcam: " + e.getMessage());
            if (mLocalVideoTrack != null) {
                mLocalVideoTrack.setEnabled(false);
            }
        }
    }

    @WorkerThread
    private void disableCamImpl() {
        Logger.d(TAG, "disableCamImpl()");
        if (mCamProducer == null) {
            return;
        }

        mCamProducer.close();
        mStore.removeProducer(mCamProducer.getId());

        mCamProducer = null;
        mCamEnabled = false;
        WEvents.getInstance().notify(WEvents.EVENT_ME_CAM_TURNED_OFF, true);
    }

    @WorkerThread
    private void enableScreenShareImpl(@NonNull VideoCapturer capturer, @NonNull DisplayMetrics dm) {
        Logger.d(TAG, "enableScreenShareImpl()");
        try {
            if (mScreenProducer != null) {
                return;
            }
            if (!mMediaSoupDevice.isLoaded()) {
                Logger.w(TAG, "enableScreenShareImpl() | not loaded");
                return;
            }
            if (!mMediaSoupDevice.canProduce("video")) {
                Logger.w(TAG, "enableScreenShareImpl() | cannot produce video");
                return;
            }
            if (mSendTransport == null) {
                Logger.w(TAG, "enableScreenShareImpl() | mSendTransport isn't ready");
                return;
            }

            if (mScreenVideoTrack == null) {
                mScreenVideoTrack = mPeerConnectionUtils.createScreenVideoTrack(mContext, capturer, "screen_share", dm);
                mScreenVideoTrack.setEnabled(true);
                isPresenting = true;
            }
            mScreenProducer =
                    mSendTransport.produce(
                            producer -> {
                                Logger.e(TAG, "onTransportClose(), screenProducer");
                                if (mScreenProducer != null) {
                                    mScreenProducer = null;
                                    isPresenting = false;
                                }
                            },
                            mScreenVideoTrack,
                            null,
                            null,
                            null);
//            mStore.addProducer(mScreenProducer);
            WEvents.getInstance().notify(WEvents.EVENT_PRESENTING, true);
        } catch (MediasoupException e) {
            e.printStackTrace();
            if (mScreenVideoTrack != null) {
                mScreenVideoTrack.setEnabled(false);
            }
        }
    }

    @WorkerThread
    private void disableScreenShareImpl() {
        Logger.d(TAG, "disableScreenShareImpl()");
        if (mScreenProducer == null) {
            return;
        }

        String producerId = mScreenProducer.getId();
        mScreenProducer.close();
//        mStore.removeProducer(mScreenProducer.getId());

        mScreenProducer = null;
        isPresenting = false;
        WEvents.getInstance().notify(WEvents.EVENT_PRESENTING, false);
        try {
            emitScreenShareOff(producerId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param roomStore
     * @param context
     * @param workHandler
     * @return {@link WSocket} as a Singleton.
     */
    public static WSocket create(
            @NonNull final Context context,
            @NonNull final RoomStore roomStore,
            @NonNull final Handler workHandler,
            @NonNull final MeetingClient meetingClient) {
        synchronized (WSocket.class) {
            if (sInstance == null) {
                sInstance = new WSocket(
                        context,
                        roomStore,
                        workHandler,
                        meetingClient);
            }
            return sInstance;
        }
    }

} /**
 * end class.
 */
