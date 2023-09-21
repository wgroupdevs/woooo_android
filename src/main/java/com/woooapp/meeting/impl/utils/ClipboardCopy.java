package com.woooapp.meeting.impl.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

/**
 * @author Muneeb Ahmad
 * <p>
 * File ClipboardCopy.java
 * Class [ClipboardCopy]
 * on 08/09/2023 at 8:18 pm
 */
public class ClipboardCopy {

    public static void clipboardCopy(Context context, String content, int tipsResId) {
        ClipboardManager clipboard =
                (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", content);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, tipsResId, Toast.LENGTH_SHORT).show();
    }

} /**
 * end class [ClipboardCopy]
 */
