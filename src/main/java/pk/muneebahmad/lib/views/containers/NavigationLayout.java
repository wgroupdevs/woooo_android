package pk.muneebahmad.lib.views.containers;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

/**
 * @author muneebahmad (ahmadgallian@yahoo.com)
 * Created On 10:30 am 05/10/2023
 * <code>class</code> NavigationLayout.java
 */
public class NavigationLayout extends FrameLayout {

    private static final String TAG = NavigationLayout.class.getSimpleName() + ".java";

    public NavigationLayout(@NonNull Context context) {
        super(context);
        this.init();
    }

    protected void init() {
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        setBackgroundResource(android.R.color.transparent);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    /**
     *
     * @return
     */
    public boolean isEmpty() {
        return getChildCount() == 0;
    }

    /**
     *
     * @return
     */
    public boolean isNotEmpty() {
        return getChildCount() > 0;
    }

} /** end class. */
