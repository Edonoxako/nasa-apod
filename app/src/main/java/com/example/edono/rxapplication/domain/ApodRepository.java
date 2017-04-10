package com.example.edono.rxapplication.domain;

import java.util.Calendar;

import com.example.edono.rxapplication.domain.model.ApodData;

import io.reactivex.Flowable;

/**
 * Created by edono on 02.02.2017.
 */

public interface ApodRepository {

    Flowable<ApodData> findApodByDate(Calendar date);

    void saveApod(ApodData apodData);
}
