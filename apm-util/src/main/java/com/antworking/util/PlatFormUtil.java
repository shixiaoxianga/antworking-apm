package com.antworking.util;

public class PlatFormUtil {

    private static final String os = System.getProperty("os.name").toLowerCase();

    public static boolean isWindows(){
        return os.contains("windows");
    }
}
