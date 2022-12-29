package com.antworking.bytebuddy.jdk;


import net.bytebuddy.asm.Advice;

import java.lang.reflect.Method;

public class JdkAdviceTest {

    @Advice.OnMethodEnter
    public static void onMethodEnter(@Advice.Origin Method method) {
        System.out.println("advice proxy");
        System.out.println("methodName："+method.getName());
    }
}