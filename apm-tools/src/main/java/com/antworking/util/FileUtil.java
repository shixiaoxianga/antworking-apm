package com.antworking.util;


import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

    public static String getPlatFormSlash() {
        return PlatFormUtil.isWindows() ? "\\" : "/";
    }

}
