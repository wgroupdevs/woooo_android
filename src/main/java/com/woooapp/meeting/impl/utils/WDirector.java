package com.woooapp.meeting.impl.utils;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

import com.woooapp.meeting.lib.MeetingClient;
import com.woooapp.meeting.lib.model.Peer;
import com.woooapp.meeting.net.models.RoomData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import eu.siacs.conversations.WooApp;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 12:52 pm 13/09/2023
 * <code>class</code> WDirector.java
 */
public final class WDirector {

    private static final String TAG = WDirector.class.getSimpleName() + ".java";
    private static WDirector sInstance = null;
    private final Map<String, Boolean> videoStateMap = new LinkedHashMap<>();
    private RoomData roomData;
    private boolean chatTranslationEnabled = false;
    private WDirector() {
        this.videoStateMap.clear();
    }

    @SuppressLint("HardwareIds")
    public String getDeviceUUID() {
//        TelephonyManager tm = (TelephonyManager)
//        eu.siacs.conversations.WooApplication.Companion.getSharedInstance().getSystemService(Context.TELEPHONY_SERVICE);
//        String devId = tm.getImei();
//        String devSerial = tm.getSimSerialNumber();
        String androidId = Settings.Secure.getString(WooApp.Companion.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
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
     * @param client
     * @param accountUniqueId
     */
    public void updateRole(@NonNull MeetingClient client, @NonNull String accountUniqueId) {
       if (this.roomData != null) {
           List<RoomData.Admin> admins = roomData.getAdmins();
           if (admins != null) {
               for (RoomData.Admin admin : roomData.getAdmins()) {
                   if (admin.getAccountUniqueId().equals(accountUniqueId)) {
                       client.setRole(MeetingClient.Role.ADMIN);
                       WEvents.getInstance().notify(WEvents.EVENT_PEER_ADAPTER_NOTIFY, true);
                       break;
                   }
               }
           }
       }
    }

    /**
     *
     * @param peerList
     * @return
     */
    public List<List<Peer>> getPeerPaginatedLists(@NonNull List<Peer> peerList) {
        Log.d(TAG, "Peer List size in Director >> " + peerList.size());
        List<List<Peer>> paginatedList = Collections.synchronizedList(new LinkedList<>());
        if (peerList.size() <= 5) {
            paginatedList.add(peerList);
            Log.d(TAG, "<< 1st List added to paginatedList ....");
        } else if (peerList.size() > 5 && peerList.size() <= 11) {
            paginatedList.add(peerList.subList(0, 5));
            paginatedList.add(peerList.subList(5, peerList.size()));
            Log.d(TAG, "<< 2nd List added to paginatedList ....");
        } else if (peerList.size() > 11 && peerList.size() <= 17) {
            paginatedList.add(peerList.subList(0, 5));
            paginatedList.add(peerList.subList(5, 11));
            paginatedList.add(peerList.subList(11, peerList.size()));
        } else if (peerList.size() > 17 && peerList.size() <= 23) {
            paginatedList.add(peerList.subList(0, 5));
            paginatedList.add(peerList.subList(5, 11));
            paginatedList.add(peerList.subList(11, 17));
            paginatedList.add(peerList.subList(17, peerList.size()));
        } else if (peerList.size() > 23 && peerList.size() <= 29) {
            paginatedList.add(peerList.subList(0, 5));
            paginatedList.add(peerList.subList(5, 11));
            paginatedList.add(peerList.subList(11, 17));
            paginatedList.add(peerList.subList(17, 23));
            paginatedList.add(peerList.subList(23, peerList.size()));
        } else if (peerList.size() > 29 && peerList.size() <= 35) {
            paginatedList.add(peerList.subList(0, 5));
            paginatedList.add(peerList.subList(5, 11));
            paginatedList.add(peerList.subList(11, 17));
            paginatedList.add(peerList.subList(17, 23));
            paginatedList.add(peerList.subList(23, 29));
            paginatedList.add(peerList.subList(29, peerList.size()));
        } else if (peerList.size() > 35 && peerList.size() <= 41) {
            paginatedList.add(peerList.subList(0, 5));
            paginatedList.add(peerList.subList(5, 11));
            paginatedList.add(peerList.subList(11, 17));
            paginatedList.add(peerList.subList(17, 23));
            paginatedList.add(peerList.subList(23, 29));
            paginatedList.add(peerList.subList(29, 35));
            paginatedList.add(peerList.subList(35, peerList.size()));
        } else if (peerList.size() > 41 && peerList.size() <= 47) {
            paginatedList.add(peerList.subList(0, 5));
            paginatedList.add(peerList.subList(5, 11));
            paginatedList.add(peerList.subList(11, 17));
            paginatedList.add(peerList.subList(17, 23));
            paginatedList.add(peerList.subList(23, 29));
            paginatedList.add(peerList.subList(29, 35));
            paginatedList.add(peerList.subList(35, 41));
            paginatedList.add(peerList.subList(41, peerList.size()));
        } else if (peerList.size() > 47 && peerList.size() <= 53) {
            paginatedList.add(peerList.subList(0, 5));
            paginatedList.add(peerList.subList(5, 11));
            paginatedList.add(peerList.subList(11, 17));
            paginatedList.add(peerList.subList(17, 23));
            paginatedList.add(peerList.subList(23, 29));
            paginatedList.add(peerList.subList(29, 35));
            paginatedList.add(peerList.subList(35, 41));
            paginatedList.add(peerList.subList(41, 47));
            paginatedList.add(peerList.subList(47, peerList.size()));
        } else if (peerList.size() > 53 && peerList.size() <= 59) {
            paginatedList.add(peerList.subList(0, 5));
            paginatedList.add(peerList.subList(5, 11));
            paginatedList.add(peerList.subList(11, 17));
            paginatedList.add(peerList.subList(17, 23));
            paginatedList.add(peerList.subList(23, 29));
            paginatedList.add(peerList.subList(29, 35));
            paginatedList.add(peerList.subList(35, 41));
            paginatedList.add(peerList.subList(41, 47));
            paginatedList.add(peerList.subList(47, 53));
            paginatedList.add(peerList.subList(53, peerList.size()));
        }
        return paginatedList;
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
    public static WDirector getInstance() {
        synchronized (WDirector.class) {
            if (sInstance == null) {
                sInstance = new WDirector();
            }
            return sInstance;
        }
    }

} /** end class. */
