package com.antworking.apm;


import net.bytebuddy.implementation.bind.annotation.*;

import java.util.concurrent.Callable;

public class JdkInterceptor {

    @RuntimeType
    @BindingPriority(Integer.MAX_VALUE)
    public Object interceptor(@AllArguments Object[] args, @SuperCall Callable<?> callable) {
        System.out.println("proxy...");
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}