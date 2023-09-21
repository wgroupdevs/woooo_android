package com.woooapp.meeting.lib;

import java.util.Locale;

/**
 * @author Muneeb Ahmad
 * <p>
 * File UrlFactory.java
 * Class [UrlFactory]
 * on 08/09/2023 at 5:10 pm
 */
public class UrlFactory {

    private static final String HOSTNAME = "v3demo.mediasoup.org";
//    private static final String HOSTNAME = "wmediasoup.watchblock.net";
    //  private static final String HOSTNAME = "192.168.1.103";
    private static final int PORT = 4443;

    public static String getInvitationLink(String roomId, boolean forceH264, boolean forceVP9) {
        String url = String.format(Locale.US, "https://%s/?roomId=%s", HOSTNAME, roomId);
//        String url = String.format(Locale.US, "https://%s/%s", HOSTNAME, roomId);
        if (forceH264) {
            url += "&forceH264=true";
        } else if (forceVP9) {
            url += "&forceVP9=true";
        }
        return url;
    }

    public static String getProtooUrl(
            String roomId, String peerId, boolean forceH264, boolean forceVP9) {
        String url =
                String.format(
                        Locale.US, "wss://%s:%d/?roomId=%s&peerId=%s", HOSTNAME, PORT, roomId, peerId);
//        String.format(Locale.US, "wss://%s:%d/%s", HOSTNAME, PORT, roomId);
        if (forceH264) {
            url += "&forceH264=true";
        } else if (forceVP9) {
            url += "&forceVP9=true";
        }
        return url;
    }

} /**
 * end class [UrlFactory]
 */
