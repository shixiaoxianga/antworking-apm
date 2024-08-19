package com.antworking.plugin.redistemplate;

import com.antworking.core.collect.AwCollectManager;
import com.antworking.core.common.ConstantAppNode;
import com.antworking.core.handler.AbstractMethodInterceptHandler;
import com.antworking.logger.AwLog;
import com.antworking.logger.LoggerFactory;
import com.antworking.model.collect.CollectDataBaseModel;
import com.antworking.model.collect.ErrorDescribeModel;
import com.antworking.model.collect.MethodDescribeModel;
import com.antworking.utils.TimeUtil;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class RedisCommonMethodHandler extends AbstractMethodInterceptHandler {
    private AwLog log = LoggerFactory.getLogger(RedisCommonMethodHandler.class);
    public final static String[] notMatchMethod = {
            "rawValue",
            "valueSerializer",
            "execute",
            "rawKey",
            "keySerializer",
            "hashKeySerializer",
            "hashValueSerializer",
            "deserializeValue",
            "stringSerializer",
            "deserializeTupleValues",
            "deserializeValues",
            "deserializeHashKeys",
            "deserializeHashValues",
            "deserializeHashMap",
            "deserializeKey",
            "deserializeKeys",
            "deserializeValue",
            "deserializeHashValue",
            "deserializeGeoResults",
            "deserializeString",
            "rawTupleValues",
            "deserializeTuple",
            "rawKey",
            "getOperations",
            "rawValues",
            "rawHashKey",
            "rawHashKeys",
            "rawHashValue",
            "rawKeys",
            "hashValueSerializerPresent",
    };

    public static ElementMatcher.Junction<NamedElement> getMatch() {
        ElementMatcher.Junction<NamedElement> matcher = null;
        for (String methodName : RedisCommonMethodHandler.notMatchMethod) {
            if (matcher == null) {
                matcher = (ElementMatchers.not(ElementMatchers.named(methodName)));
            } else {
                matcher = matcher.and(ElementMatchers.not(ElementMatchers.named(methodName)));
            }
        }
        return matcher;
    }

    @Override
    public void doBefore(Method method, Object[] params,Object instance, Class<?> clazz, Callable<Object> callable) {
        MethodDescribeModel methodDescribeModel = new MethodDescribeModel();
        methodDescribeModel.setClazz(clazz.getName());
        methodDescribeModel.setParam(params);
        methodDescribeModel.setName(method.getName());
        CollectDataBaseModel model = CollectDataBaseModel.init(AwCollectManager.get() != null,
                methodDescribeModel,
                ConstantAppNode.REDIS_TEMPLATE,
                Thread.currentThread().getName(),
                AwCollectManager.getTraceId());
        AwCollectManager.createOrAdd(model);
    }

    @Override
    public Object doAfter(Method method, Object[] params, Object instance,Class<?> clazz, Callable<Object> callable, Object result) {
        CollectDataBaseModel model = AwCollectManager.getNode(ConstantAppNode._SQL_DRIVE_CONNECT);
        assert model != null;
        MethodDescribeModel  methodDescribeModel= (MethodDescribeModel) model.getData();
        methodDescribeModel.setEndTime(TimeUtil.getCurrentTimeNano());
        methodDescribeModel.setReturnClazz(result.getClass().getName());
        return null;
    }

    @Override
    public void doCatch(Throwable e, Method method, Object[] params, Object instance,Class<?> clazz, Callable<Object> callable) {
        CollectDataBaseModel model = AwCollectManager.getNode(ConstantAppNode._REDIS_TEMPLATE);
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
        CollectDataBaseModel model = AwCollectManager.getNode(ConstantAppNode._REDIS_TEMPLATE);
        assert model != null;
        model.setEndTime(TimeUtil.getCurrentTimeNano());
        if (AwCollectManager.isExist()) {
            if (!AwCollectManager.get().get(0).isWeb()) {
                AwCollectManager.finish();
            }
        }
    }
}
