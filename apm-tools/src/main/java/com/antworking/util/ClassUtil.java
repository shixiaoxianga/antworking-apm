package com.antworking.util;


import com.antworking.common.AwConstant;

import java.io.InputStream;
import java.lang.reflect.Field;

public class ClassUtil {

    public static InputStream getClassInputStream(String clazz) {
        String name = clazz.replaceAll("\\.", "/") + ".class";
        return ClassUtil.class.getResourceAsStream(name);
    }

    public static <T> T setVariable(Object _this, T variable, boolean accessible) {
        try {
            Field field = _this.getClass().getDeclaredField(AwConstant.VARIABLE_NAME);
            field.setAccessible(accessible);
            field.set(_this, variable);
            return (T) field.get(_this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T setVariable(Object _this, T variable) {
        return setVariable(_this, variable, false);
    }


    public static <T> T getVariable(Object _this, boolean accessible) {
        try {
            Field field = _this.getClass().getDeclaredField(AwConstant.VARIABLE_NAME);
            field.setAccessible(accessible);
            return (T) field.get(_this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static <T> T getVariable(Object _this) {
        return getVariable(_this,false);
    }
}
