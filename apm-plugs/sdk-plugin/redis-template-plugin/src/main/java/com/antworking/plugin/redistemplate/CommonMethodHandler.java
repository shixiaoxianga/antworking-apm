package com.antworking.plugin.redistemplate;

import com.antworking.core.handler.AbstractMethodInterceptHandler;
import com.antworking.logger.AwLog;
import com.antworking.logger.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class CommonMethodHandler extends AbstractMethodInterceptHandler {
    private AwLog log = LoggerFactory.getLogger(CommonMethodHandler.class);

    @Override
    public void doBefore(Method method, Object[] params, Class<?> clazz, Callable<Object> callable) {
        log.info("param:{} class:{} method:{}", Arrays.toString(params), clazz.getName(), method.getName());
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
