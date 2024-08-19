package com.antworking.plugin.javasql;

import com.antworking.core.collect.AwCollectManager;
import com.antworking.core.common.ConstantAppNode;
import com.antworking.core.handler.AbstractMethodInterceptHandler;
import com.antworking.model.collect.CollectDataBaseModel;
import com.antworking.model.collect.ErrorDescribeModel;
import com.antworking.model.collect.MethodDescribeModel;
import com.antworking.utils.TimeUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.Callable;

public class JavaSqlDriverInterceptMethodHandler extends AbstractMethodInterceptHandler {
    @Override
    public void doBefore(Method method, Object[] params, Object instance,Class<?> clazz, Callable<Object> callable) {
        MethodDescribeModel methodDescribeModel = new MethodDescribeModel();
        methodDescribeModel.setClazz(clazz.getName());
        methodDescribeModel.setStartTime(System.currentTimeMillis());
        methodDescribeModel.setParam(params);
        methodDescribeModel.setName(method.getName());
        CollectDataBaseModel model = CollectDataBaseModel.init(AwCollectManager.get() != null,
                methodDescribeModel,
                ConstantAppNode.SQL_DRIVE_CONNECT,
                Thread.currentThread().getName(),
                AwCollectManager.getTraceId());
        AwCollectManager.create(model);
    }

    @Override
    public Object doAfter(Method method, Object[] params, Object instance,Class<?> clazz, Callable<Object> callable, Object result) {
        if (result instanceof java.sql.Connection) {
            CollectDataBaseModel model = AwCollectManager.getNode(ConstantAppNode._SQL_DRIVE_CONNECT);
            assert model != null;
            MethodDescribeModel  methodDescribeModel= (MethodDescribeModel) model.getData();
            methodDescribeModel.setEndTime(TimeUtil.getCurrentTimeNano());
            methodDescribeModel.setReturnClazz(result.getClass().getName());
            return Proxy.newProxyInstance(this.getClass().getClassLoader(),
                    new Class[]{java.sql.Connection.class},
                    new JavaSqlConnectionProxy(result));
        }
        return null;
    }

    @Override
    public void doCatch(Throwable e, Method method, Object[] params,Object instance, Class<?> clazz, Callable<Object> callable) {
        CollectDataBaseModel model = AwCollectManager.getNode(ConstantAppNode._SQL_DRIVE_CONNECT);
        ErrorDescribeModel errorDescribeModel = new ErrorDescribeModel();
        errorDescribeModel.setClazz(clazz.getName());
        errorDescribeModel.setMessage(e.toString());
        errorDescribeModel.setStacks(e.getStackTrace());
        errorDescribeModel.setTimeStamp(System.currentTimeMillis());
        assert model != null;
        model.setError(errorDescribeModel);
    }

    @Override
    public void doFinal(Method method, Object[] params,Object instance, Class<?> clazz, Callable<Object> callable) {
        CollectDataBaseModel model = AwCollectManager.getNode(ConstantAppNode._SQL_DRIVE_CONNECT);
        assert model != null;
        model.setEndTime(TimeUtil.getCurrentTimeNano());
        AwCollectManager.put();
    }
}
