package com.antworking.core.interceptor;

import com.antworking.logger.AwLog;
import com.antworking.logger.LoggerFactory;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class AwMethodIntercept {
    private final AwLog log = LoggerFactory.getLogger(AwMethodIntercept.class);

    @RuntimeType
    public Object interceptor(@Origin Method method,
                              @AllArguments Object[] args,
                              @Origin Class<?> clazz,
                              @SuperCall Callable<Object> callable) throws Exception {
        log.info("methodName :{} args:{} clazz:{}", method.getName(), Arrays.toString(args), clazz);
        return callable.call();
    }
}
