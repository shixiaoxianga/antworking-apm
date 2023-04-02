package com.antworking.util;


import com.antworking.common.AwConstant;

import java.io.InputStream;
import java.lang.reflect.Field;

public class ClassUtil {

    public static InputStream getClassInputStream(String clazz) {
        String name = clazz.replaceAll("\\.", "/") + ".class";
        return ClassUtil.class.getResourceAsStream(name);
    }
    public static <T> T setVariable(Object _this, T variable) {
        try {
            Field field = _this.getClass().getDeclaredField(AwConstant.VARIABLE_NAME);
            field.set(_this, variable);
            return (T) field.get(_this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static <T> T getVariable(Object _this) {
        try {
            Field field = _this.getClass().getDeclaredField(AwConstant.VARIABLE_NAME);
            return (T) field.get(_this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
