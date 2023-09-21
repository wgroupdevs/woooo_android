package com.woooapp.meeting.net.models;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.woooapp.meeting.net.util.Json;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 2:55 pm 14/09/2023
 * <code>class</code> PutMembersDataResponse.java
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PutMembersDataResponse implements Serializable {

    private String id;
    private String meetingId;
    private String meetingName;
    // TODO add micClosedUsers
    // TODO add handRaisedUsers
    private List<CreateMeetingResponse.Admin> admins;
    private List<Member> members;
    private String createdAt;
    private String updatedAt;

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

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

    public List<CreateMeetingResponse.Admin> getAdmins() {
        return admins;
    }

    public void setAdmins(List<CreateMeetingResponse.Admin> admins) {
        this.admins = admins;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    private static class Member implements Serializable {
        private String accountUniqueId;
        private String email;
        private String username;
        private String picture;
        private String socketId;
        private String id;

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

        @JsonProperty("_id")
        public String getId() {
            return id;
        }

        @JsonProperty("_id")
        public void setId(String id) {
            this.id = id;
        }
    } // end class

    @Override
    public String toString() {
        return "PutMembersDataResponse{" +
                "id='" + id + '\'' +
                ", meetingId='" + meetingId + '\'' +
                ", meetingName='" + meetingName + '\'' +
                ", admins=" + admins +
                ", members=" + members +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }

    /**
     *
     * @param jsonStr
     * @return
     * @throws IOException
     */
    @JsonIgnore
    public static PutMembersDataResponse fromJson(@NonNull String jsonStr) throws IOException {
        return Json.deserialize(PutMembersDataResponse.class, jsonStr, true);
    }

} /** end class. */
