package com.example.edono.rxapplication.coordinator;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by edono on 07.04.2017.
 */

public class ScrollAwareFabBehaviour extends FloatingActionButton.Behavior {

    private static final String TAG = "ScrollAwareFabBehaviour";

    public ScrollAwareFabBehaviour(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
                                       FloatingActionButton child,
                                       View directTargetChild,
                                       View target,
                                       int nestedScrollAxes) {
        Log.d(TAG, "onStartNestedScroll() called with: nestedScrollAxes = [" + nestedScrollAxes + "]");
        return (nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL) ||
                super.onStartNestedScroll(coordinatorLayout,
                        child,
                        directTargetChild,
                        target,
                        nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout,
                               FloatingActionButton child,
                               View target,
                               int dxConsumed,
                               int dyConsumed,
                               int dxUnconsumed,
                               int dyUnconsumed) {

        Log.d(TAG, "onNestedScroll() called with: dyConsumed = [" + dyConsumed + "], dyUnconsumed = [" + dyUnconsumed + "]");

        super.onNestedScroll(coordinatorLayout,
                child,
                target,
                dxConsumed,
                dyConsumed,
                dxUnconsumed,
                dyUnconsumed);

        if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
            child.hide();
        } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
            child.show();
        }
    }

}
