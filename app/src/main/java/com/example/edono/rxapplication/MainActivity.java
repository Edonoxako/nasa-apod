package com.example.edono.rxapplication;

import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.TransitionManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edono.rxapplication.domain.ApodPresenter;
import com.example.edono.rxapplication.domain.ApodPresenterFactory;
import com.example.edono.rxapplication.domain.ApodView;
import com.example.edono.rxapplication.domain.model.ApodData;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ApodView {

    private static final String TAG = "MainActivity";

    @BindView(R.id.image_picture) ImageView imagePicture;
    @BindView(R.id.text_title) TextView textTitle;
    @BindView(R.id.text_description) TextView textDescription;
    @BindView(R.id.bar_collapsing) CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.button_show_calendar) FloatingActionButton calendarButton;

    private ApodPresenter mPresenter;

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

        mPresenter = ApodPresenterFactory.newPresenter(this, this);
        mPresenter.startLoadingApod();
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

        CalendarDialog dialog = new CalendarDialog();
        dialog.show(getSupportFragmentManager(), "calendarDialog");
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

    @Override
    public void showTextMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
