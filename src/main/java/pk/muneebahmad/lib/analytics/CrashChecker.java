package pk.muneebahmad.lib.analytics;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import eu.siacs.conversations.ui.MainActivity;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 */
public final class CrashChecker {
    private static final String TAG = CrashChecker.class.getSimpleName() + ".java";
    public static final String FILE_NAME = "ma_stack.trace";
    private static final String EMAIL = "ahmadgallian@yahoo.com";
    private static final String EMAIL2 = "ahmadgallian@gmail.com";
    private final Activity activity;

    /**
     *
     * @param activity
     */
    public CrashChecker(@NonNull Activity activity) {
        this.activity = activity;
    }

    public void checkForCrash() {
        String dialogMessage = "In the last run, application crashed with an error" +
                ", you can kindly send us the error information to fix this error in future updates.";
        String buttonPositiveText = "Send";
        String buttonNegativeText = "Close";

        String line;
        String trace="";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(activity.openFileInput(FILE_NAME)));
            while((line = reader.readLine()) != null) {
                trace += line+"\n";
            }
        } catch(IOException e) {
            Log.w(TAG, "Can't open stack trace file >> " + e.getMessage());
        }
        if(trace.length() < 10) {
            Log.d(TAG, "No crash recorded ...");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //builder.setIcon(R.drawable.ic_setting);
        builder.setMessage(dialogMessage);
        builder.setCancelable(false);
        String finalTrace = trace;
        builder.setPositiveButton(buttonPositiveText, (dialog, which) -> {
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            String subject = "Error/Crash report";
            String body = "Mail this to developer " + "\n" + finalTrace + "\n";

            sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {EMAIL, EMAIL2});
            sendIntent.putExtra(Intent.EXTRA_TEXT, body);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            sendIntent.setType("message/rfc822");
            activity.startActivity(Intent.createChooser(sendIntent, "Send Report:"));
            activity.deleteFile(FILE_NAME);
        });
        builder.setNegativeButton(buttonNegativeText, (dialog, which) -> {
            activity.deleteFile(FILE_NAME);
            dialog.dismiss();
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

} /** end class. */
