package com.example.edono.rxapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.example.edono.rxapplication.domain.ApodFullscreenPresenter;
import com.example.edono.rxapplication.domain.ApodFullscreenView;
import com.example.edono.rxapplication.domain.ApodPresenterFactory;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenPictureActivity extends AppCompatActivity implements ApodFullscreenView{

    public static final String EXTRA_PICTURE_URL = "EXTRA_PICTURE_URL";

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;

    private final Handler mHideHandler = new Handler();
    private ApodFullscreenPresenter mPresenter;

    @BindView(R.id.image_fullscreen) ImageView mImageFullscreen;

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mImageFullscreen.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    };

    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hideSystemControls();
        }
    };

    public static Intent getStartIntent(Context context, URL pictureUrl) {
        Intent intent = new Intent(context, FullscreenPictureActivity.class);
        intent.putExtra(EXTRA_PICTURE_URL, pictureUrl);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_picture);
        ButterKnife.bind(this);

        if (getIntent() == null) {
            finish();
            return;
        }

        mPresenter = ApodPresenterFactory.newFullscreenPresenter(this);
        initImageWithUrl(getIntent());
    }

    private void initImageWithUrl(Intent intent) {
        URL pictureUrl = (URL) intent.getSerializableExtra(EXTRA_PICTURE_URL);

        Picasso picasso = Picasso.with(this);
        RequestCreator requestCreator;
        if (pictureUrl == null) {
            requestCreator = picasso.load(R.drawable.apod_stub_image);
        } else {
            requestCreator = picasso.load(pictureUrl.toString());
        }

        requestCreator.into(mImageFullscreen);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hideSystemControls() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    // Set up the user interaction to manually showSystemControls or hideSystemControls the system UI.
    @OnClick(R.id.image_fullscreen)
    public void toggle() {
        mPresenter.toggle();
    }

    @Override
    public void hideSystemControls() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @Override
    @SuppressLint("InlinedApi")
    public void showSystemControls() {
        // Show the system bar
        mImageFullscreen.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hideSystemControls() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
