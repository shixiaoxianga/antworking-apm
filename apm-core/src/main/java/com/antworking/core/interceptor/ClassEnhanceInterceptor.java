package com.antworking.core.interceptor;

import com.antworking.core.AntWorkingContextManager;
import com.antworking.core.enhance.AbstractClassEnhance;
import com.antworking.model.base.BaseCollectModel;
import com.antworking.model.base.error.ErrorDescribeModel;
import com.antworking.model.base.method.MethodDescribeModel;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.Callable;

/**
 * @author XiangXiaoWei
 * date 2022/6/11
 */
//@Slf4j
public class ClassEnhanceInterceptor {
    private static final Logger log = LoggerFactory.getLogger(ClassEnhanceInterceptor.class);

    private static AbstractClassEnhance enhance;

    public ClassEnhanceInterceptor(AbstractClassEnhance enhance) {
        ClassEnhanceInterceptor.enhance = enhance;
    }

    @RuntimeType
    public Object interceptor(@Origin Method method,
                              @AllArguments Object[] args,
                              @Origin Class<?> clazz,
                              @SuperCall Callable<Object> callable) {
        Object result = null;
        final BaseCollectModel model = beforeBuild(method, args, clazz);
        enhance.invokeMethodBefore(clazz, method, args, model);
        try {
            result = callable.call();
            return result;
        } catch (Throwable e) {
            enhance.invokeMethodException(clazz, method, args, e, afterBuild(method,args,clazz,model,e));
            log.error("invoker class :{}  method : {} args: {} error:{}", clazz.getName(), method.getName(), Arrays.toString(args), e);
        } finally {
            enhance.invokeMethodAfter(clazz, method, args, result, afterBuild(method,args,clazz,model,null));
            log.info("执行 {} {} 方法 参数：{}", clazz.getName(), method.getName(), Arrays.toString(args));
        }
        return null;
    }

    private BaseCollectModel beforeBuild(Method method, Object[] args, Class<?> clazz) {
        BaseCollectModel model = new BaseCollectModel();
        model.setOrder(AntWorkingContextManager.getOrder());
        model.setStartTime(System.currentTimeMillis());
        MethodDescribeModel methodDescribeModel = new MethodDescribeModel();
        methodDescribeModel.setName(method.getName());
        methodDescribeModel.setClazz(clazz.getName());
        if(args.length>0&&args[0]!=null){
            methodDescribeModel.setParam(Arrays.stream(args).map(Object::toString).toArray(String[]::new));
        }
        methodDescribeModel.setParamClazz(Arrays.stream(method.getParameterTypes()).map(Object::toString).toArray(String[]::new));
        methodDescribeModel.setReturnClazz(method.getReturnType().toString());
        model.setMethod(methodDescribeModel);
        return model;
    }
    private BaseCollectModel afterBuild(Method method, Object[] args, Class<?> clazz,BaseCollectModel model,Throwable e) {
        model.setEndTime(System.currentTimeMillis());
        if(e!=null){
            ErrorDescribeModel error = new ErrorDescribeModel();
            error.setTimeStamp(System.currentTimeMillis());
            error.setStacks(Arrays.stream(e.getStackTrace()).map(Object::toString).toArray(String[]::new));
            error.setMessage(e.getMessage());
            error.setClazz(e.getClass().getName());
            model.setError(error);
        }
        return model;
    }
}
