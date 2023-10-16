package com.woooapp.meeting.impl.views.models;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 4:39 pm 04/10/2023
 * <code>class</code> MeetingPage.java
 */
public final class MeetingPage {

    private int pageNo;
    private List<ListGridPeer> mPeers;

    /**
     *
     * @param pageNo
     * @param mPeers
     */
    public MeetingPage(int pageNo, @NonNull List<ListGridPeer> mPeers) {
        this.pageNo = pageNo;
        this.mPeers = mPeers;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public List<ListGridPeer> getmPeers() {
        return mPeers;
    }

    public void setPeers(List<ListGridPeer> mPeers) {
        this.mPeers = mPeers;
    }
} /** end class. */
