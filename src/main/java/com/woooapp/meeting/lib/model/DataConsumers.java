package com.woooapp.meeting.lib.model;

import org.mediasoup.droid.DataConsumer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Muneeb Ahmad
 * <p>
 * File DataModel.java
 * Class [DataModel]
 * on 08/09/2023 at 4:46 pm
 */
public class DataConsumers {

    private final Map<String, DataConsumer> dataConsumers;

    public DataConsumers() {
        dataConsumers = new ConcurrentHashMap<>();
    }

    public void addDataConsumer(DataConsumer dataConsumer) {
        dataConsumers.put(dataConsumer.getId(), dataConsumer);
    }

    public void removeDataConsumer(String dataConsumerId) {
        dataConsumers.remove(dataConsumerId);
    }

} /**
 * end class [DataModel]
 */
