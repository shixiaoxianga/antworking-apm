package com.antworking.bytebuddy.jdk;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class GeneralInterceptor {


    @RuntimeType
    public Object intercept(@AllArguments Object[] allArguments,
                            @Origin Method method, @SuperCall Callable<?> callable) throws Exception {

        System.out.println(method.getName());
        System.out.println(Arrays.toString(allArguments));
        return callable.call();
    }
}