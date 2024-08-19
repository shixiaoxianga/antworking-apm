package com.antworking.plugin.httpclient;

import com.antworking.core.enhance.AbstractEnhanceStatement;
import com.antworking.core.interceptor.AwMethodIntercept;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;

public class ApacheHttpClientEnhanceStatementImpl extends AbstractEnhanceStatement {
    private final static String[] _class = {
            "org.apache.http.impl.client.CloseableHttpClient",
    };
    private final static String[] _methods = new String[]{"execute"};

    @Override
    public ElementMatcher<? super TypeDescription> doMatcherClass() {
        return ElementMatchers.named(_class[0]);
    }

    @Override
    public DynamicType.Builder<?> doDefine(DynamicType.Builder<?> builder,
                                         TypeDescription typeDescription,
                                         ClassLoader classLoader,
                                         JavaModule module,
                                         ProtectionDomain protectionDomain) {
        return builder.method(ElementMatchers.named(_methods[0]).and(ElementMatchers.takesArguments(2)))
                .intercept(MethodDelegation.withDefaultConfiguration()
                        .filter(ElementMatchers.named(defaultInterceptMethodName()))
                        .to(new AwMethodIntercept(new ApacheHttpClientInterceptMethodHandler())));
    }
}
