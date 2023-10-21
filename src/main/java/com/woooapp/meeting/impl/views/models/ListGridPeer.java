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
    private boolean peer1MicOn = true;
    private boolean peer1CamOn = false;
    private boolean peer1HandRaised = false;
    private Peer peer2;
    private boolean peer2MicOn = true;
    private boolean peer2CamOn = false;
    private boolean peer2HandRaised = false;

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

    public boolean isPeer1MicOn() {
        return peer1MicOn;
    }

    public void setPeer1MicOn(boolean peer1MicOn) {
        this.peer1MicOn = peer1MicOn;
    }

    public boolean isPeer1CamOn() {
        return peer1CamOn;
    }

    public void setPeer1CamOn(boolean peer1CamOn) {
        this.peer1CamOn = peer1CamOn;
    }

    public boolean isPeer1HandRaised() {
        return peer1HandRaised;
    }

    public void setPeer1HandRaised(boolean peer1HandRaised) {
        this.peer1HandRaised = peer1HandRaised;
    }

    public boolean isPeer2MicOn() {
        return peer2MicOn;
    }

    public void setPeer2MicOn(boolean peer2MicOn) {
        this.peer2MicOn = peer2MicOn;
    }

    public boolean isPeer2CamOn() {
        return peer2CamOn;
    }

    public void setPeer2CamOn(boolean peer2CamOn) {
        this.peer2CamOn = peer2CamOn;
    }

    public boolean isPeer2HandRaised() {
        return peer2HandRaised;
    }

    public void setPeer2HandRaised(boolean peer2HandRaised) {
        this.peer2HandRaised = peer2HandRaised;
    }

} /** end class.  */
