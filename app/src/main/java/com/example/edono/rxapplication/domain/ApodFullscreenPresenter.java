package com.example.edono.rxapplication.domain;

/**
 * Created by Eugeny.Martinenko on 14.04.2017.
 */

public class ApodFullscreenPresenter {

    private ApodFullscreenView mView;

    private boolean mVisible = true;

    public ApodFullscreenPresenter(ApodFullscreenView mView) {
        this.mView = mView;
    }

    public void toggle() {
        if (mVisible) {
            mView.hideSystemControls();
            mVisible = false;
        } else {
            mView.showSystemControls();
            mVisible = true;
        }
    }

}
