package com.woooapp.meeting.impl.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.HashSet;
import java.util.Set;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 10:25 am 25/09/2023
 * <code>class</code> WEvents.java
 */
public final class WEvents {
    private static final String TAG = WEvents.class.getSimpleName() + ".java";
    private static WEvents sInstance = null;
    private final Set<Handler> mHandlers = new HashSet<>();
    public static final int EVENT_TYPE_SOCKET_CONNECTED           = 0x01;
    public static final int EVENT_TYPE_SOCKET_DISCONNECTED        = 0x02;
    public static final int EVENT_TYPE_SOCKET_ID                  = 0x03;
    public static final int EVENT_TYPE_MEMBER_ADDED               = 0x04;
    public static final int EVENT_TYPE_PEER_DISCONNECTED          = 0x05;
    public static final int EVENT_TYPE_PRODUCER_CREATED           = 0x06;
    public static final int EVENT_TYPE_SEND_TRANSPORT_CREATED     = 0x07;
    public static final int EVENT_TYPE_CONSUMER_CREATED           = 0x08;
    public static final int EVENT_TYPE_CONSUME_BACK_CREATED       = 0x09;
    public static final int EVENT_TYPE_FETCH_ROOM_DATA            = 0xa;
    public static final int EVENT_RECEIVED_MESSAGE                = 0xb;
    public static final int EVENT_SENT_MESSAGE                    = 0xc;
    public static final int EVENT_FAILURE_MESSAGE                 = 0xd;
    public static final int EVENT_ME_MIC_TURNED_ON                = 0xe;
    public static final int EVENT_ME_MIC_TURNED_OFF               = 0xf;
    public static final int EVENT_ME_CAM_TURNED_ON                = 0x11;
    public static final int EVENT_ME_CAM_TURNED_OFF               = 0x12;
    public static final int EVENT_ENABLE_TEXT_TRANSLATION         = 0x13;
    public static final int EVENT_ON_TEXT_TRANSLATION_RECV        = 0x14;
    public static final int EVENT_CLICKED_LANGUAGE_SELECT         = 0x15;
    public static final int EVENT_DISABLE_EVERYONE_CAM            = 0x16;
    public static final int EVENT_ENABLE_EVERYONE_CAM             = 0x17;
    public static final int EVENT_NEW_PEER_JOINED                 = 0x18;
    public static final int EVENT_PEER_EXITED                     = 0x19;
    public static final int EVENT_ME_HAND_RAISED                  = 0x20;
    public static final int EVENT_ME_HAND_LOWERED                 = 0x21;
    public static final int EVENT_TRANSLATION_ENABLED             = 0x22;
    public static final int EVENT_TRANSLATION_DISABLED            = 0x23;
    public static final int EVENT_PEER_HAND_RAISED                = 0x24;
    public static final int EVENT_PEER_HAND_LOWERED               = 0x25;
    public static final int EVENT_PEER_MIC_MUTED                  = 0x2a;
    public static final int EVENT_PEER_MIC_UNMUTED                = 0x2b;
    public static final int EVENT_PEER_CAM_TURNED_OFF             = 0x2c;
    public static final int EVENT_PEER_CAM_TURNED_ON              = 0x2d;
    public static final int EVENT_SELECT_BACKGROUND               = 0x2e;
    public static final int EVENT_SHOW_MEMBERS                    = 0x2f;
    public static final int EVENT_NETWORK_CONNECTIVITY_CHANGED    = 0x27;
    public static final int EVENT_VOICE_TRANSLATION_RECEIVED      = 0x28;
    public static final int EVENT_ADD_PASSWORD                    = 0x29;
    public static final int EVENT_RECEIVED_MUTE_EVERYONE          = 0x30;
    public static final int EVENT_RECEIVED_MUTE_MEMBER            = 0x31;
    public static final int EVENT_RECEIVED_CAM_OFF_MEMBER         = 0x32;
    public static final int EVENT_RECEIVED_KICKOUT                = 0x33;
//    public static final int EVENT_CREATED_NEW_ADMIN               = 0x34;
    public static final int EVENT_NEW_ADMIN_CREATED               = 0x35;
    public static final int EVENT_ON_NEW_ADMIN                    = 0x3a;
    public static final int EVENT_PEER_ADAPTER_NOTIFY             = 0x3b;
    public static final int EVENT_CONNECTION_STATE_FAILED         = 0x3c;
    public static final int EVENT_CONNECTION_STATE_CONNECTED      = 0x3d;
    public static final int EVENT_CONNECTION_STATE_CONNECTING     = 0x3e;
    public static final int EVENT_DESTROY                         = 0x3f;

    private WEvents() {
        Log.d(TAG, "WooEvents Initialized ...");
        mHandlers.clear();
    }

    /**
     *
     * @param handler
     */
    public void addHandler(@NonNull final Handler handler) {
        mHandlers.add(handler);
        Log.d(TAG, "Added a new Handler [" + handler + "]");
        Log.d(TAG, "Handler Counts >>> " + mHandlers.size());
    }

    /**
     *
     * @param handler
     * @return
     */
    public boolean removeHandler(@NonNull final Handler handler) {
        for (Handler h : mHandlers) {
            if (h.equals(handler)) {
                mHandlers.remove(h);
                Log.d(TAG, "Removed Handler " + handler);
                return true;
            }
        }
        Log.d(TAG, "Can't remove handler " + handler + ", Not found");
        return false;
    }

    /**
     *
     * @param eventType
     * @param msg
     */
    public void notify(int eventType, @NonNull Object msg) {
        try {
            for (Handler h : mHandlers) {
                Message message = Message.obtain(h, eventType, msg);
                message.sendToTarget();
                Log.d(TAG, "Notifying for Event [" + eventType + "]");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void destroy() {
        this.mHandlers.clear();
        sInstance = null;
    }

    /**
     *
     * @return {@link WEvents} as a Singleton
     */
    public static WEvents getInstance() {
        synchronized (WEvents.class) {
            if (sInstance == null) {
                sInstance = new WEvents();
            }
            return sInstance;
        }
    }

} /** end class. */
