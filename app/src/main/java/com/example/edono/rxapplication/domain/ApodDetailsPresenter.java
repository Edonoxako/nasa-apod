package com.example.edono.rxapplication.domain;

import android.util.Log;

import com.example.edono.rxapplication.domain.model.ApodData;
import com.example.edono.rxapplication.domain.model.ApodMediaType;

import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by edono on 14.03.2017.
 */

public class ApodDetailsPresenter {

    private static final String TAG = "ApodDetailsPresenter";
    public static final String MESSAGE_ERROR = "Error during apod loading";

    private ApodDetailsView mView;
    private ApodInteractor mInteractor;

    private Disposable mDisposable;
    private ApodData mCurrentApod;

    public ApodDetailsPresenter(ApodDetailsView view, ApodInteractor interactor) {
        this.mView = view;
        this.mInteractor = interactor;
    }

    public void startLoadingApod() {
        Calendar currentDate = Calendar.getInstance();
        startLoadingApod(currentDate);
    }

    public void startLoadingApod(Calendar date) {
        mDisposable = mInteractor.getApodByDate(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<ApodData>() {
                            @Override
                            public void accept(ApodData apodData) throws Exception {
                                mCurrentApod = apodData;
                                mView.showApod(apodData,
                                        apodData.getMediaType() != ApodMediaType.IMAGE);
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                mCurrentApod = null;
                                Log.e(TAG, MESSAGE_ERROR, throwable);
                                mView.showTextMessage(MESSAGE_ERROR);
                            }
                        });
    }

    public void chooseDate() {
        mView.showCalendar();
    }

    public void release() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    public void toFullscreenImage() {
        if (mCurrentApod != null) {
            mView.showPictureFullscreen(mCurrentApod.getHdUrl());
        } else {
            mView.showPictureFullscreen(null);
        }
    }
}
