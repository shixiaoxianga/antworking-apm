package com.antworking.plugin.tomcat;


import com.antworking.core.enhance.AbstractEnhanceStatement;
import com.antworking.core.interceptor.AwMethodIntercept;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;

public class TomcatServletEnhanceStatementImpl extends AbstractEnhanceStatement {
    private final String CLASS_NAME = "javax.servlet.http.HttpServlet";

    @Override
    public ElementMatcher<? super TypeDescription> doMatcherClass() {
        return ElementMatchers.named(CLASS_NAME);
    }

    @Override
    public DynamicType.Builder<?> doDefine(DynamicType.Builder<?> builder,
                                           TypeDescription typeDescription,
                                           ClassLoader classLoader,
                                           JavaModule module,
                                           ProtectionDomain protectionDomain) {
        return builder.method(ElementMatchers.named("service").and(ElementMatchers.isProtected()))
                .intercept(MethodDelegation.withDefaultConfiguration()
                        .filter(ElementMatchers.named(defaultInterceptMethodName()))
                        .to(new AwMethodIntercept(new TomcatHttpServletInterceptMethodHandler())));
    }
}
