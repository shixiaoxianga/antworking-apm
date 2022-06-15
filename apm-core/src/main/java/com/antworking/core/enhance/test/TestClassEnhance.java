package com.antworking.core.enhance.test;

import com.antworking.core.enhance.AbstractClassEnhance;
import com.antworking.core.matchers.AbstractMethodMatchers;
import com.antworking.model.base.BaseCollectModel;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class TestClassEnhance extends AbstractClassEnhance {

    private String CLASS_NAME="com.xxw.test.controller.IndexController";


    @Override
    public String getClassName() {
        return CLASS_NAME;
    }

    @Override
    public ElementMatcher<? super MethodDescription> buildMethodMatchers() {
        return ElementMatchers.any();
    }

    @Override
    public void invokeMethodBefore(Class<?> clazz, Method method, Object[] args, BaseCollectModel model) {

    }

    @Override
    public Object invokeMethodAfter(Class<?> clazz, Method method, Object[] args, Object result, BaseCollectModel model) {
        System.out.println(result);
        return result;
    }

    @Override
    public void invokeMethodException(Class<?> clazz, Method method, Object[] args, Throwable e, BaseCollectModel model) {

    }


}
