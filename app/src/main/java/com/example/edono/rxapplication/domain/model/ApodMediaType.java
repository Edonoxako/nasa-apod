package com.example.edono.rxapplication.domain.model;

/**
 * Created by edono on 19.03.2017.
 */

public enum ApodMediaType {

    IMAGE,
    VIDEO,
    UNKNOWN;

    public static ApodMediaType fromString(String typeString) {
        /*
        Both upper and lower case are used there because lower case
        type goes from http api in json, and upper case type goes from
        database because it is saved in such format after loading from
        backend
        */
        switch (typeString) {
            case "IMAGE": // from db
            case "image": // from api
                return IMAGE;
            case "VIDEO": // from db
            case "video": // from api
                return VIDEO;
            default:
                return UNKNOWN;
        }
    }
}
