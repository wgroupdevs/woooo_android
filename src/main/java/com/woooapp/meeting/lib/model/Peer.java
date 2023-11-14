package com.woooapp.meeting.lib.model;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Muneeb Ahmad
 * <p>
 * File Peer.java
 * Class [Peer]
 * on 08/09/2023 at 4:51 pm
 */
public class Peer extends Info {

    private String mDisplayName;
    private DeviceInfo mDevice;
    private Set<String> mConsumers;
    private Set<String> mDataConsumers;
    private boolean videoOn = false;

    public Peer(@NonNull JSONObject info) {
        super();
        mId = info.optString("id");
        mDisplayName = info.optString("displayName");
        JSONObject deviceInfo = info.optJSONObject("device");
        if (deviceInfo != null) {
            mDevice =
                    new DeviceInfo()
                            .setFlag(deviceInfo.optString("flag"))
                            .setName(deviceInfo.optString("name"))
                            .setVersion(deviceInfo.optString("version"));
        } else {
            mDevice = DeviceInfo.unknownDevice();
        }
        mConsumers = new HashSet<>();
        mDataConsumers = new HashSet<>();
    }

    @Override
    public String getId() {
        return super.getId();
    }

    @Override
    public String getDisplayName() {
        return mDisplayName;
    }

    @Override
    public DeviceInfo getDevice() {
        return mDevice;
    }

    public void setDisplayName(String displayName) {
        this.mDisplayName = displayName;
    }

    public void setDevice(DeviceInfo device) {
        this.mDevice = device;
    }

    public boolean isVideoOn() {
        return videoOn;
    }

    public void setVideoOn(boolean videoOn) {
        this.videoOn = videoOn;
    }

    public Set<String> getConsumers() {
        return mConsumers;
    }
    public Set<String> getDataConsumers() {
        return mDataConsumers;
    }

} /**
 * end class [Peer]
 */
