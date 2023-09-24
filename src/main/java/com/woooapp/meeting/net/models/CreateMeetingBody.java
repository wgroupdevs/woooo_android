package com.woooapp.meeting.net.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.woooapp.meeting.net.util.Json;

import java.io.Serializable;
import java.util.List;

/**
 * @author Muneeb Ahmad
 * <p>
 * File CreateMeeting.java
 * Class [PreMeeting]
 * on 09/09/2023 at 6:55 am
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateMeetingBody implements Serializable {

    private String meetingId;
    private String meetingName;

    private List<Admin> admins;

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

    /**
     *
     * @return
     * @throws JsonProcessingException
     */
    @JsonIgnore
    public String toJson() throws JsonProcessingException {
        return Json.serailize(this);
    }

} /**
 * end class [PreMeeting]
 */
