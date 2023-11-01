package com.woooapp.meeting.net.models;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.woooapp.meeting.net.util.Json;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 11:00 am 01/11/2023
 * <code>class</code> ChatTranslation.java
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatTranslation implements Serializable {

    @JsonProperty("Success")
    private boolean success;
    @JsonProperty("Message")
    private String message;
    @JsonProperty("Error")
    private String error;
    @JsonProperty("Data")
    private Data data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data implements Serializable {
        private String text;
        private String languageCode;
        private String serverMsgId;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getLanguageCode() {
            return languageCode;
        }

        public void setLanguageCode(String languageCode) {
            this.languageCode = languageCode;
        }

        public String getServerMsgId() {
            return serverMsgId;
        }

        public void setServerMsgId(String serverMsgId) {
            this.serverMsgId = serverMsgId;
        }

    } // end class

    /**
     *
     * @param json
     * @return
     * @throws IOException
     */
    public static ChatTranslation fromJson(@NonNull String json) throws IOException {
        return Json.deserialize(ChatTranslation.class, json, true);
    }

} /** end class. */
