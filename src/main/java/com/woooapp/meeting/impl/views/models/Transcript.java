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
    private String producerId;
    private String senderName;

    /**
     *
     * @param translation
     * @param original
     * @param producerId
     */
    public Transcript(String translation, String original, String producerId) {
        this.translation = translation;
        this.original = original;
        this.producerId = producerId;
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

    public String getProducerId() {
        return producerId;
    }

    public void setProducerId(String producerId) {
        this.producerId = producerId;
    }

    @Nullable
    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
} /** end class. */
