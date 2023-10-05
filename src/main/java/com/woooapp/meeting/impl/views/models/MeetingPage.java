package com.woooapp.meeting.impl.views.models;

import java.util.List;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 4:39 pm 04/10/2023
 * <code>class</code> MeetingPage.java
 */
public final class MeetingPage {

    private int pageNo;
    private final List<GridPeer> mPeers;

    /**
     *
     * @param pageNo
     * @param mPeers
     */
    public MeetingPage(int pageNo, List<GridPeer> mPeers) {
        this.pageNo = pageNo;
        this.mPeers = mPeers;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public List<GridPeer> getmPeers() {
        return mPeers;
    }
} /** end class. */
