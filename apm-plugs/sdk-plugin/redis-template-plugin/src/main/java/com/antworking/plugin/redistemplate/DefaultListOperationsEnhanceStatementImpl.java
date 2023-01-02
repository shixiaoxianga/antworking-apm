package com.antworking.plugin.redistemplate;

import com.antworking.core.enhance.AbstractEnhanceStatement;
import com.antworking.core.interceptor.AwMethodIntercept;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;

public class DefaultListOperationsEnhanceStatementImpl extends AbstractEnhanceStatement {

    @Override
    public ElementMatcher<? super TypeDescription> doMatcherClass() {
        return ElementMatchers.named("org.springframework.data.redis.core.DefaultListOperations");
    }

    @Override
    public DynamicType.Builder<?> doDefine(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {
        return builder.method(ElementMatchers.any())
                .intercept(MethodDelegation.withDefaultConfiguration()
                        .filter(ElementMatchers.named(defaultInterceptMethodName()))
                        .to(new AwMethodIntercept(new CommonMethodHandler())));
    }
}
