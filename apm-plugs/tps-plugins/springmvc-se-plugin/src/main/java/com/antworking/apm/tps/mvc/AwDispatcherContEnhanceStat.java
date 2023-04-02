package com.antworking.apm.tps.mvc;

import com.antworking.core.enhance.AbstractCustomEnhanceStatement;
import com.antworking.core.enhance.AbstractDynamicVariableEnhanceStatement;
import com.antworking.core.interceptor.AbstractConstructorMethodInterceptHandler;
import com.antworking.core.interceptor.AwConstructorMethodIntercept;
import com.antworking.core.interceptor.AwObjectMethodIntercept;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.reflect.Constructor;
import java.security.ProtectionDomain;

public class AwDispatcherContEnhanceStat extends AbstractCustomEnhanceStatement {
    @Override
    public ElementMatcher<? super TypeDescription> doMatcherClass() {
        return ElementMatchers.named("org.springframework.web.servlet.DispatcherServlet");
    }


    @Override
    public DynamicType.Builder<?> doDefine(DynamicType.Builder<?> builder,
                                           TypeDescription typeDescription,
                                           ClassLoader classLoader,
                                           JavaModule module,
                                           ProtectionDomain protectionDomain) {
//        return builder;
        Class<?> clazz = null;
        try {
            clazz = Class.forName("org.springframework.web.servlet.DispatcherServlet");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return builder.method(ElementMatchers.isConstructor().and(ElementMatchers.not(ElementMatchers.isDeclaredBy(Object.class))))
                .intercept(MethodDelegation.to(new AwConstructorMethodIntercept(new AwDispatchContMethodIntercept())));
    }
}
