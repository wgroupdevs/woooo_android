package com.woooapp.meeting.impl.views.models;

import com.woooapp.meeting.net.models.Message;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 2:19 pm 06/10/2023
 * <code>class</code> Chat.java
 */
public class Chat {

    public static final int MESSAGE_TYPE_SENT = 0x00;
    public static final int MESSAGE_TYPE_RECV = 0x01;
    private int messageType = -1;
    private Message message;

    /**
     *
     * @param messageType
     * @param message
     */
    public Chat(int messageType, Message message) {
        this.messageType = messageType;
        this.message = message;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
} /** end class. */
