package com.woooapp.meeting.lib.model;

import android.os.Build;

import org.json.JSONObject;

import static com.woooapp.meeting.lib.JsonUtils.jsonPut;

/**
 * @author Muneeb Ahmad
 * <p>
 * File DeviceInfo.java
 * Class [DeviceInfo]
 * on 08/09/2023 at 4:48 pm
 */
public class DeviceInfo {

    private String mFlag;
    private String mName;
    private String mVersion;

    public String getFlag() {
        return mFlag;
    }

    public DeviceInfo setFlag(String flag) {
        this.mFlag = flag;
        return this;
    }

    public String getName() {
        return mName;
    }

    public DeviceInfo setName(String name) {
        this.mName = name;
        return this;
    }

    public String getVersion() {
        return mVersion;
    }

    public DeviceInfo setVersion(String version) {
        this.mVersion = version;
        return this;
    }

    public static DeviceInfo androidDevice() {
        return new DeviceInfo()
                .setFlag("android")
                .setName("Android " + Build.DEVICE)
                .setVersion(Build.VERSION.CODENAME);
    }

    public static DeviceInfo unknownDevice() {
        return new DeviceInfo().setFlag("unknown").setName("unknown").setVersion("unknown");
    }

    public JSONObject toJSONObject() {
        JSONObject deviceInfo = new JSONObject();
        jsonPut(deviceInfo, "flag", getFlag());
        jsonPut(deviceInfo, "name", getName());
        jsonPut(deviceInfo, "version", getVersion());
        return deviceInfo;
    }

} /**
 * end class [DeviceInfo]
 */
