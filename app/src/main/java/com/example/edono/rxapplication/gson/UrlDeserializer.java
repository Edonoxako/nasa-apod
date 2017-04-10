package com.example.edono.rxapplication.gson;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by edono on 28.01.2017.
 */

public class UrlDeserializer implements JsonDeserializer<URL> {

    private static final String TAG = "UrlDeserializer";

    @Override
    public URL deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String urlString = json.getAsString();
        try {
            URL url = new URL(urlString);
            if ("http".equals(url.getProtocol())) {
                urlString = urlString.replace("http", "https");
                url = new URL(urlString);
            }
            return url;
        } catch (MalformedURLException e) {
            Log.e(TAG, "deserialize: error during url deserialization", e);
            return null;
        }
    }
}
