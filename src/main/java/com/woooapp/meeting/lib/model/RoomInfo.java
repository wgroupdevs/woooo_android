package com.woooapp.meeting.lib.model;

import static com.woooapp.meeting.lib.RoomClient.ConnectionState;
import static com.woooapp.meeting.lib.RoomClient.ConnectionState.NEW;

/**
 * @author Muneeb Ahmad
 * <p>
 * File RoomInfo.java
 * Class [RoomInfo]
 * on 08/09/2023 at 4:42 pm
 */
public class RoomInfo {

    private String mUrl;
    private String mRoomId;
    private ConnectionState mConnectionState = NEW;
    private String mActiveSpeakerId;
    private String mStatsPeerId;
    private boolean mFaceDetection = false;

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public String getRoomId() {
        return mRoomId;
    }

    public void setRoomId(String roomId) {
        this.mRoomId = roomId;
    }

    public ConnectionState getConnectionState() {
        return mConnectionState;
    }

    public void setConnectionState(ConnectionState connectionState) {
        this.mConnectionState = connectionState;
    }

    public String getActiveSpeakerId() {
        return mActiveSpeakerId;
    }

    public void setActiveSpeakerId(String activeSpeakerId) {
        this.mActiveSpeakerId = activeSpeakerId;
    }

    public String getStatsPeerId() {
        return mStatsPeerId;
    }

    public void setStatsPeerId(String statsPeerId) {
        this.mStatsPeerId = statsPeerId;
    }

    public boolean isFaceDetection() {
        return mFaceDetection;
    }

    public void setFaceDetection(boolean faceDetection) {
        this.mFaceDetection = faceDetection;
    }

} /**
 * end class [RoomInfo]
 */
