package com.woooapp.meeting.lib.lv;

import android.app.ActivityManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.woooapp.meeting.lib.model.Consumers;
import com.woooapp.meeting.lib.model.DeviceInfo;
import com.woooapp.meeting.lib.model.Me;
import com.woooapp.meeting.lib.model.Peer;
import com.woooapp.meeting.lib.model.Producers;

import org.json.JSONObject;
import org.mediasoup.droid.Consumer;
import org.mediasoup.droid.Producer;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 2:10 pm 11/11/2023
 * <code>class</code> RoomStore2.java
 */
public final class RoomStore2 {

    private static final String TAG = RoomStore2.class.getSimpleName() + ".java";
    private static RoomStore2 sInstance = null;
    private Me me;
    private final Map<String, Producer> producersMap;
    private final List<Peer> peerList;
    private final Map<String, Consumers.ConsumerWrapper> consumersMap;

    private RoomStore2() {
        this.producersMap = new ConcurrentHashMap<>();
        this.consumersMap = new ConcurrentHashMap<>();
        this.peerList = Collections.synchronizedList(new LinkedList<>());
    }

    /**
     *
     * @param peerId
     * @param displayName
     */
    public void setMe(@NonNull String peerId, @NonNull String displayName) {
        if (me == null) {
            me = new Me();
            me.setId(peerId);
            me.setDisplayName(displayName);
            me.setDevice(DeviceInfo.unknownDevice());
        }
    }

    /**
     *
     * @return
     */
    public Me getMe() {
        return me;
    }

    /**
     *
     * @param producer
     */
    public void addProducer(@NonNull Producer producer) {
       producersMap.put(producer.getId(), producer);
    }

    /**
     *
     * @param producerId
     */
    public void setProducerPaused(@NonNull String producerId) {
        Producer producer = producersMap.get(producerId);
        if (producer == null) {
            return;
        }
        producer.pause();
    }

    /**
     *
     * @param producerId
     */
    public void setProducerResumed(@NonNull String producerId) {
        Producer producer = producersMap.get(producerId);
        if (producer == null) {
            return;
        }
        producer.resume();
    }

    /**
     *
     * @param producerId
     */
    public void removeProducer(@NonNull String producerId) {
        producersMap.remove(producerId);
    }

    /**
     *
     * @param peerId
     * @param peerInfo
     */
    public void addPeer(@NonNull String peerId, @NonNull JSONObject peerInfo) {
        for (Peer p : peerList) {
            if (p.getId().equals(peerId)) {
                return;
            }
        }
        peerList.add(new Peer(peerInfo));
    }

    /**
     *
     * @param peerId
     * @param displayName
     */
    public void setPeerDisplayName(@NonNull String peerId, @NonNull String displayName) {
        for (Peer p : peerList) {
            if (p.getId().equals(peerId)) {
                p.setDisplayName(displayName);
                break;
            }
        }
    }

    /**
     *
     * @param peerId
     * @return
     */
    @Nullable
    public String getPeerDisplayName(@NonNull String peerId) {
        for (Peer p : peerList) {
            if (p.getId().equals(peerId)) {
                return p.getDisplayName();
            }
        }
        return null;
    }

    /**
     *
     * @param peerId
     * @return
     */
    @Nullable
    public Peer getPeer(@NonNull String peerId) {
        for (Peer p : peerList) {
            if (p.getId().equals(peerId)) {
                return p;
            }
        }
        return null;
    }

    /**
     *
     * @param peerId
     */
    public void removePeer(@NonNull String peerId) {
       for (int i = 0; i < peerList.size(); i++) {
           if (peerList.get(i).getId().equals(peerId)) {
               peerList.remove(i);
               break;
           }
       }
    }

    /**
     *
     * @return
     */
    @NonNull
    public List<Peer> getPeerList() {
        return this.peerList;
    }

