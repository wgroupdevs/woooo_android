package com.woooapp.meeting.impl.views.models;

import androidx.annotation.Nullable;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 4:28 pm 12/10/2023
 * <code>class</code> Transcript.java
 */
public class Transcript {

    private String translation;
    private String original;
    private String socketId;
    private String senderName;

    /**
     *
     * @param translation
     * @param original
     * @param producerId
     */
    public Transcript(String translation, String original, String socketId) {
        this.translation = translation;
        this.original = original;
        this.socketId = socketId;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }

    @Nullable
    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
} /** end class. */
