package com.antworking.core.tools;

import com.antworking.core.classload.AntWorkingClassLoad;

import java.io.InputStream;

public class ClassUtil {

    public static InputStream getClassInputStream(String clazz) {
        String name = clazz.replaceAll("\\.", "/") + ".class";
        return AntWorkingClassLoad.INSTANCE.getResourceAsStream(name);
    }
}
