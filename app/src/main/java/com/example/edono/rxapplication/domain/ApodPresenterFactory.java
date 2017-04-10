package com.example.edono.rxapplication.domain;

import android.content.Context;

import com.example.edono.rxapplication.data.AndroidSQLiteApodRepository;
import com.example.edono.rxapplication.data.ApodDataConverter;
import com.example.edono.rxapplication.data.DBHelper;
import com.example.edono.rxapplication.util.Constants;

/**
 * Created by edono on 14.03.2017.
 */

public class ApodPresenterFactory {

    public static ApodPresenter newPresenter(Context context, ApodView view) {
        ApodApiFactory apiFactory = new ApodApiFactory(Constants.BASE_URL);
        ApodApi api = apiFactory.getApi();

        DBHelper dbHelper = new DBHelper(context);
        ApodDataConverter converter = new ApodDataConverter();

        ApodRepository repository = new AndroidSQLiteApodRepository(dbHelper, converter);

        RestCacheApodInteractor interactor = new RestCacheApodInteractor(api, repository);

        return new ApodPresenter(view, interactor);
    }

}
