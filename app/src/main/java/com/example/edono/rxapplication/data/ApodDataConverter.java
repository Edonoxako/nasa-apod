package com.example.edono.rxapplication.data;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.edono.rxapplication.domain.model.ApodData;
import com.example.edono.rxapplication.domain.model.ApodMediaType;
import com.example.edono.rxapplication.util.Constants;
import com.example.edono.rxapplication.util.Util;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import static com.example.edono.rxapplication.data.ApodDataContract.SingleApod.*;

/**
 * Created by edono on 11.03.2017.
 */

public class ApodDataConverter {

    SimpleDateFormat format;

    public ApodDataConverter() {
        format = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US);
    }

    public ContentValues toContentValues(ApodData apodData) {
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME_TITLE, apodData.getTitle());
        cv.put(COLUMN_NAME_EXPLANATION, apodData.getExplanation());
        cv.put(COLUMN_NAME_MEDIA_TYPE, apodData.getMediaType().toString());
        cv.put(COLUMN_NAME_HD_URL, Util.toStringFromNullable(apodData.getHdUrl()));
        cv.put(COLUMN_NAME_URL, Util.toStringFromNullable(apodData.getUrl()));
        cv.put(COLUMN_NAME_DATE, format.format(apodData.getDate().getTime()));

        return cv;
    }

    public ApodData fromCursor(Cursor cursor) throws ParseException, MalformedURLException {
        String savedTitle = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_TITLE));
        String savedExplanation = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_EXPLANATION));
        String savedDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_DATE));
        String savedHdUrl = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_HD_URL));
        String savedUrl = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_URL));
        String savedMediaType = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_MEDIA_TYPE));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(format.parse(savedDate));

        return new ApodData(
                calendar,
                savedExplanation,
                savedHdUrl.isEmpty() ? null : new URL(savedHdUrl),
                ApodMediaType.fromString(savedMediaType),
                savedTitle,
                savedUrl.isEmpty() ? null : new URL(savedUrl)
        );
    }

}
