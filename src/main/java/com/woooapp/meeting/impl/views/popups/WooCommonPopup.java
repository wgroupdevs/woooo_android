package com.woooapp.meeting.impl.views.popups;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.woooapp.meeting.impl.views.UIManager;
import com.woooapp.meeting.impl.views.animations.WooAnimationUtil;

import eu.siacs.conversations.R;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 10:43 am 16/10/2023
 * <code>class</code> WooCommonPopupp.java
 */
public class WooCommonPopup extends Popup {

    private static final String TAG = WooCommonPopup.class.getSimpleName() + ".java";
    public static final int VERTICAL_POSITION_TOP = 0x00;
    public static final int VERTICAL_POSITION_CENTER = 0x01;
    public static final int VERTICAL_POSITION_BOTTOM = 0x02;
    private final View mContentView;
    private final UIManager uiManager;
    private RelativeLayout mainContainer;
    private ImageView ivThumb;
    private TextView tvText;
    private String message;
    private int verticalPosition;
    private boolean autoDismiss = true;

    /**
     *
     * @param context
     * @param rootView as on {@link RelativeLayout}
     * @param message
     * @param autoDismiss
     * @param verticalPosition
     */
    public WooCommonPopup(Context context, RelativeLayout rootView, @NonNull String message, boolean autoDismiss, int verticalPosition) {
        super(context, rootView);
        this.mContentView = LayoutInflater.from(getContext()).inflate(R.layout.layout_popup_general, null);
        this.uiManager = UIManager.getUIManager(getContext());
        this.autoDismiss = autoDismiss;
        this.verticalPosition = verticalPosition;
        this.message = message;
        this.initComponents();
    }

    private void initComponents() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setLayoutParams(params);

        this.mainContainer = mContentView.findViewById(R.id.mainContainerPopup);
        setContainer(this.mainContainer);
        this.ivThumb = mContentView.findViewById(R.id.ivPopupThumb);
        this.tvText = mContentView.findViewById(R.id.tvPopupMessage);
    }

    @Override
    public void show() {
//        ivBg.setImageBitmap(blurredBg);
        tvText.setText(message);

        // Add to parent
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        switch (verticalPosition) {
            case VERTICAL_POSITION_BOTTOM:
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                params.setMargins(10, 0, 10, 150);
                break;
            case VERTICAL_POSITION_CENTER:
                params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                params.setMargins(60, 0, 0, 10);
                break;
            case VERTICAL_POSITION_TOP:
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                params.setMargins(10, 60, 10, 0);
                break;
        }
        this.mainContainer.setPadding(5, 5, 5, 5);
        this.mainContainer.setLayoutParams(params);
        this.addView(mainContainer, params);
        getRootView().addView(this, getLayoutParams());
        switch (verticalPosition) {
            case VERTICAL_POSITION_BOTTOM:
                WooAnimationUtil.slideFromBottom(this.mainContainer, null);
                break;
            case VERTICAL_POSITION_CENTER:
                WooAnimationUtil.slideFromLeft(this.mainContainer, null);
                break;
            case VERTICAL_POSITION_TOP:
                WooAnimationUtil.slideFromTop(this.mainContainer, null);
                break;
        }

        if (autoDismiss) {
            new Handler().postDelayed(this::dismiss, 3000);
        }
    }

    @Override
    public void dismiss() {
        Animation.AnimationListener listener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getRootView().removeView(WooCommonPopup.this);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

        switch (verticalPosition) {
            case VERTICAL_POSITION_BOTTOM:
                WooAnimationUtil.slideToBottom(this.mainContainer, listener);
                break;
            case VERTICAL_POSITION_CENTER:
                WooAnimationUtil.slideToLeft(this.mainContainer, listener);
                break;
            case VERTICAL_POSITION_TOP:
                WooAnimationUtil.slideToTop(this.mainContainer, listener);
                break;
        }
    }

} /** end class. */
