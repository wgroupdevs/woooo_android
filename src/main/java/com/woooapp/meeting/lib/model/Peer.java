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

    private String mId;
    private String mDisplayName;
    private DeviceInfo mDevice;
    private boolean handRaised = false;
    private boolean camOn = true;
    private boolean micOn = true;
    private Set<String> mConsumers;
    private Set<String> mDataConsumers;

    public Peer(@NonNull JSONObject info) {
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
        return mId;
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

    public boolean isHandRaised() {
        return handRaised;
    }

    public void setHandRaised(boolean handRaised) {
        this.handRaised = handRaised;
    }

    public boolean isCamOn() {
        return camOn;
    }

    public void setCamOn(boolean camOn) {
        this.camOn = camOn;
    }

    public boolean isMicOn() {
        return micOn;
    }

    public void setMicOn(boolean micOn) {
        this.micOn = micOn;
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
