package com.antworking.apm;


import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.BindingPriority;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class JdkAdvice {

    @Advice.OnMethodEnter
    public static void before(@Advice.This Object _this) {
        System.out.printf("_this: %s",_this);
    }
}