package com.antworking.apm.tps.httpclient;

import com.antworking.apm.model.AwNodeEnum;
import com.antworking.apm.model.collect.AwCollectTraceData;
import com.antworking.core.collect.AwCollectManager;
import com.antworking.core.collect.AwCollectPack;
import com.antworking.core.collect.AwDataHandler;
import com.antworking.core.interceptor.AbstractObjectMethodInterceptHandler;
import com.antworking.core.interceptor.AntWorkingDynamicVariable;
import com.antworking.core.interceptor.AwDefaultDynamicVariable;
import com.antworking.util.ClassUtil;
import com.antworking.util.JsonUtil;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class AwInternalHttpClientMethodIntercept extends AbstractObjectMethodInterceptHandler {
    @Override
    public void doBefore(Method method, Object _this, Object[] params, Class<?> clazz,
                         Callable<Object> callable, AntWorkingDynamicVariable variable,
                         String nodeId) {
        AwCollectTraceData.Data data = AwDataHandler.initData(params, clazz, method.getName(), AwNodeEnum.HTTP_CLIENT, nodeId);
        AwCollectManager.get().addData(data);
    }

    @Override
    public Object doAfter(Method method, Object _this, Object[] params,
                          Class<?> clazz, Callable<Object> callable, Object result,
                          AntWorkingDynamicVariable variable,
                          String nodeId) {
        Object req = params[1];
        InternalModel model = new InternalModel();

        InternalRequestAdapter requestAdapter = new InternalRequestAdapter(req);
        model.setUri(requestAdapter.getUri());
        model.setProtocolVersion(requestAdapter.protocolVersion());
        model.setMethod(requestAdapter.getMethod());
        model.setRepHeader(requestAdapter.getAllHeader());

        InternalResponseAdapter responseAdapter = new InternalResponseAdapter(result);
        model.setRepHeader(responseAdapter.getAllHeader());
        model.setCode(responseAdapter.getCode());

        AwCollectTraceData.Data data = AwCollectManager.get().getData(nodeId);
        data = data.toBuilder()
                .setContent(JsonUtil.toJsonString(model)).build();
        AwCollectManager.get().setData(data, nodeId);
        return result;
    }

    @Override
    public void doCatch(Throwable e,
                        Object _this,
                        Method method,
                        Object[] params,
                        Class<?> clazz,
                        Callable<Object> callable,
                        AntWorkingDynamicVariable variable,
                        String nodeId) {
        AwCollectTraceData.Data data = AwCollectManager.get().getData(nodeId);
        data = data.toBuilder()
                .setError(e.toString())
                .setStackTrace(Arrays.toString(Arrays.stream(e.getStackTrace()).map(Object::toString).toArray(String[]::new)))
                .setContent("1").build();
        AwCollectManager.get().setData(data, nodeId);
    }

    @Override
    public void doFinal(Method method,
                        Object _this,
                        Object[] params,
                        Class<?> clazz,
                        Callable<Object> callable,
                        AntWorkingDynamicVariable variable,
                        String nodeId) {
        AwCollectTraceData.Data data = AwCollectManager.get().getData(nodeId);
        long endTime = System.currentTimeMillis();
        data = data.toBuilder()
                .setEndTime(endTime)
                .setUseTime(String.valueOf((endTime - data.getStartTime())))
                .build();
        AwCollectManager.get().setData(data, nodeId);
        AwCollectManager.get().finish(nodeId);
    }
}
