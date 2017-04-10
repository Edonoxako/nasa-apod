package com.example.edono.rxapplication.util;

/**
 * Created by edono on 19.03.2017.
 */

public class Util {

    public static String toStringFromNullable(Object o) {
        if (o == null) {
            return "";
        } else {
            return o.toString();
        }
    }

}
