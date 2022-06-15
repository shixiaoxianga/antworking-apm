package com.antworking.core.enhance;

import com.antworking.core.matchers.AbstractMethodMatchers;
import com.antworking.model.base.BaseCollectModel;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author XiangXiaoWei
 * date 2022/6/11
 */
public abstract class AbstractClassEnhance {


    public abstract String getClassName();

    public abstract ElementMatcher<? super MethodDescription> buildMethodMatchers();


    public abstract void invokeMethodBefore(Class<?> clazz,
                                            Method method,
                                            Object[] args,
                                            BaseCollectModel model);

    public abstract Object invokeMethodAfter(Class<?> clazz,
                                           Method method,
                                           Object[] args,
                                           Object result,
                                           BaseCollectModel model);

    public abstract void invokeMethodException(Class<?> clazz,
                                               Method method,
                                               Object[] args,
                                               Throwable e,
                                               BaseCollectModel model);


    protected ElementMatcher<? super MethodDescription> arrayToMatcher(String[] methodNames) {
        ElementMatcher<? super MethodDescription> matchers = null;
        for (String methodName : methodNames) {
            matchers = ElementMatchers.named(methodName);
        }
        return matchers;
    }


}
