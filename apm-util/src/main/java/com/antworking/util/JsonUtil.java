package com.antworking.util;

import com.google.gson.GsonBuilder;

public class JsonUtil {

    public static String toJsonString(Object o){
        return new GsonBuilder().disableHtmlEscaping().create().toJson(o);
    }
}
