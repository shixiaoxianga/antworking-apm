package com.antworking.core.interceptor;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author AXiang
 * date 2022/12/30 12:20
 */
public interface AwObjectMethodInterceptHandler {

    void before(Method method,
                Object _this,
                Object[] params,
                Class<?> clazz,
                Callable<Object> callable,
                AntWorkingDynamicVariable variable);

    Object after(Method method,
                 Object _this,
                 Object[] params,
                 Class<?> clazz,
                 Callable<Object> callable,
                 Object result,
                 AntWorkingDynamicVariable variable);

    void _catch(Throwable e,
                Object _this,
                Method method,

                Object[] params,
                Class<?> clazz,
                Callable<Object> callable,
                AntWorkingDynamicVariable variable);

    void _final(Method method,
                Object _this,
                Object[] params,
                Class<?> clazz,
                Callable<Object> callable,
                AntWorkingDynamicVariable variable);
}
