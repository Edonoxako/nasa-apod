package com.example.edono.rxapplication.data;

import android.provider.BaseColumns;

/**
 * Created by edono on 11.03.2017.
 */

public class ApodDataContract {

    public ApodDataContract() {}

    public static abstract class SingleApod implements BaseColumns {
        public static final String TABLE_NAME = "apod";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_EXPLANATION = "explanation";
        public static final String COLUMN_NAME_HD_URL = "hd_url";
        public static final String COLUMN_NAME_MEDIA_TYPE = "media_type";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_URL = "url";
    }

    public static final String TEXT_TYPE = " TEXT";
    public static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + SingleApod.TABLE_NAME + " (" +
                    SingleApod._ID + " INTEGER PRIMARY KEY," +
                    SingleApod.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
                    SingleApod.COLUMN_NAME_EXPLANATION + TEXT_TYPE + COMMA_SEP +
                    SingleApod.COLUMN_NAME_HD_URL + TEXT_TYPE + COMMA_SEP +
                    SingleApod.COLUMN_NAME_MEDIA_TYPE + TEXT_TYPE + COMMA_SEP +
                    SingleApod.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    SingleApod.COLUMN_NAME_URL + TEXT_TYPE +
            " );";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SingleApod.TABLE_NAME;
}
