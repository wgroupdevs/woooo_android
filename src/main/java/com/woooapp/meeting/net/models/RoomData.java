package com.woooapp.meeting.net.models;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.woooapp.meeting.net.util.Json;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 9:26 am 28/09/2023
 * <code>class</code> RoomData.java
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomData {

    private String meetingId;
    private String meetingName;
    private List<String> micClosedUsers;
    private List<String> handRaisedUsers;
    private List<Admin> admins;
    private List<Member> members;

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }

    public List<String> getMicClosedUsers() {
        return micClosedUsers;
    }

    public void setMicClosedUsers(List<String> micClosedUsers) {
        this.micClosedUsers = micClosedUsers;
    }

    public List<String> getHandRaisedUsers() {
        return handRaisedUsers;
    }

    public void setHandRaisedUsers(List<String> handRaisedUsers) {
        this.handRaisedUsers = handRaisedUsers;
    }

    public List<Admin> getAdmins() {
        return admins;
    }

    public void setAdmins(List<Admin> admins) {
        this.admins = admins;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Admin implements Serializable {

        private String accountUniqueId;
        private String username;
        private String email;
        private String picture;

        public String getAccountUniqueId() {
            return accountUniqueId;
        }

        public void setAccountUniqueId(String accountUniqueId) {
            this.accountUniqueId = accountUniqueId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }
    } // end class

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Member implements Serializable {
        private String accountUniqueId;
        private String email;
        private String username;
        private String picture;
        private String socketId;

        public String getAccountUniqueId() {
            return accountUniqueId;
        }

        public void setAccountUniqueId(String accountUniqueId) {
            this.accountUniqueId = accountUniqueId;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getSocketId() {
            return socketId;
        }

        public void setSocketId(String socketId) {
            this.socketId = socketId;
        }
    } /** end class. */

    /**
     *
     * @param json
     * @return
     * @throws IOException
     */
    @JsonIgnore
    public static RoomData fromJson(@NonNull String json) throws IOException {
        return (RoomData) Json.deserialize(RoomData.class, json, true);
    }

} /** end class. */
