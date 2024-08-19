package com.antworking.core.handler;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author AXiang
 * date 2022/12/30 12:20
 */
public abstract class AbstractMethodInterceptHandler implements IMethodInterceptHandler {

    @Override
    public void before(Method method, Object[] params, Object instance,Class<?> clazz, Callable<Object> callable) {
        doBefore(method, params,instance, clazz, callable);
    }

    @Override
    public Object after(Method method, Object[] params,Object instance, Class<?> clazz, Callable<Object> callable, Object result) {
        return doAfter(method, params, instance,clazz, callable, result);
    }

    @Override
    public void _catch(Throwable e, Method method, Object[] params,Object instance, Class<?> clazz, Callable<Object> callable) {
        doCatch(e, method, params, instance,clazz, callable);
    }

    @Override
    public void _final(Method method, Object[] params,Object instance, Class<?> clazz, Callable<Object> callable) {
        doFinal(method, params,instance, clazz, callable);
    }

    public abstract void doBefore(Method method,
                Object[] params,
                Object instance,
                Class<?> clazz,
                Callable<Object> callable);

    public abstract Object doAfter(Method method,
                 Object[] params,
                 Object instance,
                 Class<?> clazz,
                 Callable<Object> callable,
                 Object result);

    public abstract void doCatch(Throwable e, Method method,
                Object[] params,
                Object instance,
                Class<?> clazz,
                Callable<Object> callable);

    public abstract void doFinal(Method method,
                Object[] params,
                Object instance,
                Class<?> clazz,
                Callable<Object> callable);
}
