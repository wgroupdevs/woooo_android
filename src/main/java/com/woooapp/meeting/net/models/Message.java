package com.woooapp.meeting.net.models;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.woooapp.meeting.net.util.Json;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 11:19 am 02/10/2023
 * <code>class</code> Message.java
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message implements Serializable {

    private String meetingId;
    private String name;
    private String socketId;
    private String message;
    private String profileImage;

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    /**
     *
     * @return
     * @throws JsonProcessingException
     */
    public String toJson() throws JsonProcessingException {
        return Json.serailize(this);
    }

    /**
     *
     * @param json
     * @return
     * @throws IOException
     */
    public static Message fromJson(@NonNull final String json) throws IOException {
        return Json.deserialize(Message.class, json, true);
    }

} /** end class. */
