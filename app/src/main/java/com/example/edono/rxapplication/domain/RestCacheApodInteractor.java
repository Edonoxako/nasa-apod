package com.example.edono.rxapplication.domain;

import com.example.edono.rxapplication.domain.model.ApodData;
import com.example.edono.rxapplication.util.Constants;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * Created by edono on 28.01.2017.
 */

public class RestCacheApodInteractor implements ApodInteractor {

    private ApodApi api;
    private ApodRepository repository;

    public RestCacheApodInteractor(ApodApi api, ApodRepository repository) {
        this.api = api;
        this.repository = repository;
    }

    @Override
    public Flowable<ApodData> getApodByDate(Calendar date) {
        return repository.findApodByDate(date)
                .switchIfEmpty(saveAndGetFromBackend(date));
    }

    private Flowable<ApodData> saveAndGetFromBackend(Calendar date) {
        SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US);
        String formattedDate = format.format(date.getTime());
        return api.getApodByDate(formattedDate)
                .doOnNext(new Consumer<ApodData>() {
                    @Override
                    public void accept(ApodData apodData) throws Exception {
                        repository.saveApod(apodData);
                    }
                });
    }
}
