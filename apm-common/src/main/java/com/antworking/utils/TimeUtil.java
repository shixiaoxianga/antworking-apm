package com.antworking.utils;

public class TimeUtil {
    public static long getCurrentTimeNano() {
        return  System.currentTimeMillis() * 1000000L + System.nanoTime() % 1000000L;
    }
}
