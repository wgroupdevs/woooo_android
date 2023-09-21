package com.woooapp.meeting.lib.model;

import androidx.annotation.NonNull;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 1:13 pm 13/09/2023
 * <code>class</code> PeersScreen.java
 */
public class PeersScreen {

    private Peer peer1;
    private Peer peer2;
    private Peer peer3;
    private Peer peer4;

    /**
     *
     * @param peer1
     * @param peer2
     * @param peer3
     * @param peer4
     */
    public PeersScreen(@NonNull Peer peer1, Peer peer2, Peer peer3, Peer peer4) {
        this.peer1 = peer1;
        this.peer2 = peer2;
        this.peer3 = peer3;
        this.peer4 = peer4;
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

    public Peer getPeer3() {
        return peer3;
    }

    public void setPeer3(Peer peer3) {
        this.peer3 = peer3;
    }

    public Peer getPeer4() {
        return peer4;
    }

    public void setPeer4(Peer peer4) {
        this.peer4 = peer4;
    }

} /** end class. */
