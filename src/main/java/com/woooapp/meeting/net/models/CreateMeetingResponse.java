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
 * Created On 3:50 pm 11/09/2023
 * <code>class</code> PreMeetingResponse.java
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateMeetingResponse implements Serializable {

    private String id;
    private String meetingId;
    private String meetingName;

    private List<Admin> admins;

    private String createdAt;
    private String updatedAt;

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

    public List<Admin> getAdmins() {
        return admins;
    }

    public void setAdmins(List<Admin> admins) {
        this.admins = admins;
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

    public static class Admin implements Serializable {

        private String id;
        private String accountUniqueId;
        private String username;
        private String email;
        private String picture;

        public String getId() {
            return id;
        }

        @JsonProperty("_id")
        public void setId(String id) {
            this.id = id;
        }

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

    @Override
    public String toString() {
        return "PreMeetingResponse{" +
                "id='" + id + '\'' +
                ", meetingId='" + meetingId + '\'' +
                ", meetingName='" + meetingName + '\'' +
                ", admins=" + admins +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }

    /**
     *
     * @param jsonString
     * @return
     * @throws IOException
     */
    @JsonIgnore
    public static CreateMeetingResponse fromJson(@NonNull String jsonString) throws IOException {
        return Json.deserialize(CreateMeetingResponse.class, jsonString, true);
    }

} /** end class. */
