package com.woooapp.meeting.lib;

import java.util.Locale;

/**
 * @author Muneeb Ahmad (ahmadgallian@yahoo.com)
 * <p>
 * Class UrlFactory2.java
 * Created on 15/09/2023 at 2:29 am
 */
public class UrlFactory2 {
        private static final String HOSTNAME = "wmediasoup.watchblock.net/";
    //  private static final String HOSTNAME = "192.168.1.103";
    private static final int PORT = 4443;

    public static String getInvitationLink() {
        String url = String.format(Locale.US, "https://%s", HOSTNAME);
        return url;
    }

    public static String getProtooUrl() {
        String url = String.format("https://%s", HOSTNAME);
//        String.format(Locale.US, "wss://%s:%d/%s", HOSTNAME, PORT, roomId);
        return url;
    }

} /** end class. */
