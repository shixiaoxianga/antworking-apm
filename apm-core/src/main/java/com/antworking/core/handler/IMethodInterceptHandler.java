package com.antworking.core.handler;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author AXiang
 * date 2022/12/30 12:20
 */
public interface IMethodInterceptHandler {

    void before(Method method,
                Object[] params,
                Class<?> clazz,
                Callable<Object> callable);

    Object after(Method method,
                 Object[] params,
                 Class<?> clazz,
                 Callable<Object> callable,
                 Object result);

    void _catch(Throwable e, Method method,
                Object[] params,
                Class<?> clazz,
                Callable<Object> callable);

    void _final(Method method,
                Object[] params,
                Class<?> clazz,
                Callable<Object> callable);
}
