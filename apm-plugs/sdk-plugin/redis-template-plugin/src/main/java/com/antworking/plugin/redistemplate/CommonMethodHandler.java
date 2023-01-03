package com.antworking.plugin.redistemplate;

import com.antworking.core.collect.AwCollectManager;
import com.antworking.core.common.ConstantAppNode;
import com.antworking.core.handler.AbstractMethodInterceptHandler;
import com.antworking.core.interceptor.AwMethodIntercept;
import com.antworking.logger.AwLog;
import com.antworking.logger.LoggerFactory;
import com.antworking.model.collect.CollectDataBaseModel;
import com.antworking.model.collect.ErrorDescribeModel;
import com.antworking.model.collect.MethodDescribeModel;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class CommonMethodHandler extends AbstractMethodInterceptHandler {
    private AwLog log = LoggerFactory.getLogger(CommonMethodHandler.class);
    public final static String[] notMatchMethod = {
            "rawValue",
            "valueSerializer",
            "execute",
            "rawKey",
            "keySerializer",
            "deserializeValue"
    };

    public static ElementMatcher.Junction<NamedElement> getMatch() {
        ElementMatcher.Junction<NamedElement> matcher = null;
        for (String methodName : CommonMethodHandler.notMatchMethod) {
            if (matcher == null) {
                matcher = (ElementMatchers.not(ElementMatchers.named(methodName)));
            } else {
                matcher = matcher.and(ElementMatchers.not(ElementMatchers.named(methodName)));
            }
        }
        return matcher;
    }

    @Override
    public void doBefore(Method method, Object[] params, Class<?> clazz, Callable<Object> callable) {
        MethodDescribeModel methodDescribeModel = new MethodDescribeModel();
        methodDescribeModel.setClazz(clazz.getName());
        methodDescribeModel.setStartTime(System.currentTimeMillis());
        methodDescribeModel.setParam(params);
        methodDescribeModel.setName(method.getName());
        CollectDataBaseModel model = CollectDataBaseModel.init(AwCollectManager.get() != null,
                methodDescribeModel,
                ConstantAppNode.REDIS_TEMPLATE,
                Thread.currentThread().getName());
        AwCollectManager.put(model);
    }

    @Override
    public Object doAfter(Method method, Object[] params, Class<?> clazz, Callable<Object> callable, Object result) {
        return null;
    }

    @Override
    public void doCatch(Throwable e, Method method, Object[] params, Class<?> clazz, Callable<Object> callable) {
        CollectDataBaseModel model = AwCollectManager.getNode(ConstantAppNode._REDIS_TEMPLATE);
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
        CollectDataBaseModel model = AwCollectManager.getNode(ConstantAppNode._REDIS_TEMPLATE);
        assert model != null;
        model.setEndTime(System.currentTimeMillis());
    }
}
