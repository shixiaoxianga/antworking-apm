package com.antworking.core.interceptor;

import com.antworking.core.AntWorkingContextManager;
import com.antworking.core.enhance.AbstractClassEnhance;
import com.antworking.core.method.MethodTools;
import com.antworking.model.base.BaseCollectModel;
import com.antworking.model.base.error.ErrorDescribeModel;
import com.antworking.model.base.method.MethodDescribeModel;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * @author XiangXiaoWei
 * date 2022/6/11
 */
//@Slf4j
public class ClassEnhanceInterceptor {
    private static final Logger log = LoggerFactory.getLogger(ClassEnhanceInterceptor.class);

    private static AbstractClassEnhance enhance;
    private Throwable e;
    public ClassEnhanceInterceptor(AbstractClassEnhance enhance) {
        ClassEnhanceInterceptor.enhance = enhance;
    }

    @RuntimeType
    public Object interceptor(@Origin Method method,
                              @AllArguments Object[] args,
                              @Origin Class<?> clazz,
                              @SuperCall Callable<Object> callable) {
        MethodDescribeModel methodDescribeModel = new MethodDescribeModel();
        Object result = null;
        final BaseCollectModel model = beforeBuild(method, args, clazz, methodDescribeModel);
        enhance.invokeMethodBefore(clazz, method, args, model);
        try {
            result = callable.call();
        } catch (Throwable e) {
            this.e=e;
            enhance.invokeMethodException(clazz, method, args, e, afterBuild(method, args, clazz, model, e, methodDescribeModel));
            log.debug("invoker class :{}  method : {} args: {} error:{}", clazz.getName(), method.getName(), Arrays.toString(args), e);
            e.printStackTrace();
        } finally {
            if(this.e==null){
                result = enhance.invokeMethodAfter(clazz, method, args, result, afterBuild(method, args, clazz, model, null, methodDescribeModel));
                log.debug("invoker {} {} method args：{}", clazz.getName(), method.getName(), Arrays.toString(args));
            }
        }
        return result;
    }

    private BaseCollectModel beforeBuild(Method method,
                                         Object[] args,
                                         Class<?> clazz,
                                         MethodDescribeModel methodDescribeModel) {
        BaseCollectModel model = new BaseCollectModel();
        model.setOrder(AntWorkingContextManager.getOrder());
        model.setStartTime(System.currentTimeMillis());
        methodDescribeModel.setCrux(true);
        MethodTools.INSTANCE.buildMethodDes(methodDescribeModel,clazz, args, method);
        return model;
    }

    private BaseCollectModel afterBuild(Method method,
                                        Object[] args,
                                        Class<?> clazz,
                                        BaseCollectModel model,
                                        Throwable e,
                                        MethodDescribeModel methodDescribeModel) {
        model.setEndTime(System.currentTimeMillis());
        if (e != null) {
            ErrorDescribeModel error = new ErrorDescribeModel();
            error.setTimeStamp(System.currentTimeMillis());
            error.setStacks(Arrays.stream(e.getStackTrace()).map(Object::toString).toArray(String[]::new));
            error.setMessage(e.getMessage());
            error.setClazz(e.getClass().getName());
            methodDescribeModel.setError(error);
        }
        model.putMethods(methodDescribeModel);
        return model;
    }
}
