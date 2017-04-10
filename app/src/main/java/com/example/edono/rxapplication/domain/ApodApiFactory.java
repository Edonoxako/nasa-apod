package com.example.edono.rxapplication.domain;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by edono on 28.01.2017.
 */

public class ApodApiFactory {

    private String baseUrl;

    public ApodApiFactory(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public ApodApi getApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ApodApi.class);
    }

    public static ApodApiFactory create(String baseUrl) {
        return new ApodApiFactory(baseUrl);
    }
}
