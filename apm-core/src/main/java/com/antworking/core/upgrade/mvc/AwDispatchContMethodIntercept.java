package com.antworking.core.upgrade.mvc;

import com.antworking.apm.model.collect.AwCollectTraceData;
import com.antworking.core.interceptor.AbstractConstructorMethodInterceptHandler;
import com.antworking.util.ClassUtil;

import java.util.concurrent.Callable;

public class AwDispatchContMethodIntercept extends AbstractConstructorMethodInterceptHandler {

    @Override
    public void doBefore(Object _this,  Object[] params, Class<?> clazz, Callable<Object> callable) {
        System.err.println(_this);
        System.err.println(clazz);
        AwDispatchDynamicVariable variable = ClassUtil.setVariable(_this,new AwDispatchDynamicVariable());
        variable.set(AwCollectTraceData.Data.newBuilder().setTraceId("1").build());
    }

    @Override
    public void doAfter(Object _this, Object[] params, Class<?> clazz, Callable<Object> callable) {
        AwDispatchDynamicVariable variable = ClassUtil.getVariable(_this);
        System.err.println(variable.get0().getTraceId());
    }

    @Override
    public void doCatch(Object _this, Throwable e, Object[] params, Class<?> clazz, Callable<Object> callable) {

    }

    @Override
    public void doFinal(Object _this, Object[] params, Class<?> clazz, Callable<Object> callable) {

    }
}
