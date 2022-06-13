package com.antworking.bytebuddy.annotations;

import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class SimpleMethodIntercept {

    @RuntimeType
    public Object enhance(@SuperMethod Method superMethod, @Origin Method method,
                          @AllArguments Object[] args , @This Object obj ,
                          @SuperCall Callable<?> call, @Origin Class <?> clazz){
        final long l = System.currentTimeMillis();

        System.out.println("原生class："+clazz.getName());
        System.out.println("增强方法执行"+superMethod.getName()+"");
        System.out.println("原生方法执行"+method.getName()+"");
        System.out.println("参数："+Arrays.asList(args));
        try{
            final Object o = call.call();
            System.out.println("原生方法执行结果："+o);
            return o;
        }catch (Throwable e){
            System.out.println("出错了");
        }finally {
            System.out.println("耗时："+ (System.currentTimeMillis() - l));
            System.out.println("结束了");
            System.out.println("-------------------------------------------------------");
        }
        return null;
    }

}
