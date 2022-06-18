package com.antworking.core.enhance.http.java_net;

import com.antworking.core.enhance.AbstractClassEnhance;
import com.antworking.model.base.BaseCollectModel;
import com.antworking.model.base.method.MethodDescribeModel;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Method;

/**
 * @author XiangXiaoWei
 * date 2022/6/18
 */
public class HttpUrlConnectionClassEnhance extends AbstractClassEnhance {


    @Override
    public String getClassName() {
        return "sun.net.www.protocol.http.HttpURLConnection";
    }

    @Override
    public ElementMatcher<? super MethodDescription> buildMethodMatchers() {
        return ElementMatchers.named("connect").and(ElementMatchers.named("getHeaderField"));
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
