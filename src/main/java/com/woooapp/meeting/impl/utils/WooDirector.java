package com.woooapp.meeting.impl.utils;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.provider.Settings;

import androidx.annotation.NonNull;

import com.woooapp.meeting.net.models.RoomData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import eu.siacs.conversations.WooApplication;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 12:52 pm 13/09/2023
 * <code>class</code> Director.java
 */
public final class WooDirector {

    private static WooDirector sInstance = null;
    private final Map<String, Boolean> videoStateMap = new LinkedHashMap<>();
    private RoomData roomData;
    private boolean chatTranslationEnabled = false;
    private WooDirector() {
        this.videoStateMap.clear();
    }

    @SuppressLint("HardwareIds")
    public String getDeviceUUID() {
//        TelephonyManager tm = (TelephonyManager)
//        eu.siacs.conversations.WooApplication.Companion.getSharedInstance().getSystemService(Context.TELEPHONY_SERVICE);
//        String devId = tm.getImei();
//        String devSerial = tm.getSimSerialNumber();
        String androidId = Settings.Secure.getString(WooApplication.Companion.getSharedInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
        UUID devUUID = new UUID(androidId.hashCode(), (long) System.currentTimeMillis() << 32 | System.nanoTime());
        String uuid = devUUID.toString();
        return uuid;
    }

    /**
     *
     * @param pId
     * @param videoOn
     */
    public void addPeerVideoState(String pId, boolean videoOn) {
        videoStateMap.put(pId, videoOn);
    }

    /**
     *
     * @param pId
     * @return
     */
    public boolean isCameraOn(@NonNull String pId) {
        if (videoStateMap.get(pId) != null) {
            return videoStateMap.get(pId);
        }
        return true;
    }

    /**
     *
     * @param asset
     * @param filename
     * @return
     */
    @Nullable
    public String readFileFromAssets(@NonNull AssetManager asset, @NonNull String filename) {
        StringBuilder strBuff = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(asset.open(filename), "UTF-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                strBuff.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return strBuff.toString();
    }

    /**
     *
     * @return
     */
    @Nullable
    public RoomData getRoomData() {
        return roomData;
    }

    /**
     *
     * @param roomData
     */
    public void setRoomData(RoomData roomData) {
        this.roomData = roomData;
    }

    /**
     *
     * @return
     */
    public boolean isChatTranslationEnabled() {
        return chatTranslationEnabled;
    }

    /**
     *
     * @param chatTranslationEnabled
     */
    public void setChatTranslationEnabled(boolean chatTranslationEnabled) {
        this.chatTranslationEnabled = chatTranslationEnabled;
    }

    public void dispose() {
        if (this.videoStateMap != null) {
            this.videoStateMap.clear();
        }
    }

    /**
     *
     * @return
     */
    public static WooDirector getInstance() {
        synchronized (WooDirector.class) {
            if (sInstance == null) {
                sInstance = new WooDirector();
            }
            return sInstance;
        }
    }

} /** end class. */
