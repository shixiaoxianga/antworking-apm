package com.antworking.core.tools;

import com.antworking.core.AntWorkingContextManager;
import com.antworking.model.base.BaseCollectModel;
import com.antworking.model.base.error.ErrorDescribeModel;
import com.antworking.model.base.method.MethodDescribeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;

public enum CollectionModelTools {
    INSTANCE;

    private static final Logger log = LoggerFactory.getLogger(CollectionModelTools.class);

    //构建方法描述
    public MethodDescribeModel buildMethodDes(MethodDescribeModel methodDescribeModel,
                                              Class<?> clazz,
                                              Object[] args,
                                              Method method) {
        methodDescribeModel.setClazz(clazz.getName());
        methodDescribeModel.setName(method.getName());
        if (args != null && args.length > 0
                && args[0] != null) {
            methodDescribeModel.setParam(Arrays.stream(args).map(Object::toString).toArray(String[]::new));
        }
        methodDescribeModel.setParamClazz(Arrays.stream(method.getParameterTypes()).map(Object::toString).toArray(String[]::new));
        methodDescribeModel.setReturnClazz(method.getReturnType().toString());
        return methodDescribeModel;
    }

    public MethodDescribeModel buildMethodDes(Class<?> clazz,
                                              Object[] args,
                                              Method method) {
        return buildMethodDes(new MethodDescribeModel(), clazz, args, method);
    }


    public void totalEnd(Throwable e, BaseCollectModel model, Class<?> clazz, Object[] args, Method method) {
        model.setEndTime(System.currentTimeMillis());
        if (e != null) {
            ErrorDescribeModel error = new ErrorDescribeModel();
            error.setTimeStamp(System.currentTimeMillis());
            error.setStacks(Arrays.stream(e.getStackTrace()).map(Object::toString).toArray(String[]::new));
            error.setMessage(e.getMessage());
            error.setClazz(e.getClass().getName());

            MethodDescribeModel methodDescribeModel =
                    CollectionModelTools.INSTANCE.buildMethodDes(clazz, args, method);
            methodDescribeModel.setError(error);
            model.putMethods(methodDescribeModel);
        }
    }


    //节点采集结束
    public void childrenEnd(Throwable e, BaseCollectModel model, Object target, Object[] args, Method method) {
        childrenEnd(e, model, target.getClass(), args, method);
    }

    public void childrenEnd(Throwable e, BaseCollectModel model, Class<?> target, Object[] args, Method method) {
        model.setEndTime(System.currentTimeMillis());
        if (e != null) {
            ErrorDescribeModel error = new ErrorDescribeModel();
            error.setTimeStamp(System.currentTimeMillis());
            error.setStacks(Arrays.stream(e.getStackTrace()).map(Object::toString).toArray(String[]::new));
            error.setMessage(e.getMessage());
            error.setClazz(e.getClass().getName());

            MethodDescribeModel methodDescribeModel =
                    CollectionModelTools.INSTANCE.buildMethodDes(target.getClass(), args, method);
            methodDescribeModel.setError(error);
            model.putMethods(methodDescribeModel);
        }
        AntWorkingContextManager.linkModel(model);
        log.info("整个节点Model=====》" + AntWorkingContextManager.get());
    }


    public void createBaseCollectModel(BaseCollectModel model,
                                       Method method,
                                       Object[] args,
                                       Class<?> clazz,
                                       MethodDescribeModel methodDescribeModel) {
        if (methodDescribeModel != null) {
            methodDescribeModel.setCrux(true);
            CollectionModelTools.INSTANCE.buildMethodDes(methodDescribeModel, clazz, args, method);
        }
        model.setOrder(AntWorkingContextManager.getOrder());
        model.setStartTime(System.currentTimeMillis());
        //改为结束后link
//        AntWorkingContextManager.linkModel(model);
    }

    //此方法适用于采集开始
    public BaseCollectModel linkStartCreateBaseCollectModel(Method method,
                                                   Object[] args,
                                                   Class<?> clazz,
                                                   MethodDescribeModel methodDescribeModel) {
        methodDescribeModel.setCrux(true);
        CollectionModelTools.INSTANCE.buildMethodDes(methodDescribeModel, clazz, args, method);

        BaseCollectModel model = new BaseCollectModel();
        model.setOrder(AntWorkingContextManager.getOrder());
        model.setStartTime(System.currentTimeMillis());
        AntWorkingContextManager.set(model);
        return model;
    }

}
