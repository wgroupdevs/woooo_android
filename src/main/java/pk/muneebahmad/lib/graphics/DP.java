package pk.muneebahmad.lib.graphics;

import android.content.Context;
import android.util.TypedValue;

import androidx.annotation.NonNull;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 */
public final class DP {

    /**
     *
     * @param context
     * @param px
     * @return
     */
    public static float valueOf(@NonNull Context context, float px) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, context.getResources().getDisplayMetrics());
    }

} /** end class. */
