package com.woooapp.meeting.impl.views.popups;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import eu.siacs.conversations.R;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 3:51 pm 13/10/2023
 * <code>class</code> WooProgressDialog.java
 */
public class WooProgressDialog {

    private final Activity activity;
    private final String text;
    private AlertDialog dialog;


    private WooProgressDialog(@NonNull Activity activity, @NonNull String text) {
        this.text = text;
        this.activity = activity;

        View contentView = LayoutInflater.from(activity).inflate(R.layout.layout_progress_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.TransparentBgDialogStyle);
        builder.setView(contentView);
        ImageView iv = contentView.findViewById(R.id.ivProgress);
        TextView tv = contentView.findViewById(R.id.tvProgress);

        Glide.with(contentView).asGif().load(R.drawable.ic_gif_wooo).into(iv);
        tv.setText(text);

        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    /**
     *
     * @param text
     * @return
     */
    public static WooProgressDialog make(@NonNull Activity activity, @NonNull String text) {
        return new WooProgressDialog(activity, text);
    }

} /** end class. */
