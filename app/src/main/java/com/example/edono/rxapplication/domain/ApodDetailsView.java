package com.example.edono.rxapplication.domain;

import com.example.edono.rxapplication.domain.model.ApodData;

import java.io.FileNotFoundException;
import java.net.URL;

/**
 * Created by edono on 14.03.2017.
 */

public interface ApodDetailsView {

    void showApod(ApodData apodData, boolean useStubImage);

    void showTextMessage(String message);

    void showCalendar();

    void showPictureFullscreen(URL pictureUrl);

}
