package com.antworking.core.enhance;

import com.antworking.core.matchers.AbstractMethodMatchers;
import com.antworking.model.base.BaseCollectModel;
import com.antworking.model.base.method.MethodDescribeModel;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author XiangXiaoWei
 * date 2022/6/11
 */
public abstract class AbstractClassEnhance {


    public abstract void init();

    public abstract String interceptorClass();

    public abstract ElementMatcher<? super MethodDescription> buildMethodMatchers();

    public abstract ElementMatcher<? super TypeDescription> buildTypeMatchers();


    public abstract BaseCollectModel invokeMethodBefore(Class<?> clazz,
                                            Method method,
                                            Object[] args,
                                            BaseCollectModel model,
                                            MethodDescribeModel methodDescribeModel);

    public abstract Object invokeMethodAfter(Class<?> clazz,
                                             Method method,
                                             Object[] args,
                                             Object result,
                                             BaseCollectModel model,
                                             MethodDescribeModel methodDescribeModel);

    public abstract void invokeMethodException(Class<?> clazz,
                                               Method method,
                                               Object[] args,
                                               Throwable e,
                                               BaseCollectModel model,
                                               MethodDescribeModel methodDescribeModel);


    protected ElementMatcher<? super MethodDescription> arrayToMatcher(String[] methodNames) {
        ElementMatcher<? super MethodDescription> matchers = null;
        for (String methodName : methodNames) {
            matchers = ElementMatchers.named(methodName);
        }
        return matchers;
    }


}
