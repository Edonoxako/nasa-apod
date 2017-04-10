package com.example.edono.rxapplication.domain;

import com.example.edono.rxapplication.domain.model.ApodData;
import com.example.edono.rxapplication.util.Constants;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by edono on 28.01.2017.
 */

public interface ApodApi {

    @GET("/planetary/apod?api_key=" + Constants.API_KEY)
    Flowable<ApodData> getApodByDate(@Query("date") String date);

}
