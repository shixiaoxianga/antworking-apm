package com.antworking.apm;

import com.antworking.core.enhance.AbstractEnhanceStatement;
import com.antworking.core.enhance.EnhanceStatement;
import com.antworking.core.interceptor.AwMethodIntercept;
import com.antworking.logger.AwLog;
import com.antworking.logger.LoggerFactory;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;

public class TestEnhanceStatement extends AbstractEnhanceStatement {
    private final AwLog log = LoggerFactory.getLogger(TestEnhanceStatement.class);
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
        log.debug("define class: {}",typeDescription.getName());
        return builder.method(ElementMatchers.any())
                .intercept(MethodDelegation.withDefaultConfiguration()
                        .filter(ElementMatchers.named("interceptor"))
                        .to(new AwMethodIntercept()));
    }

    public static void main(String[] args) throws Exception {
        EnhanceStatement instance = (EnhanceStatement) Class.forName("com.antworking.apm.TestEnhanceStatement").newInstance();
        System.out.println(instance.matcherClass());
    }
}
