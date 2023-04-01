package com.antworking.apm;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.util.concurrent.Callable;

public class Interceptor {

    @RuntimeType
    public Object interceptor(@AllArguments Object[] args, @SuperCall Callable<?> callable) {
        System.out.println("proxy...");
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}