package com.woooapp.meeting.impl.views.models;

import android.view.View;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 5:37 pm 10/10/2023
 * <code>class</code> MeetingPage2.java
 */
public class MeetingPage2 {

    private int pageNo;
    private View contentView;

    /**
     *
     * @param pageNo
     * @param contentView
     */
    public MeetingPage2(int pageNo, @NonNull View contentView) {
        this.pageNo = pageNo;
        this.contentView = contentView;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public View getContentView() {
        return contentView;
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
    }
} /** end class. */
