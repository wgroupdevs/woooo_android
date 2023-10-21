package com.woooapp.meeting.impl.views.animations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author Muneeb Ahmad
 * <p>
 * File WooAnimationUtil.java
 * Class [WooAnimationUtil]
 * on 30/09/2023 at 5:57 pm
 */
public final class WooAnimationUtil {

    private static final String TAG = WooAnimationUtil.class.getSimpleName() + ".java";

    /**
     * @param v
     * @param width
     * @param adapter
     */
    public static void openLeftMenu(@NonNull View v, float width, @Nullable AnimatorListenerAdapter adapter) {
        ValueAnimator anim = ObjectAnimator.ofFloat(v, View.TRANSLATION_X, 0);
        anim.setDuration(300);
        if (adapter != null) {
            anim.addListener(adapter);
        }
        anim.start();
    }

    /**
     * @param v
     * @param width
     * @param adapter
     */
    public static void closeLeftMenu(@NonNull final View v, float width, AnimatorListenerAdapter adapter) {
        ValueAnimator anim = ObjectAnimator.ofFloat(v, View.TRANSLATION_X, -width);
        anim.setDuration(300);
        if (adapter != null) {
            anim.addListener(adapter);
        }
        anim.start();
    }

    /**
     * @param view
     * @param width
     */
    public static void setLeftMenuClosed(@NonNull final View view, float width, @Nullable AnimatorListenerAdapter adapter) {
        TranslateAnimation ta = new TranslateAnimation(0f, -width, 0f, 0f);
        ta.setDuration(10);
        ta.setRepeatCount(0);
        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setTranslationX(-width);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(ta);
    }

    /**
     * @param view
     * @param adapter
     */
    public static void showView(@NonNull View view, @Nullable AnimatorListenerAdapter adapter) {
        ValueAnimator scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 0f, 1f);
        scaleX.setDuration(200);
        ValueAnimator scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 0f, 1f);
        scaleY.setDuration(200);
        ValueAnimator alpha = ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f);
        alpha.setDuration(200);
        AnimatorSet set = new AnimatorSet();
        if (adapter != null) {
            set.addListener(adapter);
        }
        set.play(scaleX).with(scaleY).with(alpha);
        set.start();
    }

    /**
     * @param view
     * @param pivotX
     * @param pivotY
     * @param adapter
     */
    public static void showView(@NonNull View view, int pivotX, int pivotY, @Nullable AnimatorListenerAdapter adapter) {
        if (pivotX > -1) {
            view.setPivotX(pivotX);
        }
        if (pivotY > -1) {
            view.setPivotY(pivotY);
        }
        showView(view, adapter);
    }

    public static void hideView(@NonNull View view, @Nullable AnimatorListenerAdapter adapter) {
        ValueAnimator scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 1f, 0f);
        scaleX.setDuration(200);
        ValueAnimator scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1f, 0f);
        scaleY.setDuration(200);
        ValueAnimator alpha = ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 0f);
        alpha.setDuration(200);
        AnimatorSet set = new AnimatorSet();
        if (adapter != null) {
            set.addListener(adapter);
        }
        set.play(scaleX).with(scaleY).with(alpha);
        set.start();
    }

    /**
     * @param view
     * @param pivotX
     * @param pivotY
     * @param adapter
     */
    public static void hideView(@NonNull View view, int pivotX, int pivotY, @Nullable AnimatorListenerAdapter adapter) {
        if (pivotX > -1) {
            view.setPivotX(pivotX);
        }
        if (pivotY > -1) {
            view.setPivotY(pivotY);
        }
        hideView(view, adapter);
    }

    /**
     * @param view
     * @param pivotX
     * @param fromX
     * @param toX
     * @param adapter
     */
    public static void scaleX(@NonNull View view, int pivotX, float fromX, float toX, @Nullable AnimatorListenerAdapter adapter) {
        view.setPivotX(pivotX);
        ValueAnimator scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, fromX, toX);
        scaleX.setDuration(200);
        if (adapter != null) scaleX.addListener(adapter);
        scaleX.start();
    }

    /**
     * @param view
     * @param adapter
     */
    public static void slideFromLeft(@NonNull View view, @Nullable Animation.AnimationListener listener) {
        view.clearAnimation();
        TranslateAnimation anim = new TranslateAnimation(-2000, 0, 0, 0);
        anim.setDuration(400);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setRepeatCount(0);
        if (listener != null) anim.setAnimationListener(listener);
        view.startAnimation(anim);
    }

    /**
     * @param view
     * @param adapter
     */
    public static void slideToLeft(@NonNull View view, @Nullable Animation.AnimationListener listener) {
        view.clearAnimation();
        TranslateAnimation anim = new TranslateAnimation(0, -2000, 0, 0);
        anim.setDuration(400);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setRepeatCount(0);
        if (listener != null) anim.setAnimationListener(listener);
        view.startAnimation(anim);
    }

    /**
     * @param view
     * @param adapter
     */
    public static void slideFromTop(@NonNull View view, @Nullable Animation.AnimationListener listener) {
        view.clearAnimation();
        TranslateAnimation anim = new TranslateAnimation(0, 0, -2000, 0);
        anim.setDuration(400);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setRepeatCount(0);
        if (listener != null) anim.setAnimationListener(listener);
        view.startAnimation(anim);
    }

    /**
     * @param view
     * @param adapter
     */
    public static void slideToTop(@NonNull View view, @Nullable Animation.AnimationListener listener) {
        view.clearAnimation();
        TranslateAnimation anim = new TranslateAnimation(0, 0, 0, -2000);
        anim.setDuration(400);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setRepeatCount(0);
        if (listener != null) anim.setAnimationListener(listener);
        view.startAnimation(anim);
    }

    /**
     * @param view
     * @param adapter
     */
    public static void slideFromBottom(@NonNull View view, @Nullable Animation.AnimationListener listener) {
        view.clearAnimation();
        TranslateAnimation anim = new TranslateAnimation(0, 0, 4000, 0);
        anim.setDuration(400);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setRepeatCount(0);
        if (listener != null) anim.setAnimationListener(listener);
        view.startAnimation(anim);
    }

    /**
     *
     * @param view
     * @param durationMillis
     * @param listener
     */
    public static void slideFromBottom(@NonNull View view, long durationMillis, @Nullable Animation.AnimationListener listener) {
        view.clearAnimation();
        TranslateAnimation anim = new TranslateAnimation(0, 0, 4000, 0);
        anim.setDuration(durationMillis);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setRepeatCount(0);
        if (listener != null) anim.setAnimationListener(listener);
        view.startAnimation(anim);
    }

    /**
     * @param view
     * @param adapter
     */
    public static void slideToBottom(@NonNull View view, @Nullable Animation.AnimationListener listener) {
        view.clearAnimation();
        TranslateAnimation anim = new TranslateAnimation(0, 0, 0, 4000);
        anim.setDuration(400);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setRepeatCount(0);
        if (listener != null) anim.setAnimationListener(listener);
        view.startAnimation(anim);
    }

    /**
     *
     * @param view
     * @param durationMillis
     * @param listener
     */
    public static void slideToBottom(@NonNull View view, long durationMillis, @Nullable Animation.AnimationListener listener) {
        view.clearAnimation();
        TranslateAnimation anim = new TranslateAnimation(0, 0, 0, 4000);
        anim.setDuration(durationMillis);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.setRepeatCount(0);
        if (listener != null) anim.setAnimationListener(listener);
        view.startAnimation(anim);
    }

} /**
 * end class [WooAnimationUtil]
 */
