package com.example.material_contextmenu_library;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;

public class AnimatorUtils {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static ObjectAnimator rotationCloseToRight(View v) {
        return ObjectAnimator.ofFloat(v, "rotationY", 0, -90);
    }

    public static ObjectAnimator rotationOpenFromRight(View v) {
        return ObjectAnimator.ofFloat(v, "rotationY", -90, 0);
    }

    public static ObjectAnimator rotationCloseVertical(View v) {
        return ObjectAnimator.ofFloat(v, "rotationX", 0, -90);
    }

    public static ObjectAnimator rotationOpenVertical(View v) {
        return ObjectAnimator.ofFloat(v, "rotationX", -90, 0);
    }

    public static ObjectAnimator alfaDisappear(View v) {
        return ObjectAnimator.ofFloat(v, "alpha", 1, 0);
    }

    public static ObjectAnimator alfaAppear(View v) {
        return ObjectAnimator.ofFloat(v, "alpha", 0, 1);
    }

    public static ObjectAnimator translationRight(View v, float x) {
        return ObjectAnimator.ofFloat(v, "translationX", 0, x);
    }
    public static ObjectAnimator translationLeft(View v, float x) {
        return ObjectAnimator.ofFloat(v, "translationX", x, 0);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static AnimatorSet fadeOutSet(View v, float x){
        AnimatorSet fadeOutSet = new AnimatorSet();
        fadeOutSet.playTogether(alfaDisappear(v), translationRight(v,x));
        return fadeOutSet;
    }

}
