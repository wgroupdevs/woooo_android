package com.woooapp.meeting.impl.views.models;

import com.woooapp.meeting.lib.MeetingClient;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 12:45 am 21/10/2023
 * <code>class</code> Member.java
 */
public final class Member {

    private String socketId;
    private String name;
    private String picture;
    private MeetingClient.Role role;

    /**
     *
     * @param socketId
     * @param name
     * @param picture
     * @param role
     */
    public Member(String socketId, String name, String picture, MeetingClient.Role role) {
        this.socketId = socketId;
        this.name = name;
        this.picture = picture;
        this.role = role;
    }

    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public MeetingClient.Role getRole() {
        return role;
    }

    public void setRole(MeetingClient.Role role) {
        this.role = role;
    }

} /** end class. */
