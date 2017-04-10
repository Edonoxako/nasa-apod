package com.example.edono.rxapplication.gson;

import android.util.Log;

import com.example.edono.rxapplication.util.Constants;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by edono on 28.01.2017.
 */

public class CalendarDeserializer implements JsonDeserializer<Calendar> {

    private static final String TAG = "CalendarDeserializer";

    @Override
    public Calendar deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US);
        Calendar calendar = Calendar.getInstance();

        try {
            String stringDate = json.getAsString();
            calendar.setTime(format.parse(stringDate));
        } catch (ParseException e) {
            Log.e(TAG, "deserialize: error during date deserialization", e);
        }

        return calendar;
    }
}
