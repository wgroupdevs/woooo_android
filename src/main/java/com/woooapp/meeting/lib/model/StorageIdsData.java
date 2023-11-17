package com.woooapp.meeting.lib.model;

import java.io.Serializable;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 10:46 am 16/11/2023
 * <code>class</code> StorageIdsData.java
 */
public class StorageIdsData implements Serializable {

    private final String producerSockId;
    private final String storageId;

    /**
     *
     * @param producerSockId
     * @param storageId
     */
    public StorageIdsData(String producerSockId, String storageId) {
        this.producerSockId = producerSockId;
        this.storageId = storageId;
    }

    public String getProducerSockId() {
        return producerSockId;
    }

    public String getStorageId() {
        return storageId;
    }

} /** end class. */
