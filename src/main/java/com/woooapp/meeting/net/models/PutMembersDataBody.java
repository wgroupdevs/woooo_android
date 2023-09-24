package com.woooapp.meeting.net.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.woooapp.meeting.net.util.Json;

import java.io.Serializable;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 2:51 pm 14/09/2023
 * <code>class</code> PutMembersDataBody.java
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PutMembersDataBody implements Serializable {

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

    @JsonIgnore
    @Override
    public String toString() {
        return "PutMembersDataBody{" +
                "accountUniqueId='" + accountUniqueId + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", picture='" + picture + '\'' +
                ", socketId='" + socketId + '\'' +
                '}';
    }

    /**
     *
     * @return
     * @throws JsonProcessingException
     */
    @JsonIgnore
    public String toJson() throws JsonProcessingException {
        return Json.serailize(this);
    }

} /** end class. */
