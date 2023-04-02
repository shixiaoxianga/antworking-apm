package com.antworking.core.interceptor;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author AXiang
 * date 2022/12/30 12:20
 */
public interface AwConstructorMethodInterceptHandler {

    void before(
            Object _this,
            Object[] params,
            Class<?> clazz,
            Callable<Object> callable);

    void after(
            Object _this,
            Object[] params,
            Class<?> clazz,
            Callable<Object> callable);

    void _catch(

            Object _this, Throwable e,
            Object[] params,
            Class<?> clazz,
            Callable<Object> callable);

    void _final(Object _this, Object[] params,
                Class<?> clazz,
                Callable<Object> callable);
}
