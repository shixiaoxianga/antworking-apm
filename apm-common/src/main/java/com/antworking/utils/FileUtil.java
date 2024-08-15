package com.antworking.utils;


public class FileUtil {

    public static String getPlatFormSlash() {
        return PlatFormUtil.isWindows() ? "\\" : "/";
    }

}
