package com.antworking.javasql;

import com.antworking.core.handler.AbstractMethodInterceptHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class JavaSqlDriverInterceptMethodHandler extends AbstractMethodInterceptHandler {
    @Override
    public void doBefore(Method method, Object[] params, Class<?> clazz, Callable<Object> callable) {

    }

    @Override
    public Object doAfter(Method method, Object[] params, Class<?> clazz, Callable<Object> callable, Object result) {
        System.out.println(method.getName()+"  "+ Arrays.toString(params)+"   "+clazz.getName());
        if (result instanceof java.sql.Connection) {
            return Proxy.newProxyInstance(this.getClass().getClassLoader(),
                    new Class[]{java.sql.Connection.class},
                    new JavaSqlConnectionProxy(result));
        }
        return null;
    }

    @Override
    public void doCatch(Throwable e, Method method, Object[] params, Class<?> clazz, Callable<Object> callable) {

    }

    @Override
    public void doFinal(Method method, Object[] params, Class<?> clazz, Callable<Object> callable) {

    }
}
