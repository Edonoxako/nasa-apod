package com.example.edono.rxapplication.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.example.edono.rxapplication.domain.ApodRepository;
import com.example.edono.rxapplication.domain.model.ApodData;
import com.example.edono.rxapplication.util.Constants;

import io.reactivex.Flowable;

import static com.example.edono.rxapplication.data.ApodDataContract.SingleApod.COLUMN_NAME_DATE;
import static com.example.edono.rxapplication.data.ApodDataContract.SingleApod.COLUMN_NAME_EXPLANATION;
import static com.example.edono.rxapplication.data.ApodDataContract.SingleApod.COLUMN_NAME_HD_URL;
import static com.example.edono.rxapplication.data.ApodDataContract.SingleApod.COLUMN_NAME_MEDIA_TYPE;
import static com.example.edono.rxapplication.data.ApodDataContract.SingleApod.COLUMN_NAME_TITLE;
import static com.example.edono.rxapplication.data.ApodDataContract.SingleApod.COLUMN_NAME_URL;
import static com.example.edono.rxapplication.data.ApodDataContract.SingleApod.TABLE_NAME;

/**
 * Created by edono on 11.03.2017.
 */

public class AndroidSQLiteApodRepository implements ApodRepository {

    private static final String TAG = "AndroidSQLiteRepository";

    private DBHelper dbHelper;
    private ApodDataConverter converter;

    public AndroidSQLiteApodRepository(DBHelper dbHelper, ApodDataConverter converter) {
        this.dbHelper = dbHelper;
        this.converter = converter;
    }

    @Override
    public Flowable<ApodData> findApodByDate(Calendar date) {
        String[] projection = {
                COLUMN_NAME_TITLE,
                COLUMN_NAME_EXPLANATION,
                COLUMN_NAME_DATE,
                COLUMN_NAME_HD_URL,
                COLUMN_NAME_URL,
                COLUMN_NAME_MEDIA_TYPE
        };

        SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US);
        String selection = COLUMN_NAME_DATE + " LIKE ?";
        String[] selectionArgs = { format.format(date.getTime()) };

        Flowable<ApodData> flowable;
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try (Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        )) {
            if (cursor.getCount() == 0) {
                flowable = Flowable.empty();
            } else {
                cursor.moveToFirst();
                flowable = Flowable.just(converter.fromCursor(cursor));
            }
        } catch (ParseException | MalformedURLException e) {
            Log.e(TAG, "findApodByDate: error while parsing the result of database query", e);
            flowable = Flowable.error(e);
        }

        db.close();
        return flowable;
    }

    @Override
    public void saveApod(ApodData apodData) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = converter.toContentValues(apodData);
        db.insert(ApodDataContract.SingleApod.TABLE_NAME, null, contentValues);
        db.close();
    }
}
