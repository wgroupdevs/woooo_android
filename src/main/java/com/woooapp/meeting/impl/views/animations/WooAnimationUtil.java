package com.woooapp.meeting.impl.views.animations;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

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
     *
     * @param v
     * @param width
     */
    public static void openLeftMenu(@NonNull View v, float width) {
        ValueAnimator anim = ObjectAnimator.ofFloat(v, View.TRANSLATION_X, 0);
        anim.setDuration(400);
        anim.start();
    }

    /**
     *
     * @param v
     * @param width
     */
    public static void closeLeftMenu(@NonNull final View v, float width) {
        ValueAnimator anim = ObjectAnimator.ofFloat(v, View.TRANSLATION_X, -width);
        anim.setDuration(400);
        anim.start();
    }

    public static void showView(@NonNull View view) {
        ValueAnimator scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 0f, 1f);
        scaleX.setDuration(200);
        ValueAnimator scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 0f, 1f);
        scaleY.setDuration(200);
        ValueAnimator alpha = ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f);
        alpha.setDuration(200);
        AnimatorSet set = new AnimatorSet();
        set.play(scaleX).with(scaleY).with(alpha);
    }

} /** end class [WooAnimationUtil] */
