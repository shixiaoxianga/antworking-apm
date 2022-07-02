package com.antworking.core.interceptor;

import com.antworking.core.AntWorkingContextManager;
import com.antworking.core.enhance.AbstractClassEnhance;
import com.antworking.core.tools.CollectionModelTools;
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

    private AbstractClassEnhance enhance;

    public ClassEnhanceInterceptor(AbstractClassEnhance enhance) {
        this.enhance = enhance;
    }

    @RuntimeType
    public Object interceptor(@Origin Method method,
                              @AllArguments Object[] args,
                              @Origin Class<?> clazz,
                              @SuperCall Callable<Object> callable) {
        Throwable throwable = null;
        MethodDescribeModel methodDescribeModel = new MethodDescribeModel();
        Object result = null;
        BaseCollectModel model = AntWorkingContextManager.get();
        model = enhance.invokeMethodBefore(clazz, method, args, model, methodDescribeModel);
        if(model==null){
            model = CollectionModelTools.INSTANCE.createBaseCollectModel(null, method, args, clazz, methodDescribeModel);
        }
        try {
            result = callable.call();
        } catch (Throwable e) {
            throwable = e;
            afterBuild(method, args, clazz, model, e, methodDescribeModel);
            enhance.invokeMethodException(clazz, method, args, e, model, methodDescribeModel);

            log.debug("invoker class :{}  method : {} args: {} error:{}", clazz.getName(), method.getName(), Arrays.toString(args), e);
            e.printStackTrace();
        } finally {
            if (throwable == null) {
                afterBuild(method, args, clazz, model, null, methodDescribeModel);
                result = enhance.invokeMethodAfter(clazz, method, args, result, model, methodDescribeModel);
                log.debug("invoker {} {} method args：{}", clazz.getName(), method.getName(), Arrays.toString(args));
            }
        }
        return result;
    }

    private Object call(Callable<Object> callable) {
        Object call = null;
        try {
            call = callable.call();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return call;
    }


    private void afterBuild(Method method,
                            Object[] args,
                            Class<?> clazz,
                            BaseCollectModel model,
                            Throwable e,
                            MethodDescribeModel methodDescribeModel) {
        model.setEndTime(System.currentTimeMillis());
        if (e != null) {
            ErrorDescribeModel error
                    = new ErrorDescribeModel();
            error.setTimeStamp(System.currentTimeMillis());
            error.setStacks(Arrays.stream(e.getStackTrace()).map(Object::toString).toArray(String[]::new));
            error.setMessage(e.getMessage());
            error.setClazz(e.getClass().getName());
            methodDescribeModel.setError(error);
        }
        model.putMethods(methodDescribeModel);
    }
}
