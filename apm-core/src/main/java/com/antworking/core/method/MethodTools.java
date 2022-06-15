package com.antworking.core.method;

import com.antworking.model.base.BaseCollectModel;
import com.antworking.model.base.error.ErrorDescribeModel;
import com.antworking.model.base.method.MethodDescribeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;

public enum MethodTools {
    INSTANCE;

    private static final Logger log = LoggerFactory.getLogger(MethodTools.class);
    public MethodDescribeModel buildMethodDes(MethodDescribeModel methodDescribeModel,
                                              Class<?> clazz,
                                              Object[] args,
                                              Method method) {
        methodDescribeModel.setClazz(clazz.getName());
        methodDescribeModel.setName(method.getName());
        if (args.length > 0
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
        return buildMethodDes(new MethodDescribeModel(),clazz,args,method);
    }


    public void end(Throwable e, BaseCollectModel model, Object target, Object[] args, Method method){
        end(e,model,target.getClass(),args,method);
    }
    public void end(Throwable e, BaseCollectModel model, Class<?> target, Object[] args, Method method){
        model.setEndTime(System.currentTimeMillis());
        if(e!=null){
            ErrorDescribeModel error = new ErrorDescribeModel();
            error.setTimeStamp(System.currentTimeMillis());
            error.setStacks(Arrays.stream(e.getStackTrace()).map(Object::toString).toArray(String[]::new));
            error.setMessage(e.getMessage());
            error.setClazz(e.getClass().getName());

            MethodDescribeModel methodDescribeModel =
                    MethodTools.INSTANCE.buildMethodDes(target.getClass(),args,method);
            methodDescribeModel.setError(error);
            model.putMethods(methodDescribeModel);
        }
        log.info(model.toString());
    }
}
