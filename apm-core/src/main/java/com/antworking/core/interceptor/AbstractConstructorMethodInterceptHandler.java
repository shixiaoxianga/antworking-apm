package com.antworking.core.interceptor;

import com.antworking.common.AwConstant;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author AXiang
 * date 2022/12/30 12:20
 */
public abstract class AbstractConstructorMethodInterceptHandler implements AwConstructorMethodInterceptHandler {

    @Override
    public void before(Object _this, Object[] params, Class<?> clazz, Callable<Object> callable) {
        doBefore(_this, params, clazz, callable);
    }

    @Override
    public void after(Object _this, Object[] params, Class<?> clazz, Callable<Object> callable) {
        doAfter(_this,params, clazz, callable);
    }

    @Override
    public void _catch(Object _this, Throwable e, Object[] params, Class<?> clazz, Callable<Object> callable) {
        doCatch(_this,e, params, clazz, callable);
    }

    @Override
    public void _final(Object _this, Object[] params, Class<?> clazz, Callable<Object> callable) {
        doFinal(_this,params, clazz, callable);
    }

    public abstract void doBefore(Object _this, Object[] params, Class<?> clazz, Callable<Object> callable);

    public abstract void doAfter(Object _this, Object[] params, Class<?> clazz, Callable<Object> callable);

    public abstract void doCatch(Object _this, Throwable e, Object[] params, Class<?> clazz, Callable<Object> callable);

    public abstract void doFinal(Object _this, Object[] params, Class<?> clazz, Callable<Object> callable);


}
