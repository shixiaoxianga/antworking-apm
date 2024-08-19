package com.antworking.plugin.httpclient;

import com.antworking.core.collect.AwCollectManager;
import com.antworking.core.common.ConstantAppNode;
import com.antworking.core.handler.AbstractMethodInterceptHandler;
import com.antworking.model.collect.CollectDataBaseModel;
import com.antworking.model.collect.ErrorDescribeModel;
import com.antworking.model.collect.MethodDescribeModel;
import com.antworking.plugin.httpclient.model.ApacheHttpClientDescribeModel;
import com.antworking.utils.JsonUtil;
import com.antworking.utils.TimeUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class ApacheHttpClientInterceptMethodHandler extends AbstractMethodInterceptHandler {
    @Override
    public void doBefore(Method method, Object[] params, Object instance,Class<?> clazz, Callable<Object> callable) {

        HttpUriRequestAdapter requestAdapter = new HttpUriRequestAdapter(params[0]);
        ApacheHttpClientDescribeModel describeModel = new ApacheHttpClientDescribeModel();

        describeModel.setMethod(requestAdapter.getMethod());
        describeModel.setUrl(requestAdapter.getUrl());
        describeModel.setReqHeaders(requestAdapter.getHeaders());
        CollectDataBaseModel model = CollectDataBaseModel.init(AwCollectManager.get() != null,
                describeModel,
                ConstantAppNode.APACHE_HTTP_CLIENT,
                Thread.currentThread().getName(),
                AwCollectManager.getTraceId());
        AwCollectManager.createOrAdd(model);
    }

    @Override
    public Object doAfter(Method method, Object[] params, Object instance,Class<?> clazz, Callable<Object> callable, Object result) {
        CloseableHttpResponseAdapter responseAdapter = new CloseableHttpResponseAdapter(result);
        CollectDataBaseModel model = AwCollectManager.getNode(ConstantAppNode.APACHE_HTTP_CLIENT.getFrame());
        assert model != null;
        ApacheHttpClientDescribeModel  methodDescribeModel= (ApacheHttpClientDescribeModel) model.getData();

        methodDescribeModel.setRespCode(responseAdapter.getStatusCode());
        methodDescribeModel.setRespHeaders(responseAdapter.getHeaders());
        return null;
    }

    @Override
    public void doCatch(Throwable e, Method method, Object[] params,Object instance, Class<?> clazz, Callable<Object> callable) {
        CollectDataBaseModel model = AwCollectManager.getNode(ConstantAppNode.APACHE_HTTP_CLIENT.getFrame());
        ErrorDescribeModel errorDescribeModel = new ErrorDescribeModel();
        errorDescribeModel.setClazz(clazz.getName());
        errorDescribeModel.setMessage(e.toString());
        errorDescribeModel.setStacks(e.getStackTrace());
        errorDescribeModel.setTimeStamp(System.currentTimeMillis());
        assert model != null;
        model.setError(errorDescribeModel);
    }

    @Override
    public void doFinal(Method method, Object[] params, Object instance,Class<?> clazz, Callable<Object> callable) {
        CollectDataBaseModel model = AwCollectManager.getNode(ConstantAppNode.APACHE_HTTP_CLIENT.getFrame());
        assert model != null;
        model.setEndTime(TimeUtil.getCurrentTimeNano());
        if (AwCollectManager.isExist()) {
            if (!AwCollectManager.get().get(0).isWeb()) {
                AwCollectManager.finish();
            }
        }
    }
}
