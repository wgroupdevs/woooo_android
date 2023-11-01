package com.woooapp.meeting.lib.model;

/**
 * @author Muneeb Ahmad
 * <p>
 * File Info.java
 * Class [Info]
 * on 08/09/2023 at 4:49 pm
 */
public class Info {

    private boolean micOn = true;
    private boolean camOn = false;
    private boolean handRaised = false;

    private String displayPicture;

    public String getId() {
        return "";
    }

    public String getDisplayName() {
        return "";
    }

    public DeviceInfo getDevice() {
        return DeviceInfo.androidDevice();
    }

    public boolean isMicOn() {
        return micOn;
    }

    public void setMicOn(boolean micOn) {
        this.micOn = micOn;
    }

    public boolean isCamOn() {
        return camOn;
    }

    public void setCamOn(boolean camOn) {
        this.camOn = camOn;
    }

    public boolean isHandRaised() {
        return handRaised;
    }

    public void setHandRaised(boolean handRaised) {
        this.handRaised = handRaised;
    }

    public String getDisplayPicture() {
        return displayPicture;
    }

    public void setDisplayPicture(String displayPicture) {
        this.displayPicture = displayPicture;
    }
} /**
 * end class [Info]
 */
