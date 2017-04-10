package com.example.edono.rxapplication.domain;

import com.example.edono.rxapplication.domain.model.ApodData;

import java.io.FileNotFoundException;

/**
 * Created by edono on 14.03.2017.
 */

public interface ApodView {

    void showApod(ApodData apodData, boolean useStubImage);

    void showTextMessage(String message);

    void showCalendar();

}
