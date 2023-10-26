package pk.muneebahmad.lib.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 4:07 pm 07/10/2023
 * <code>class</code> DateUtils.java
 */
public class DateUtils {

    /**
     *
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
        return format.format(new Date());
    }

} /** end class. */
