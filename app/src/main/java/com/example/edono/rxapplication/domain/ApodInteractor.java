package com.example.edono.rxapplication.domain;

import com.example.edono.rxapplication.domain.model.ApodData;

import java.util.Calendar;

import io.reactivex.Flowable;

/**
 * Created by edono on 26.01.2017.
 */

public interface ApodInteractor {

    Flowable<ApodData> getApodByDate(Calendar date);

}
