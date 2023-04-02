package com.antworking.apm.tps.mvc;

import com.antworking.core.enhance.AbstractDynamicVariableEnhanceStatement;
import com.antworking.core.interceptor.AwObjectMethodIntercept;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;

public class AwDispatcherMethodEnhanceStat extends AbstractDynamicVariableEnhanceStatement {
    @Override
    public ElementMatcher<? super TypeDescription> doMatcherClass() {
        return ElementMatchers.named("org.springframework.web.servlet.DispatcherServlet");
    }

    @Override
    public ElementMatcher<? super MethodDescription> doMatcherMethod() {
        return ElementMatchers.named("doService");
    }

    @Override
    public DynamicType.Builder<?> doDefine(DynamicType.Builder<?> builder,
                                           TypeDescription typeDescription,
                                           ClassLoader classLoader,
                                           JavaModule module,
                                           ProtectionDomain protectionDomain) {
        return builder.method(doMatcherMethod()).intercept(MethodDelegation.to(new AwObjectMethodIntercept(new AwDispatchMethodIntercept())));
    }
}
