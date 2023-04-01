package com.antworking.apm.plugs.demo;

import com.antworking.core.interceptor.AbstractObjectMethodInterceptHandler;
import com.antworking.core.interceptor.AntWorkingDynamicVariable;
import com.antworking.core.interceptor.AwObjectMethodIntercept;
import com.antworking.core.interceptor.AwObjectMethodInterceptHandler;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class AwUserMethodIntercept extends AbstractObjectMethodInterceptHandler {
    @Override
    public void doBefore(Method method, Object _this, Object[] params, Class<?> clazz, Callable<Object> callable, AntWorkingDynamicVariable variable) {
        System.err.println(method.getName());
    }

    @Override
    public Object doAfter(Method method, Object _this, Object[] params, Class<?> clazz, Callable<Object> callable, Object result,AntWorkingDynamicVariable variable) {
        return result;
    }

    @Override
    public void doCatch(Throwable e, Object _this, Method method, Object[] params, Class<?> clazz, Callable<Object> callable,AntWorkingDynamicVariable variable) {

    }

    @Override
    public void doFinal(Method method, Object _this, Object[] params, Class<?> clazz, Callable<Object> callable,AntWorkingDynamicVariable variable) {

    }
}
