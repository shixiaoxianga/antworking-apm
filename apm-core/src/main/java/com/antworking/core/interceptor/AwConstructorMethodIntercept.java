package com.antworking.core.interceptor;

import com.antworking.common.AwConstant;
import com.antworking.logger.AwLog;
import com.antworking.logger.LoggerFactory;
import net.bytebuddy.implementation.bind.annotation.*;

import javax.swing.text.html.ObjectView;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class AwConstructorMethodIntercept {
    private final AwLog log = LoggerFactory.getLogger(AwConstructorMethodIntercept.class);

    AwConstructorMethodInterceptHandler handler;

    public AwConstructorMethodIntercept(AwConstructorMethodInterceptHandler handler) {
        this.handler = handler;
    }

    @RuntimeType
    public void interceptor(
            @This Object _this,
            @AllArguments Object[] args,
            @Origin Class<?> clazz,
            @SuperCall Callable<Object> callable) throws Exception {
        handler.before(_this, args, clazz, callable);
        try {
            callable.call();
            handler.after(_this, args, clazz, callable);
        } catch (Throwable e) {
            handler._catch(_this, e, args, clazz, callable);
            throw e;
        } finally {
            handler._final(_this, args, clazz, callable);
        }
    }
}
