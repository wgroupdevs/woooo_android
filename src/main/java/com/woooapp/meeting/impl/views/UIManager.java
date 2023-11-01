package com.woooapp.meeting.impl.views;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.renderscript.Allocation;
import androidx.renderscript.Element;
import androidx.renderscript.RenderScript;
import androidx.renderscript.ScriptIntrinsicBlur;

import java.util.List;

import eu.siacs.conversations.BuildConfig;
import eu.siacs.conversations.R;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 10:39 am 03/10/2023
 * <code>class</code> UIManager.java
 */
public final class UIManager {

    private static final String TAG = UIManager.class.getSimpleName() + ".java";
    private final Context mContext;

    private UIManager(Context context) {
        this.mContext = context;
    }

    /**
     *
     * @param view
     * @return
     */
    public Bitmap createBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        Drawable backgroundDrawable = view.getBackground();
        if (backgroundDrawable != null) {
            backgroundDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.parseColor("#80000000"));
        }
        view.draw(canvas);
        return bitmap;
    }

    /**
     *
     * @param context
     * @param image
     * @return
     */
    public Bitmap blur(Context context, Bitmap image) {
        float BITMAP_SCALE = 2f;
        float BLUR_RADIUS = 20f;
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);
        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }

    /**
     * @param context
     * @param title
     * @param message
     * @param buttonText
     * @param iconRes
     * @param callback
     */
    public static void showErrorDialog(
            @NonNull Context context,
            @NonNull String title,
            @NonNull String message,
            @NonNull String buttonText,
            int iconRes,
            @Nullable DialogCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.TransparentBgDialogStyle);
        View v = LayoutInflater.from(context).inflate(R.layout.layout_dialog_error, null);
        ImageView ivIcon = v.findViewById(R.id.errorDialogIconIv);
        TextView tvTitle = v.findViewById(R.id.errorDialogTitleTv);
        TextView tvMessage = v.findViewById(R.id.errorDialogMessageTv);
        Button buttonOk = v.findViewById(R.id.errorDialogPositiveButton);

        if (iconRes != -1) {
            ivIcon.setImageResource(iconRes);
        }
        tvTitle.setText(title);
        tvMessage.setText(message);
        buttonOk.setText(buttonText);

        builder.setView(v);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        buttonOk.setOnClickListener(view -> {
            if (callback != null) {
                callback.onPositiveButton(view, null);
            }
            dialog.dismiss();
        });
    }

    /**
     *
     * @param activity
     * @param title
     * @param messages
     * @param cancellable
     * @param intentClass
     */
    public void sendNotification(@NonNull Activity activity,
                                 @NonNull String title,
                                 @NonNull List<String> messages,
                                 boolean cancellable,
                                 @NonNull Class<?> intentClass) {
        Intent intent = new Intent(activity, intentClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS);
        intent.putExtra("notification", true);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(activity);
        stackBuilder.addParentStack(intentClass);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0x9f, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(BuildConfig.APP_NAME);
        for (String message : messages) {
            inboxStyle.addLine(message);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity, activity.getClass().getSimpleName().toUpperCase());
        builder.setSmallIcon(R.drawable.woooo_logo);
        builder.setLargeIcon(BitmapFactory.decodeResource(activity.getResources(), R.drawable.woooo_logo));
        builder.setContentTitle("Woooo Meeting in Progress");
        builder.setStyle(inboxStyle);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(false);
        builder.setCategory(Notification.CATEGORY_CALL);
        NotificationManagerCompat nm = NotificationManagerCompat.from(activity);

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Notification permission not granted ....");
            return;
        }
        nm.notify(0x9c, builder.build());
    }

    public void cancelNotification(@NonNull Activity activity) {
        NotificationManagerCompat nm = NotificationManagerCompat.from(activity);
        nm.cancel(0x9c);
    }

    public interface DialogCallback {
        void onPositiveButton(@Nullable Object sender, @Nullable Object data);

        void onNeutralButton(@Nullable Object sender, @Nullable Object data);

        void onNegativeButton(@Nullable Object sender, @Nullable Object data);
    } // end interface

    /**
     *
     * @param context
     * @return
     */
    public static UIManager getUIManager(@NonNull final Context context) {
        synchronized (UIManager.class) {
            return new UIManager(context);
        }
    }

} /**
 * end class.
 */
