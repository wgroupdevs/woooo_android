package com.woooapp.meeting.impl.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;

import com.android.volley.VolleyError;
import com.woogroup.woooo_app.woooo.di.WooApplication;
import com.woooapp.meeting.lib.MeetingClient;
import com.woooapp.meeting.net.ApiManager;
import com.woooapp.meeting.net.models.PutMembersDataBody;

import java.util.UUID;

import javax.annotation.Nullable;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 12:52 pm 13/09/2023
 * <code>class</code> Director.java
 */
public final class WooDirector {

    private static WooDirector sInstance = null;
    private WooDirector() {}

    @SuppressLint("HardwareIds")
    public String getDeviceUUID() {
//        TelephonyManager tm = (TelephonyManager)
//        WooApplication.Companion.getSharedInstance().getSystemService(Context.TELEPHONY_SERVICE);
//        String devId = tm.getImei();
//        String devSerial = tm.getSimSerialNumber();
        String androidId = Settings.Secure.getString(WooApplication.Companion.getSharedInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
        UUID devUUID = new UUID(androidId.hashCode(), (long) System.currentTimeMillis() << 32 | System.nanoTime());
        String uuid = devUUID.toString();
        return uuid;
    }

    /**
     *
     * @param meetingId
     * @param callback
     */
    public void fetchRoomData(
            @NonNull String meetingId,
            @Nullable ApiManager.ApiResult callback) {
       ApiManager.build(WooApplication.Companion.getSharedInstance())
               .fetchRoomData(meetingId,
                       callback != null ? callback : new ApiManager.ApiResult() {
                           @Override
                           public void onResult(Object response) {

                           }

                           @Override
                           public void onFailure(VolleyError error) {

                           }
                       });
    }

    /**
     *
     * @param socketId
     * @param meetingClient
     */
    public void addMember(@NonNull final String socketId, @NonNull MeetingClient meetingClient) {
        PutMembersDataBody body = new PutMembersDataBody();
        body.setEmail(meetingClient.getEmail());
        body.setAccountUniqueId(meetingClient.getAccountUniqueId());
        body.setUsername(meetingClient.getUsername());
        body.setPicture(meetingClient.getPicture());
        body.setSocketId(socketId);
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
