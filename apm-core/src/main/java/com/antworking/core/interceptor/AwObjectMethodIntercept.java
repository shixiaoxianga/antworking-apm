package com.antworking.core.interceptor;

import com.antworking.common.AwConstant;
import com.antworking.core.collect.AwCollectManager;
import com.antworking.logger.AwLog;
import com.antworking.logger.LoggerFactory;
import com.antworking.util.ClassUtil;
import com.antworking.util.UuidUtil;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.UUID;
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
        String nodeId = UuidUtil.getId();
        handler.before(method, _this, args, clazz, callable, ClassUtil.getVariable(_this, true),nodeId);
        Object result;
        try {
            result = callable.call();
            Object newRes = handler.after(method,
                    _this,
                    args,
                    clazz,
                    callable,
                    result,
                    ClassUtil.getVariable(_this, true),nodeId);
            return newRes == null ? result : newRes;
        } catch (Throwable e) {
            handler._catch(e, _this,
                    method, args,
                    clazz, callable,
                    ClassUtil.getVariable(_this, true),nodeId);
            throw e;
        } finally {
            handler._final(method, _this,
                    args, clazz,
                    callable,
                    ClassUtil.getVariable(_this, true),nodeId);
        }
    }
}
