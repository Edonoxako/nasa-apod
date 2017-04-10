package com.example.edono.rxapplication.gson;

import com.example.edono.rxapplication.domain.model.ApodMediaType;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by edono on 19.03.2017.
 */

public class ApodMediaTypeDeserializer implements JsonDeserializer<ApodMediaType> {
    @Override
    public ApodMediaType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String mediaType = json.getAsString();
        return ApodMediaType.fromString(mediaType);
    }
}
