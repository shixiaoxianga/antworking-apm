package com.antworking.plugin.redistemplate;

import com.antworking.core.handler.AbstractMethodInterceptHandler;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class DefaultGeoOperationsInterceptMethodHandler extends AbstractMethodInterceptHandler {
    @Override
    public void doBefore(Method method, Object[] params, Object instance,Class<?> clazz, Callable<Object> callable) {

    }

    @Override
    public Object doAfter(Method method, Object[] params,Object instance, Class<?> clazz, Callable<Object> callable, Object result) {
        return null;
    }

    @Override
    public void doCatch(Throwable e, Method method, Object[] params,Object instance, Class<?> clazz, Callable<Object> callable) {

    }

    @Override
    public void doFinal(Method method, Object[] params,Object instance, Class<?> clazz, Callable<Object> callable) {

    }
}
