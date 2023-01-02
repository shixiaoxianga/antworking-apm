package com.antworking.plugin.tomcat;

import com.antworking.core.common.ConstantAppNode;
import com.antworking.core.collect.AwCollectManager;
import com.antworking.core.handler.AbstractMethodInterceptHandler;
import com.antworking.logger.AwLog;
import com.antworking.logger.LoggerFactory;
import com.antworking.model.collect.CollectDataBaseModel;
import com.antworking.model.collect.ErrorDescribeModel;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class TomcatHttpServletInterceptMethodHandler extends AbstractMethodInterceptHandler {

    private final AwLog log = LoggerFactory.getLogger(TomcatHttpServletInterceptMethodHandler.class);

    @Override
    public void doBefore(Method method, Object[] params, Class<?> clazz, Callable<Object> callable) {
        CollectDataBaseModel model = null;
        TomcatReqDescribeModel tomcat = new TomcatReqDescribeModel();
        if (AwCollectManager.get() == null) {
            model = CollectDataBaseModel.init(true, tomcat, ConstantAppNode.TOMCAT.setVersion("未知"),
                    Thread.currentThread().getName());
            AwCollectManager.create(model);
        }
        HttpServletRequestAdapter request;
        Object req = params[0];
        request = new HttpServletRequestAdapter(req);
        tomcat.setMethodName(method.getName());
        tomcat.setClientIp(request.getClientIp());
        tomcat.setReqUri(request.getRequestURI());
        tomcat.setReqUrl(request.getRequestURL());
    }

    @Override
    public Object doAfter(Method method, Object[] params, Class<?> clazz, Callable<Object> callable, Object result) {
        HttpServletResponseAdapter response;
        Object resp = params[1];
        response = new HttpServletResponseAdapter(resp);
        CollectDataBaseModel model = AwCollectManager.getNode(ConstantAppNode._TOMCAT);
        TomcatReqDescribeModel tomcat = (TomcatReqDescribeModel) model.getData();
        tomcat.setRepCode(response.getResponseCode());
        return null;
    }

    @Override
    public void doCatch(Throwable e, Method method, Object[] params, Class<?> clazz, Callable<Object> callable) {
        CollectDataBaseModel model = AwCollectManager.getNode(ConstantAppNode._TOMCAT);
        ErrorDescribeModel errorDescribeModel = new ErrorDescribeModel();
        errorDescribeModel.setClazz(clazz.getName());
        errorDescribeModel.setMessage(e.getMessage());
        errorDescribeModel.setStacks(e.getStackTrace());
        errorDescribeModel.setTimeStamp(System.currentTimeMillis());
        assert model != null;
        model.setError(errorDescribeModel);
    }

    @Override
    public void doFinal(Method method, Object[] params, Class<?> clazz, Callable<Object> callable) {
        CollectDataBaseModel model = AwCollectManager.getNode(ConstantAppNode._TOMCAT);
        assert model != null;
        model.setEndTime(System.currentTimeMillis());
        AwCollectManager.write();
    }
}
