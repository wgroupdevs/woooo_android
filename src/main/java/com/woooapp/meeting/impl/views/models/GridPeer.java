package com.woooapp.meeting.impl.views.models;

import androidx.annotation.NonNull;

import com.woooapp.meeting.lib.model.Peer;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 2:48 pm 26/09/2023
 * <code>class</code> GridPeer.java
 */
public class GridPeer {

    public static final int VIEW_TYPE_ME = 0x00;
    public static final int VIEW_TYPE_PEER = 0x01;

    private int viewType = VIEW_TYPE_PEER;
    private Peer peer;

    /**
     *
     * @param viewType
     * @param peer
     */
    public GridPeer(int viewType, Peer peer) {
        this.viewType = viewType;
        this.peer = peer;
    }

    public Peer getPeer() {
        return peer;
    }

    public void setPeer(Peer peer) {
        this.peer = peer;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

} /** end class. */
