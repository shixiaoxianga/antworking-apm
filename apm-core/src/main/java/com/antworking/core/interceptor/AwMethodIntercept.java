package com.antworking.core.interceptor;

import com.antworking.core.handler.IMethodInterceptHandler;
import com.antworking.logger.AwLog;
import com.antworking.logger.LoggerFactory;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class AwMethodIntercept {
    private final AwLog log = LoggerFactory.getLogger(AwMethodIntercept.class);

    IMethodInterceptHandler handler;

    public AwMethodIntercept(IMethodInterceptHandler handler) {
        this.handler = handler;
    }

    @RuntimeType
    public Object interceptor(
                              @This Object instance,
                              @Origin Method method,
                              @AllArguments Object[] args,
                              @Origin Class<?> clazz,
                              @SuperCall Callable<Object> callable) throws Exception {
        handler.before(method, args, instance,clazz, callable);
        Object result;
        try {
            result = callable.call();
            Object newRes = handler.after(method, args,instance, clazz, callable, result);
            return newRes == null ? result : newRes;
        } catch (Throwable e) {
            handler._catch(e, method, args, instance,clazz, callable);
            throw e;
        } finally {
            handler._final(method, args,instance, clazz, callable);
        }
    }
}
