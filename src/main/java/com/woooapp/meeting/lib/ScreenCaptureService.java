package com.woooapp.meeting.lib;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.woooapp.meeting.impl.views.MeetingActivity;

import eu.siacs.conversations.R;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 9:58 am 20/11/2023
 * <code>class</code> ScreenCaptureService.java
 */
public class ScreenCaptureService extends Service {

    private static final String TAG = ScreenCaptureService.class.getSimpleName() + ".java";
    private IBinder serviceBinder = new ScreenCaptureServiceBinder();
    private NotificationManagerCompat notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "<< Screen Capture Service onCreate()");
        Intent intent1 = new Intent(this, MeetingActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0x9c, intent1,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ? PendingIntent.FLAG_IMMUTABLE : PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId = "Wooo_Screen_capture";

        NotificationChannelCompat channelCompat =
                new NotificationChannelCompat.Builder(channelId, NotificationManagerCompat.IMPORTANCE_DEFAULT)
                        .setName("Woooo Meeting")
                        .setDescription("Meeting in progress")
                        .build();
        notificationManager = NotificationManagerCompat.from(this);
        notificationManager.createNotificationChannel(channelCompat);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_screen_share_blue_34)
                .setContentTitle("Woooo Casting Screen ...")
                .setContentText("Your Screen was shared with other members.")
                .setCategory(Notification.CATEGORY_SERVICE)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.woooo_logo))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(0x1cf, builder.build(), ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION);
        } else {
            startForeground(0x1cf, builder.build());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "<< Screen Capture Service onDestroy() called.");
        stopForeground(true);
        stopSelf();
        super.onDestroy();
    }

    /**
     *
     */
    public class ScreenCaptureServiceBinder extends Binder {

        public ScreenCaptureService getService() {
            return ScreenCaptureService.this;
        }

    } // end class

} /** end class. */
