package com.woooapp.meeting.device;

import android.content.Context;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 2:15 pm 26/09/2023
 * <code>class</code> Display.java
 */
public final class Display {

    private Display() {}

    /**
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dpToPx(final Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    /**
     *
     * @param context
     * @param px
     * @return
     */
    public static int pxToDp(final Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    /**
     *
     * @param context
     * @return display width in pixels as <code>int</code>
     */
    public static int getDisplayWidth(@NonNull final Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     *
     * @param context
     * @return display height in pixels as <code>int</code>
     */
    public static int getDisplayHeight(@NonNull final Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

} /** end class. */