    public List<List<Peer>> getPeerPaginatedLists() {
        List<List<Peer>> paginatedList = Collections.synchronizedList(new LinkedList<>());
        if (peerList.size() <= 5) {
            paginatedList.add(peerList);
        } else if (peerList.size() > 5 && peerList.size() <= 11) {
            paginatedList.add(peerList.subList(0, 5));
            paginatedList.add(peerList.subList(5, peerList.size()));
        } else if (peerList.size() > 11 && peerList.size() <= 17) {
            paginatedList.add(peerList.subList(0, 5));
            paginatedList.add(peerList.subList(5, 11));
            paginatedList.add(peerList.subList(11, peerList.size()));
        } else if (peerList.size() > 17 && peerList.size() <= 23) {
            paginatedList.add(peerList.subList(0, 5));
            paginatedList.add(peerList.subList(5, 11));
            paginatedList.add(peerList.subList(11, 17));
            paginatedList.add(peerList.subList(17, peerList.size()));
        } else if (peerList.size() > 23 && peerList.size() <= 29) {
            paginatedList.add(peerList.subList(0, 5));
            paginatedList.add(peerList.subList(5, 11));
            paginatedList.add(peerList.subList(11, 17));
            paginatedList.add(peerList.subList(17, 23));
            paginatedList.add(peerList.subList(23, peerList.size()));
        } else if (peerList.size() > 29 && peerList.size() <= 35) {
            paginatedList.add(peerList.subList(0, 5));
            paginatedList.add(peerList.subList(5, 11));
            paginatedList.add(peerList.subList(11, 17));
            paginatedList.add(peerList.subList(17, 23));
            paginatedList.add(peerList.subList(23, 29));
            paginatedList.add(peerList.subList(29, peerList.size()));
        } else if (peerList.size() > 35 && peerList.size() <= 41) {
            paginatedList.add(peerList.subList(0, 5));
            paginatedList.add(peerList.subList(5, 11));
            paginatedList.add(peerList.subList(11, 17));
            paginatedList.add(peerList.subList(17, 23));
            paginatedList.add(peerList.subList(23, 29));
            paginatedList.add(peerList.subList(29, 35));
            paginatedList.add(peerList.subList(35, peerList.size()));
        } else if (peerList.size() > 41 && peerList.size() <= 47) {
            paginatedList.add(peerList.subList(0, 5));
            paginatedList.add(peerList.subList(5, 11));
            paginatedList.add(peerList.subList(11, 17));
            paginatedList.add(peerList.subList(17, 23));
            paginatedList.add(peerList.subList(23, 29));
            paginatedList.add(peerList.subList(29, 35));
            paginatedList.add(peerList.subList(35, 41));
            paginatedList.add(peerList.subList(41, peerList.size()));
        } else if (peerList.size() > 47 && peerList.size() <= 53) {
            paginatedList.add(peerList.subList(0, 5));
            paginatedList.add(peerList.subList(5, 11));
            paginatedList.add(peerList.subList(11, 17));
            paginatedList.add(peerList.subList(17, 23));
            paginatedList.add(peerList.subList(23, 29));
            paginatedList.add(peerList.subList(29, 35));
            paginatedList.add(peerList.subList(35, 41));
            paginatedList.add(peerList.subList(41, 47));
            paginatedList.add(peerList.subList(47, peerList.size()));
        } else if (peerList.size() > 53 && peerList.size() <= 59) {
            paginatedList.add(peerList.subList(0, 5));
            paginatedList.add(peerList.subList(5, 11));
            paginatedList.add(peerList.subList(11, 17));
            paginatedList.add(peerList.subList(17, 23));
            paginatedList.add(peerList.subList(23, 29));
            paginatedList.add(peerList.subList(29, 35));
            paginatedList.add(peerList.subList(35, 41));
            paginatedList.add(peerList.subList(41, 47));
            paginatedList.add(peerList.subList(47, 53));
            paginatedList.add(peerList.subList(53, peerList.size()));
        }
        return paginatedList;
    }

    /**
     *
     * @param peerId
     * @param type
     * @param consumer
     * @param remotelyPaused
     */
    public void addConsumer(@NonNull String peerId, @NonNull String type, @NonNull Consumer consumer, boolean remotelyPaused) {
        consumersMap.put(consumer.getId(), new Consumers.ConsumerWrapper(type, remotelyPaused, consumer));
        for (Peer p : peerList) {
            if (p.getId().equals(peerId)) {
                p.getConsumers().add(consumer.getId());
                break;
            }
        }
    }

    /**
     *
     * @param peerId
     * @param consumerId
     */
    public void removeConsumer(@NonNull String peerId, @NonNull String consumerId) {
        consumersMap.remove(consumerId);
        Peer p = getPeer(peerId);
        if (p == null) {
            return;
        }
        p.getConsumers().remove(consumerId);
    }

    public void setConsumerPaused(@NonNull String consumerId) {
        Consumers.ConsumerWrapper wrapper = consumersMap.get(consumerId);
        if (wrapper == null) {
            return;
        }
        wrapper.mLocallyPaused = true;
    }

    public void setConsumerResumed(@NonNull String consumerId) {
        Consumers.ConsumerWrapper wrapper = consumersMap.get(consumerId);
        if (wrapper == null) {
            return;
        }
        wrapper.mLocallyPaused = false;
    }

    /**
     *
     * @param consumerId
     * @return
     */
    @Nullable
    public Consumers.ConsumerWrapper getConsumer(@NonNull String consumerId) {
        return consumersMap.get(consumerId);
    }

    /**
     *
     * @return
     */
    public Map<String, Consumers.ConsumerWrapper> getConsumers() {
        return this.consumersMap;
    }

    /**
     *
     * @return
     */
    public static RoomStore2 getInstance() {
        synchronized (RoomStore2.class) {
            if (sInstance == null) {
                sInstance = new RoomStore2();
            }
            return sInstance;
        }
    }

    public void destroy() {
        synchronized (RoomStore2.class) {
            // TODO
            if (sInstance != null) {
                sInstance = null;
            }
        }
    }

} /** end class. */
