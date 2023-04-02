package com.antworking.apm.tps.mvc;

import com.antworking.apm.model.AwNodeEnum;
import com.antworking.apm.model.collect.AwCollectTraceData;
import com.antworking.core.collect.AwDataHandler;
import com.antworking.core.interceptor.AbstractConstructorMethodInterceptHandler;
import com.antworking.core.interceptor.AbstractObjectMethodInterceptHandler;
import com.antworking.core.interceptor.AntWorkingDynamicVariable;
import com.antworking.util.ClassUtil;

import java.lang.reflect.Method;
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
