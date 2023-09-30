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
 * <code>class</code> WooEvents.java
 */
public final class WooEvents {
    private static final String TAG = WooEvents.class.getSimpleName() + ".java";
    private static WooEvents sInstance = null;
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

    private WooEvents() {
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
        for (Handler h : mHandlers) {
            Message message = Message.obtain(h, eventType, msg);
            message.sendToTarget();
            Log.d(TAG, "Notifying for Event [" + eventType + "]");
        }
    }

    /**
     *
     * @return {@link WooEvents} as a Singleton
     */
    public static WooEvents getInstance() {
        synchronized (WooEvents.class) {
            if (sInstance == null) {
                sInstance = new WooEvents();
            }
            return sInstance;
        }
    }

} /** end class. */
