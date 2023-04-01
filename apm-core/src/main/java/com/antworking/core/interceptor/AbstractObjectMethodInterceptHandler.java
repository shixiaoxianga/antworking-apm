package com.antworking.core.interceptor;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author AXiang
 * date 2022/12/30 12:20
 */
public abstract class AbstractObjectMethodInterceptHandler implements AwObjectMethodInterceptHandler {

    @Override
    public void before(Method method, Object _this, Object[] params, Class<?> clazz, Callable<Object> callable,AntWorkingDynamicVariable variable) {
        doBefore(method, _this,params, clazz, callable,variable);
    }

    @Override
    public Object after(Method method, Object _this, Object[] params, Class<?> clazz, Callable<Object> callable, Object result,AntWorkingDynamicVariable variable) {
        return doAfter(method, _this, params, clazz, callable, result,variable);
    }

    @Override
    public void _catch(Throwable e, Object _this, Method method, Object[] params, Class<?> clazz, Callable<Object> callable,AntWorkingDynamicVariable variable) {
        doCatch(e, _this, method, params, clazz, callable,variable);
    }

    @Override
    public void _final(Method method, Object _this, Object[] params, Class<?> clazz, Callable<Object> callable,AntWorkingDynamicVariable variable) {
        doFinal(method, _this, params, clazz, callable,variable);
    }

    public abstract void doBefore(Method method,
                                  Object _this,
                                  Object[] params,
                                  Class<?> clazz,
                                  Callable<Object> callable,AntWorkingDynamicVariable variable);

    public abstract Object doAfter(Method method,
                                   Object _this,
                                   Object[] params,
                                   Class<?> clazz,
                                   Callable<Object> callable,
                                   Object result,AntWorkingDynamicVariable variable);

    public abstract void doCatch(Throwable e,
                                 Object _this,
                                 Method method,

                                 Object[] params,
                                 Class<?> clazz,
                                 Callable<Object> callable,AntWorkingDynamicVariable variable);

    public abstract void doFinal(Method method,
                                 Object _this,
                                 Object[] params,
                                 Class<?> clazz,
                                 Callable<Object> callable,AntWorkingDynamicVariable variable);
}
