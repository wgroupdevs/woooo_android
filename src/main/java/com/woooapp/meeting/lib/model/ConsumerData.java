package com.woooapp.meeting.lib.model;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 3:27 pm 14/11/2023
 * <code>class</code> ConsumerData.java
 */
public class ConsumerData {

    private final String id;
    private final String peerId;
    private final String kind;

    /**
     *
     * @param id
     * @param peerId
     * @param kind
     */
    public ConsumerData(String id, String peerId, String kind) {
        this.id = id;
        this.peerId = peerId;
        this.kind = kind;
    }

    public String getId() {
        return id;
    }

    public String getPeerId() {
        return peerId;
    }

    public String getKind() {
        return kind;
    }

} /** end class. */
