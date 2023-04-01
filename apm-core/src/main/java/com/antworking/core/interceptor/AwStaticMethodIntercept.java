package com.antworking.core.interceptor;

import com.antworking.logger.AwLog;
import com.antworking.logger.LoggerFactory;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class AwStaticMethodIntercept {
    private final AwLog log = LoggerFactory.getLogger(AwStaticMethodIntercept.class);

    AwStaticMethodInterceptHandler handler;

    public AwStaticMethodIntercept(AwStaticMethodInterceptHandler handler) {
        this.handler = handler;
    }

    @RuntimeType
    public Object interceptor(@Origin Method method,
                              @AllArguments Object[] args,
                              @Origin Class<?> clazz,
                              @SuperCall Callable<Object> callable) throws Exception {
        handler.before(method, args, clazz, callable);
        Object result;
        try {
            result = callable.call();
            Object newRes = handler.after(method, args, clazz, callable, result);
            return newRes == null ? result : newRes;
        } catch (Throwable e) {
            handler._catch(e, method, args, clazz, callable);
            throw e;
        } finally {
            handler._final(method, args, clazz, callable);
        }
    }
}
