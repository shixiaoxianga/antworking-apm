package com.antworking.apm;

import com.antworking.core.enhance.AbstractEnhanceStatement;
import com.antworking.core.enhance.EnhanceStatement;
import com.antworking.core.interceptor.AwMethodIntercept;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;

public class TestEnhanceStatement extends AbstractEnhanceStatement {
    @Override
    public ElementMatcher<? super TypeDescription> doMatcherClass() {
        return ElementMatchers.named("com.xxw.test.controller.IndexController");
    }

    @Override
    public DynamicType.Builder<?> doDefine(DynamicType.Builder<?> builder,
                                           TypeDescription typeDescription,
                                           ClassLoader classLoader,
                                           JavaModule module,
                                           ProtectionDomain protectionDomain) {
        return builder.method(ElementMatchers.any())
                .intercept(MethodDelegation.to(AwMethodIntercept.class));
    }

    public static void main(String[] args) throws Exception {
        EnhanceStatement instance = (EnhanceStatement) Class.forName("com.antworking.apm.TestEnhanceStatement").newInstance();
        System.out.println(instance.matcherClass());
    }
}
