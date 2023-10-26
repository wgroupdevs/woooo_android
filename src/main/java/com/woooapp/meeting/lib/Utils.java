package com.woooapp.meeting.lib;

import java.util.Random;

/**
 * @author Muneeb Ahmad
 * <p>
 * File Utils.java
 * Class [Utils]
 * on 08/09/2023 at 5:13 pm
 */
public class Utils {

    private static final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";
    private static final String ALLOWED_NUMERIC_CHARACTERS = "0123456789";

    /**
     *
     * @param sizeOfRandomString
     * @return
     */
    public static String getRandomString(final int sizeOfRandomString) {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    /**
     *
     * @param limit
     * @return
     */
    public static String getNumericString(final int limit) {
        final Random random = new Random();
        final StringBuilder strBuff = new StringBuilder(limit);
        for (int i = 0; i < limit; i++) {
            strBuff.append(ALLOWED_NUMERIC_CHARACTERS.charAt(random.nextInt(ALLOWED_NUMERIC_CHARACTERS.length())));
        }
        return strBuff.toString();
    }

} /**
 * end class [Utils]
 */
