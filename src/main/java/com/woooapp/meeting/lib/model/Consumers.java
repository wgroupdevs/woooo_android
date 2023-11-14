package com.woooapp.meeting.lib.model;

import org.json.JSONArray;
import org.mediasoup.droid.Consumer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Muneeb Ahmad
 * <p>
 * File Consumers.java
 * Class [Consumers]
 * on 08/09/2023 at 4:45 pm
 */
public class Consumers {

    public static class ConsumerWrapper {

        private String mType;
        public boolean mLocallyPaused;
        public boolean mRemotelyPaused;
        private int mSpatialLayer;
        private int mTemporalLayer;
        private Consumer mConsumer;
        private JSONArray mScore;
        private int mPreferredSpatialLayer;
        private int mPreferredTemporalLayer;

        public ConsumerWrapper(String type, boolean remotelyPaused, Consumer consumer) {
            mType = type;
            mLocallyPaused = false;
            mRemotelyPaused = remotelyPaused;
            mSpatialLayer = -1;
            mTemporalLayer = -1;
            mConsumer = consumer;
            mPreferredSpatialLayer = -1;
            mPreferredTemporalLayer = -1;
        }

        public String getType() {
            return mType;
        }

        public boolean isLocallyPaused() {
            return mLocallyPaused;
        }

        public boolean isRemotelyPaused() {
            return mRemotelyPaused;
        }

        public int getSpatialLayer() {
            return mSpatialLayer;
        }

        public int getTemporalLayer() {
            return mTemporalLayer;
        }

        public Consumer getConsumer() {
            return mConsumer;
        }

        public JSONArray getScore() {
            return mScore;
        }

        public int getPreferredSpatialLayer() {
            return mPreferredSpatialLayer;
        }

        public int getPreferredTemporalLayer() {
            return mPreferredTemporalLayer;
        }
    }

    private final Map<String, ConsumerWrapper> consumers;

    public Consumers() {
        consumers = new ConcurrentHashMap<>();
    }

    public void addConsumer(String type, Consumer consumer, boolean remotelyPaused) {
        consumers.put(consumer.getId(), new ConsumerWrapper(type, remotelyPaused, consumer));
    }

    public void removeConsumer(String consumerId) {
        consumers.remove(consumerId);
    }

    public void setConsumerPaused(String consumerId, String originator) {
        ConsumerWrapper wrapper = consumers.get(consumerId);
        if (wrapper == null) {
            return;
        }

        if ("local".equals(originator)) {
            wrapper.mLocallyPaused = true;
        } else {
            wrapper.mRemotelyPaused = true;
        }
    }

    public void setConsumerResumed(String consumerId, String originator) {
        ConsumerWrapper wrapper = consumers.get(consumerId);
        if (wrapper == null) {
            return;
        }

        if ("local".equals(originator)) {
            wrapper.mLocallyPaused = false;
        } else {
            wrapper.mRemotelyPaused = false;
        }
    }

    public void setConsumerCurrentLayers(String consumerId, int spatialLayer, int temporalLayer) {
        ConsumerWrapper wrapper = consumers.get(consumerId);
        if (wrapper == null) {
            return;
        }
        wrapper.mSpatialLayer = spatialLayer;
        wrapper.mTemporalLayer = temporalLayer;
    }

    public void setConsumerScore(String consumerId, JSONArray score) {
        ConsumerWrapper wrapper = consumers.get(consumerId);
        if (wrapper == null) {
            return;
        }

        wrapper.mScore = score;
    }

    public ConsumerWrapper getConsumer(String consumerId) {
        return consumers.get(consumerId);
    }

    public void clear() {
        consumers.clear();
    }

} /**
 * end class [Consumers]
 */
