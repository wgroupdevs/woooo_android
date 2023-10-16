package com.woooapp.meeting.impl.views.popups;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 5:27 pm 12/10/2023
 * <code>class</code> Popup.java
 */
public abstract class Popup extends RelativeLayout {

    private static final String TAG = Popup.class.getSimpleName() + ".java";
    private final Context mContext;
    private final RelativeLayout mRootView;
    private RelativeLayout mContainer;

    /**
     *
     * @param context
     * @param rootView
     */
    public Popup(Context context, RelativeLayout rootView) {
        super(context);
        this.mContext = context;
        this.mRootView = rootView;
    }

    public abstract void show();

    public abstract void dismiss();

    /**
     *
     * @return
     */
    @Nullable
    public RelativeLayout getContainer() {
        return mContainer;
    }

    /**
     *
     * @param mContainer
     */
    public void setContainer(@NonNull RelativeLayout mContainer) {
        this.mContainer = mContainer;
    }

    /**
     *
     * @return
     */
    public RelativeLayout getRootView() {
        return this.mRootView;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (getContainer() != null) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getY() < getContainer().getY() ||
                        event.getY() > getContainer().getY() + getContainer().getHeight()) {
                    Log.d(TAG, "<< Popup Clicked outside bounds >>");
                    dismiss();
                    return true;
                }
            }
        }
        return false;
    }

} /** end class. */
