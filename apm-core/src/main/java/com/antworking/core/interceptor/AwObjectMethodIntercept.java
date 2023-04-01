package com.antworking.core.interceptor;

import com.antworking.common.AwConstant;
import com.antworking.logger.AwLog;
import com.antworking.logger.LoggerFactory;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class AwObjectMethodIntercept {
    private final AwLog log = LoggerFactory.getLogger(AwObjectMethodIntercept.class);

    AwObjectMethodInterceptHandler handler;

    public AwObjectMethodIntercept(AwObjectMethodInterceptHandler handler) {
        this.handler = handler;
    }

    @RuntimeType
    public Object interceptor(@Origin Method method,
                              @This Object _this,
                              @AllArguments Object[] args,
                              @Origin Class<?> clazz,
                              @SuperCall Callable<Object> callable) throws Exception {
        AntWorkingDynamicVariable variable =(AntWorkingDynamicVariable) _this.getClass().getDeclaredField(AwConstant.VARIABLE_NAME).get(_this);
        handler.before(method, _this, args, clazz, callable,variable);
        Object result;
        try {
            result = callable.call();
            Object newRes = handler.after(method, _this, args, clazz, callable, result,variable);
            return newRes == null ? result : newRes;
        } catch (Throwable e) {
            handler._catch(e, _this, method, args, clazz, callable,variable);
            throw e;
        } finally {
            handler._final(method, _this, args, clazz, callable,variable);
        }
    }
}
