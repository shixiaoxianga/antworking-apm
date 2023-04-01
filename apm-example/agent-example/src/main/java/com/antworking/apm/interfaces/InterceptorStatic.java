package com.antworking.apm.interfaces;

import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.concurrent.Callable;

public class InterceptorStatic {

    @RuntimeType
    public Object interceptor(@AllArguments Object[] args, @SuperCall Callable<?> callable, @Origin Class<?> clazz) {
        System.out.println("static proxy...");
        try {

            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}