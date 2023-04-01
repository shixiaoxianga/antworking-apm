package com.antworking.apm.interfaces;

import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.concurrent.Callable;

public class InterceptorObject {

    @RuntimeType
    public Object interceptor(@This Object obj, @AllArguments Object[] args, @SuperCall Callable<?> callable) {
        System.out.println("object proxy...");
        try {
            Field aw = obj.getClass().getDeclaredField("oaw");
            aw.set(obj,new IAwCollectHandler());
            AwCollectHandler o = (AwCollectHandler) aw.get(obj);
            o.set("lll");
            System.out.println(o.get());
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}