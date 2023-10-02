package com.woooapp.meeting.impl.views.popups;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.woooapp.meeting.impl.views.animations.WooAnimationUtil;

import eu.siacs.conversations.R;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 3:49 pm 02/10/2023
 * <code>class</code> MeetingMorePopup.java
 */
public final class MeetingMorePopup {

    private static final String TAG = MeetingMorePopup.class.getSimpleName() + ".java";
    private final View mContentView;
    private final Context mContext;
    private final RelativeLayout mParent;

    private RelativeLayout mMainContainer;
    private LinearLayout mMainLayout;

    /**
     *
     * @param context
     * @param parent
     */
    public MeetingMorePopup(@NonNull final Context context, @NonNull RelativeLayout parent) {
        this.mContext = context;
        this.mParent = parent;
        this.mContentView = LayoutInflater.from(context).inflate(R.layout.meeting_more_popup, null);

        this.setContentView();
    }

    private void setContentView() {
        this.mMainContainer = new RelativeLayout(mContext);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        this.mMainContainer.setLayoutParams(params);

        this.mMainLayout = mContentView.findViewById(R.id.morePopupContainer);

        this.mMainContainer.setOnClickListener(view -> {
            dismiss();
        });
    }

    public void show() {
        // Add to parent
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        params.setMargins(0, 0, 100, 200);
        this.mMainContainer.addView(this.mMainLayout, params);
        mParent.addView(this.mMainContainer, mMainContainer.getLayoutParams());

        WooAnimationUtil.showView(this.mMainLayout);
    }

    public void dismiss() {
        WooAnimationUtil.hideView(this.mMainLayout, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mParent.removeView(mMainContainer);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                Log.d(TAG, "<< Hide Animation Started >>");
            }
        });
    }

} /** end class. */
