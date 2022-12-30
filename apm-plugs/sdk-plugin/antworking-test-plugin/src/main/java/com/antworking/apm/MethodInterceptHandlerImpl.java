package com.antworking.apm;

import com.antworking.core.handler.IMethodInterceptHandler;
import com.antworking.logger.AwLog;
import com.antworking.logger.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class MethodInterceptHandlerImpl implements IMethodInterceptHandler {
    private final AwLog log = LoggerFactory.getLogger(MethodInterceptHandlerImpl.class);

    @Override
    public void before(Method method, Object[] params, Class<?> clazz, Callable<Object> callable) {

    }

    @Override
    public Object after(Method method, Object[] params, Class<?> clazz, Callable<Object> callable, Object result) {
        log.info("methodName: {} args: {} clazz: {} res:{} ",
                method.getName(), Arrays.toString(params), clazz, result);
        return null;
    }

    @Override
    public void _catch(Throwable e, Method method, Object[] params, Class<?> clazz, Callable<Object> callable) {

    }

    @Override
    public void _final(Method method, Object[] params, Class<?> clazz, Callable<Object> callable) {

    }
}
