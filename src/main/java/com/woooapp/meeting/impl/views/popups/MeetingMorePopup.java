package com.woooapp.meeting.impl.views.popups;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.woooapp.meeting.impl.views.UIManager;
import com.woooapp.meeting.impl.views.animations.WooAnimationUtil;
import com.woooapp.meeting.lib.MeetingClient;

import coil.ComponentRegistry;
import coil.ImageLoader;
import coil.disk.DiskCache;
import coil.memory.MemoryCache;
import coil.request.DefaultRequestOptions;
import coil.request.Disposable;
import coil.request.ImageRequest;
import coil.request.ImageResult;
import eu.siacs.conversations.R;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import pk.muneebahmad.lib.graphics.CircleDrawable;
import pk.muneebahmad.lib.net.Http;
import pk.muneebahmad.lib.net.HttpImageAdapter;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 3:49 pm 02/10/2023
 * <code>class</code> MeetingMorePopup.java
 */
public final class MeetingMorePopup extends RelativeLayout {

    private static final String TAG = MeetingMorePopup.class.getSimpleName() + ".java";
    private final View mContentView;
    private final Context mContext;
    private final RelativeLayout mParent;
    private LinearLayout mMainLayout;
    private final UIManager uiManager;
    private final int mBottomBarHeight;
    private final MeetingClient mClient;
    private Bitmap blurredBg;
    private ImageRequest mImageRequest;

    /**
     *
     * @param context
     * @param parent
     */
    public MeetingMorePopup(
            @NonNull final Context context,
            @NonNull RelativeLayout parent,
            int bottomBarHeight,
            @NonNull MeetingClient client) {
        super(context);
        this.mContext = context;
        this.mParent = parent;
        this.mBottomBarHeight = bottomBarHeight;
        this.mClient = client;
        this.uiManager = UIManager.getUIManager(mContext);
        this.mContentView = LayoutInflater.from(context).inflate(R.layout.meeting_more_popup, null);
        this.setContentView();
    }

    private void setContentView() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        this.setLayoutParams(params);

        this.mMainLayout = mContentView.findViewById(R.id.morePopupContainer);

        final ImageView ivThumb = mContentView.findViewById(R.id.morePopupIvThumb);
        TextView tvName = mContentView.findViewById(R.id.morePopupTvName);

        if (mClient.getPicture() != null) {
            Log.d(TAG, "Loading image >> " + mClient.getPicture());
            Http.build().getImage(mContext, true, mClient.getPicture(), new HttpImageAdapter() {
                @Override
                public void connected(String resource) {}

                @Override
                public void failed(String resource, String reasonPhrase) {}

                @Override
                public void done(String resource, final Bitmap bitmap) {
                    if (bitmap != null) {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            CircleDrawable cd = new CircleDrawable(bitmap);
                            ivThumb.setImageDrawable(cd);
                        });
                    }
                }
            });
        }
        tvName.setText(mClient.getUsername());
    }

    public void show() {
        // Add to parent
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        params.setMargins(10, 0, 10, mBottomBarHeight + 10);
        this.addView(this.mMainLayout, params);
        mParent.addView(this, getLayoutParams());

        WooAnimationUtil.showView(this.mMainLayout, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                blurredBg = uiManager.blur(mContext, uiManager.createBitmap(mParent));
                ImageView ivBg = new ImageView(mContext);
                ivBg.setImageBitmap(blurredBg);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                ivBg.setOnTouchListener((view, motionEvent) -> true);
                addView(ivBg, 0, params);
            }
        });
    }

    public void dismiss() {
        WooAnimationUtil.hideView(this.mMainLayout, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (getChildAt(0) != null) {
                    removeViewAt(0);
                }
                mParent.removeView(MeetingMorePopup.this);
                if (blurredBg != null) {
                    blurredBg.recycle();
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                Log.d(TAG, "<< Hide Animation Started >>");
            }
        });
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (event.getY() < mMainLayout.getY() ||
                    event.getY() > mMainLayout.getY() + mMainLayout.getHeight()) {
                Log.d(TAG, "<< Popup Clicked outside bounds >>");
                dismiss();
                return true;
            }
        }
        return false;
    }

} /** end class. */
