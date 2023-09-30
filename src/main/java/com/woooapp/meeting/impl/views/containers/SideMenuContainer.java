package com.woooapp.meeting.impl.views.containers;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * @author Muneeb Ahmad
 * <p>
 * File SideMenuContiner.java
 * Class [SideMenuContainer]
 * on 30/09/2023 at 5:48 pm
 */
public class SideMenuContainer extends RelativeLayout {

    private static final String TAG = SideMenuContainer.class.getSimpleName() + ".java";
    private static final int LEFT_TOLERANCE = 25;
    private static final double PERCENT_OF_SCROLL_TO_FINISH     = 0.2d;

    private boolean isDragging = false;
    private int startX;
    private int currentX;

    public SideMenuContainer(Context context) {
        super(context);
    }

    public SideMenuContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideMenuContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SideMenuContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!isDragging) {
            if (event.getAction() == MotionEvent.ACTION_DOWN && event.getX() < LEFT_TOLERANCE) {
                isDragging = true;
                return true;
            }
            return false;
        }
        switch (event.getAction()) {

        }
        return super.onInterceptTouchEvent(event);
    }

} /** end class [SideMenuContainer] */
