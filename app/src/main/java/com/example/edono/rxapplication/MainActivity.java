package com.example.edono.rxapplication;

import android.animation.Animator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edono.rxapplication.domain.ApodDetailsPresenter;
import com.example.edono.rxapplication.domain.ApodPresenterFactory;
import com.example.edono.rxapplication.domain.ApodDetailsView;
import com.example.edono.rxapplication.domain.model.ApodData;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import java.net.URL;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ApodDetailsView {

    private static final String TAG = "MainActivity";

    @BindView(R.id.activity_main) CoordinatorLayout root;
    @BindView(R.id.image_picture) ImageView imagePicture;
    @BindView(R.id.text_title) TextView textTitle;
    @BindView(R.id.text_description) TextView textDescription;
    @BindView(R.id.bar_collapsing) CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.button_show_calendar) FloatingActionButton calendarButton;
    @BindView(R.id.view_reveal_pane) View revealPane;

    private ApodDetailsPresenter mPresenter;

    private Target mTarget;
    private Picasso mPicasso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_apod_details);

        ButterKnife.bind(this);

        initPicasso();
        initPicassoTarget();

        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.chooseDate();
            }
        });

        mPresenter = ApodPresenterFactory.newDetailsPresenter(this, this);
        mPresenter.startLoadingApod();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CalendarActivity.REQUEST_CODE && data != null) {
            Calendar date = CalendarActivity.extractResult(data);
            mPresenter.startLoadingApod(date);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        super.onStop();
        revealPane.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.release();
    }

    @Override
    public void showApod(ApodData apodData, boolean useStubImage) {
        Log.d(TAG, "showApod: " + apodData);
        textTitle.setText(apodData.getTitle());
        textDescription.setText(apodData.getExplanation());

        RequestCreator requestCreator;
        if (useStubImage) {
            requestCreator = mPicasso.load(R.drawable.apod_stub_image);
        } else {
            requestCreator = mPicasso.load(apodData.getUrl().toString());
        }

        requestCreator.into(mTarget);
    }

    @Override
    public void showCalendar() {
        animateReveal();
        new Handler()
                .postDelayed(new Runnable() {
            @Override
            public void run() {
                goToCalendar();
            }
        }, 200);
    }

    @Override
    public void showTextMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.image_picture)
    public void onImageClick() {
        mPresenter.toFullscreenImage();
    }

    @Override
    public void showPictureFullscreen(URL pictureUrl) {
        Intent startIntent = FullscreenPictureActivity.getStartIntent(this, pictureUrl);

        Bundle apodPictureOptions = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, imagePicture, imagePicture.getTransitionName())
                .toBundle();

        startActivity(startIntent, apodPictureOptions);
    }

    private void animateReveal() {
        revealPane.setVisibility(View.VISIBLE);

        int cx = root.getWidth();
        int cy = root.getHeight();

        int startX = (int) (calendarButton.getWidth() / 2 + calendarButton.getX());
        int startY = (int) (calendarButton.getWidth() / 2 + calendarButton.getY());

        float finalRadius = Math.max(cx, cy) * 1.2f;

        Animator reveal = ViewAnimationUtils
                .createCircularReveal(revealPane, startX, startY, calendarButton.getWidth(), finalRadius);

        reveal.setDuration(350);
        reveal.start();
    }

    private void goToCalendar() {
        Intent intent = new Intent(this, CalendarActivity.class);
        ActivityOptions activityOptions = ActivityOptions
                .makeSceneTransitionAnimation(this);

        startActivityForResult(intent, CalendarActivity.REQUEST_CODE, activityOptions.toBundle());
    }

    private void setStatusBarScrimFromImage(Bitmap bitmap) {
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int darkMutedColor =
                        palette.getDarkMutedColor(getResources().getColor(R.color.colorPrimaryDark));
                collapsingToolbar.setStatusBarScrim(new ColorDrawable(darkMutedColor));
            }
        });
    }

    private void initPicassoTarget() {
        mTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                setStatusBarScrimFromImage(bitmap);
                imagePicture.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                imagePicture.setImageDrawable(errorDrawable);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                imagePicture.setImageDrawable(placeHolderDrawable);
            }
        };
    }

    private void initPicasso() {
        mPicasso = new Picasso.Builder(this)
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        Log.e(TAG, "onImageLoadFailed: fail loading image", exception);
                    }
                })
                .build();
    }
}
