package pk.muneebahmad.lib.analytics;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 */
public class UnexpectedExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = UnexpectedExceptionHandler.class.getSimpleName() + ".java";
    private Thread.UncaughtExceptionHandler defaultUEH;
    private Context context = null;

    /**
     *
     * @param context
     */
    public UnexpectedExceptionHandler(Context context) {
        this.context = context;
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        StackTraceElement[] arr = e.getStackTrace();
        String report = e.toString()+"\n\n";
        report += "--------- Stack trace ---------\n\n";
        for (int i=0; i<arr.length; i++) {
            report += "    "+arr[i].toString()+"\n";
        }
        report += "-------------------------------\n\n";
        Log.d(TAG, "Saving crash report ....");

        report += "--------- Cause ---------\n\n";
        Throwable cause = e.getCause();
        if(cause != null) {
            report += cause.toString() + "\n\n";
            arr = cause.getStackTrace();
            for (int i=0; i<arr.length; i++) {
                report += "    "+arr[i].toString()+"\n";
            }
        }
        report += "-------------------------------\n\n";
        try {
            FileOutputStream trace = context.openFileOutput(CrashChecker.FILE_NAME, Context.MODE_PRIVATE);
            trace.write(report.getBytes());
            trace.close();
        } catch(IOException ioe) {
            Log.w(TAG, "Can't Open woo_stack.trace >>>" + ioe.getMessage());
        }
        defaultUEH.uncaughtException(t, e);
    }

} /** end class. */
