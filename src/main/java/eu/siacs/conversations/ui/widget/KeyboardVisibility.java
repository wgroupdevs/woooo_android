package eu.siacs.conversations.ui.widget;

import android.app.Activity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.graphics.Rect;


 public class KeyboardVisibility {

    public interface KeyboardVisibilityListener {
        void onKeyboardVisibilityChanged(boolean keyboardVisible);
    }

    public static void setEventListener(Activity activity, final KeyboardVisibilityListener listener) {
        final View rootView = activity.findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            private int previousHeight = rootView.getHeight();

            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int heightDiff = rootView.getRootView().getHeight() - r.height();
                boolean isVisible = heightDiff > rootView.getRootView().getHeight() * 0.15;

                if (isVisible != (previousHeight < r.height())) {
                    listener.onKeyboardVisibilityChanged(isVisible);
                }

                previousHeight = r.height();
            }
        });
    }
}