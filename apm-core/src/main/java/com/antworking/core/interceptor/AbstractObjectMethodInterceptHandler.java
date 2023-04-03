package com.antworking.core.interceptor;

import com.antworking.util.ClassUtil;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author AXiang
 * date 2022/12/30 12:20
 */
public abstract class AbstractObjectMethodInterceptHandler implements AwObjectMethodInterceptHandler {

    @Override
    public void before(Method method, Object _this, Object[] params, Class<?> clazz, Callable<Object> callable,AntWorkingDynamicVariable variable,
                       String nodeId) {
        doBefore(method, _this,params, clazz, callable,variable,nodeId);
    }

    @Override
    public Object after(Method method, Object _this, Object[] params, Class<?> clazz, Callable<Object> callable, Object result,AntWorkingDynamicVariable variable,
                        String nodeId) {
        return doAfter(method, _this, params, clazz, callable, result,variable,nodeId);
    }

    @Override
    public void _catch(Throwable e, Object _this, Method method, Object[] params, Class<?> clazz, Callable<Object> callable,AntWorkingDynamicVariable variable,
                       String nodeId) {
        doCatch(e, _this, method, params, clazz, callable,variable,nodeId);
    }

    @Override
    public void _final(Method method, Object _this, Object[] params, Class<?> clazz, Callable<Object> callable,AntWorkingDynamicVariable variable,
                       String nodeId) {
        doFinal(method, _this, params, clazz, callable,variable,nodeId);
    }

    public abstract void doBefore(Method method,
                                  Object _this,
                                  Object[] params,
                                  Class<?> clazz,
                                  Callable<Object> callable,AntWorkingDynamicVariable variable,
                                  String nodeId);

    public abstract Object doAfter(Method method,
                                   Object _this,
                                   Object[] params,
                                   Class<?> clazz,
                                   Callable<Object> callable,
                                   Object result,AntWorkingDynamicVariable variable,
                                   String nodeId);

    public abstract void doCatch(Throwable e,
                                 Object _this,
                                 Method method,

                                 Object[] params,
                                 Class<?> clazz,
                                 Callable<Object> callable,AntWorkingDynamicVariable variable,
                                 String nodeId);

    public abstract void doFinal(Method method,
                                 Object _this,
                                 Object[] params,
                                 Class<?> clazz,
                                 Callable<Object> callable,AntWorkingDynamicVariable variable,
                                 String nodeId);
}
