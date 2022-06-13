package com.antworking.core.interceptor;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * @author XiangXiaoWei
 * date 2022/6/11
 */
//@Slf4j
public class ClassEnhanceInterceptor {
    private static final Logger log = LoggerFactory.getLogger(ClassEnhanceInterceptor.class);

    @RuntimeType
    public Object interceptor(@Origin Method method,
                              @AllArguments Object[] args,
                              @Origin Class<?> clazz,
                              @SuperCall Callable<Object> callable) {

        final long l = System.currentTimeMillis();
        try {
            return callable.call();
        } catch (Throwable e) {
            log.error("invoker class :{}  method : {} args: {} error:{}", clazz.getName(), method.getName(), Arrays.asList(args),e);
        } finally {
            log.info("执行 {} {} 方法 参数：{} 耗时：{} ms",clazz.getName(),method.getName(),Arrays.asList(args),System.currentTimeMillis()-l);
        }
        return null;
    }

}
