package com.antworking.plugin.jdk.http;

import com.antworking.core.enhance.AbstractClassEnhance;
import com.antworking.model.base.BaseCollectModel;
import com.antworking.model.base.method.MethodDescribeModel;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Method;

/**
 * @author XiangXiaoWei
 * date 2022/6/18
 */
public class HttpUrlConnectionClassEnhance extends AbstractClassEnhance {



    @Override
    public void init() {
    }

    @Override
    public String interceptorClass() {
        return "com.antworking.plugin.jdk.http.HttpURLConnectionMethodInterceptor";
    }

    @Override
    public ElementMatcher<? super MethodDescription> buildMethodMatchers() {
//        return ElementMatchers.named("connect");
        return ElementMatchers.any();
    }

    @Override
    public ElementMatcher<? super TypeDescription> buildTypeMatchers() {
        return ElementMatchers.nameEndsWith(".HttpURLConnection");
    }

    @Override
    public BaseCollectModel invokeMethodBefore(Class<?> clazz, Method method, Object[] args, BaseCollectModel model, MethodDescribeModel methodDescribeModel) {
        return null;
    }

    @Override
    public Object invokeMethodAfter(Class<?> clazz, Method method, Object[] args, Object result, BaseCollectModel model, MethodDescribeModel methodDescribeModel) {
        return null;
    }

    @Override
    public void invokeMethodException(Class<?> clazz, Method method, Object[] args, Throwable e, BaseCollectModel model, MethodDescribeModel methodDescribeModel) {

    }
}
