package com.antworking.apm;

import com.antworking.core.handler.AbstractMethodInterceptHandler;
import com.antworking.core.handler.IMethodInterceptHandler;
import com.antworking.logger.AwLog;
import com.antworking.logger.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class MethodInterceptHandlerImpl extends AbstractMethodInterceptHandler {
    private final AwLog log = LoggerFactory.getLogger(MethodInterceptHandlerImpl.class);

    @Override
    public void doBefore(Method method, Object[] params, Class<?> clazz, Callable<Object> callable) {

    }

    @Override
    public Object doAfter(Method method, Object[] params, Class<?> clazz, Callable<Object> callable, Object result) {
        return null;
    }

    @Override
    public void doCatch(Throwable e, Method method, Object[] params, Class<?> clazz, Callable<Object> callable) {

    }

    @Override
    public void doFinal(Method method, Object[] params, Class<?> clazz, Callable<Object> callable) {

    }
}
