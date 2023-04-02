package com.antworking.apm.tps.mvc;

import com.antworking.apm.model.AwNodeEnum;
import com.antworking.apm.model.collect.AwCollectTraceData;
import com.antworking.core.collect.AwCollectManager;
import com.antworking.core.collect.AwCollectPack;
import com.antworking.core.collect.AwDataHandler;
import com.antworking.core.factory.AntWorkingFactory;
import com.antworking.core.interceptor.AbstractObjectMethodInterceptHandler;
import com.antworking.core.interceptor.AntWorkingDynamicVariable;
import com.antworking.util.ClassUtil;
import com.antworking.util.JsonUtil;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class AwDispatchMethodIntercept extends AbstractObjectMethodInterceptHandler {
    @Override
    public void doBefore(Method method, Object _this, Object[] params, Class<?> clazz, Callable<Object> callable, AntWorkingDynamicVariable variable) {
        if (variable == null) {
            variable = ClassUtil.setVariable(_this, new AwDispatchDynamicVariable());
            variable.set(AwDataHandler.initData(params, clazz, method.getName(), AwNodeEnum.SPRING_MAC));
            AwCollectManager.add(new AwCollectPack(variable.get().getTraceId(), variable.get()));
        }
    }

    @Override
    public Object doAfter(Method method, Object _this, Object[] params, Class<?> clazz, Callable<Object> callable, Object result, AntWorkingDynamicVariable variable) {
        AwCollectTraceData.Data data = variable.get();

        HttpServletRequestAdapter request;
        Object req = params[0];
        request = new HttpServletRequestAdapter(req);
        HttpServletResponseAdapter response;
        Object resp = params[1];
        response = new HttpServletResponseAdapter(resp);
        TomcatReqDescribeModel tomcatModel = new TomcatReqDescribeModel();
        tomcatModel.setMethodName(request.getMethod());
        tomcatModel.setClientIp(request.getClientIp());
        tomcatModel.setReqUri(request.getRequestURI());
        tomcatModel.setReqUrl(request.getRequestURL());
        tomcatModel.setRepCode(response.getResponseCode());
        data = data.toBuilder()
                .setContent(JsonUtil.toJsonString(tomcatModel)).build();
        variable.set(data);
        return result;
    }

    @Override
    public void doCatch(Throwable e, Object _this, Method method, Object[] params, Class<?> clazz, Callable<Object> callable, AntWorkingDynamicVariable variable) {
        AwCollectTraceData.Data data = variable.get();
        data = data.toBuilder()
                .setError(e.toString())
                .setStackTrace(Arrays.toString(Arrays.stream(e.getStackTrace()).map(Object::toString).toArray(String[]::new)))
                .setContent("1").build();
        variable.set(data);
    }

    @Override
    public void doFinal(Method method, Object _this, Object[] params, Class<?> clazz, Callable<Object> callable, AntWorkingDynamicVariable variable) {
        AwCollectTraceData.Data data = variable.get();
        data = data.toBuilder()
                .setEndTime(System.currentTimeMillis())
                .setUseTime(String.valueOf(data.getStartTime() - data.getEndTime()))
                .build();
        variable.set(data);
        variable.write(data);
        System.out.println(JsonUtil.toJsonString(data));
    }
}
