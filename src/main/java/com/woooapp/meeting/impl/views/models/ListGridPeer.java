package com.woooapp.meeting.impl.views.models;

import androidx.annotation.Nullable;

import com.woooapp.meeting.lib.model.Peer;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 5:30 pm 10/10/2023
 * <code>class</code> ListGridPeer.java
 */
public class ListGridPeer {

    public static final int VIEW_TYPE_ME_PEER = 0x00;
    public static final int VIEW_TYPE_PEER_PEER = 0x01;
    private final int viewType;
    private Peer peer1;
    private Peer peer2;

    /**
     *
     * @param viewType
     * @param peer1
     * @param peer2
     */
    public ListGridPeer(int viewType, @Nullable Peer peer1, @Nullable Peer peer2) {
        this.viewType = viewType;
        this.peer1 = peer1;
        this.peer2 = peer2;
    }

    /**
     *
     * @return
     */
    public int getViewType() {
        return viewType;
    }

    public Peer getPeer1() {
        return peer1;
    }

    public void setPeer1(Peer peer1) {
        this.peer1 = peer1;
    }

    public Peer getPeer2() {
        return peer2;
    }

    public void setPeer2(Peer peer2) {
        this.peer2 = peer2;
    }

} /** end class.  */
