package com.woooapp.meeting.lib.model;

import com.woooapp.meeting.lib.Utils;

/**
 * @author Muneeb Ahmad
 * <p>
 * File Notify.java
 * Class [Notify]
 * on 08/09/2023 at 4:51 pm
 */
public class Notify {

    private String mId;
    private String mType;
    private String mTitle;
    private String mText;
    private int mTimeout;

    public Notify(String type, String text) {
        this(type, text, 0);
    }

    public Notify(String type, String text, int timeout) {
        this(type, null, text, timeout);
    }

    public Notify(String type, String title, String text, int timeout) {
        this.mId = Utils.getRandomString(6).toLowerCase();
        this.mType = type;
        this.mTitle = title;
        this.mText = text;
        this.mTimeout = timeout;
        if (this.mTimeout == 0) {
            if ("info".equals(this.mType)) {
                this.mTimeout = 3000;
            } else if ("error".equals(this.mType)) {
                this.mTimeout = 5000;
            }
        }
    }

    public String getId() {
        return mId;
    }

    public String getType() {
        return mType;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getText() {
        return mText;
    }

    public int getTimeout() {
        return mTimeout;
    }

} /**
 * end class [Notify]
 */
