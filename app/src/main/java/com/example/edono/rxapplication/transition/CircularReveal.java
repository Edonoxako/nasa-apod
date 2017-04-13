package com.example.edono.rxapplication.transition;

import android.animation.Animator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.transition.Transition;
import android.support.transition.TransitionValues;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

/**
 * Created by Eugeny.Martinenko on 12.04.2017.
 */

public class CircularReveal extends Transition {

    public static final String PROPNAME_VISIBILITY =
            "com.example.edono.rxapplication.transition:circular_reveal:visibility";

    @Override
    public void captureEndValues(@NonNull TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public void captureStartValues(@NonNull TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Nullable
    @Override
    public Animator createAnimator(@NonNull ViewGroup sceneRoot, @Nullable TransitionValues startValues, @Nullable TransitionValues endValues) {
        if (startValues == null || endValues == null) return null;

        View view = endValues.view;

        Integer startVisibility = (Integer) startValues.values.get(PROPNAME_VISIBILITY);
        Integer endVisibility = (Integer) endValues.values.get(PROPNAME_VISIBILITY);

        float startRadius = (float) view.getWidth() / 2;
        float finalRadius = (float) Math.hypot(sceneRoot.getWidth(), sceneRoot.getHeight());

        // get the center for the clipping circle
        int cx = (sceneRoot.getLeft() + sceneRoot.getRight()) / 2;
        int cy = (sceneRoot.getTop() + sceneRoot.getBottom()) / 2;

        if ((startVisibility.equals(View.INVISIBLE) || startVisibility.equals(View.GONE)) && endVisibility.equals(View.VISIBLE)) {
            return ViewAnimationUtils.createCircularReveal(sceneRoot, cx, cy, startRadius, finalRadius);
        }

        return null;
    }

    private void captureValues(TransitionValues transitionValues) {
        transitionValues.values.put(PROPNAME_VISIBILITY, transitionValues.view.getVisibility());
    }
}
