package com.antworking.apm.plugs.demo;

import com.antworking.common.AwConstant;
import com.antworking.core.enhance.AbstractDynamicVariableEnhanceStatement;
import com.antworking.core.interceptor.AntWorkingDynamicVariable;
import com.antworking.core.interceptor.AwObjectMethodIntercept;
import com.antworking.logger.AwLog;
import com.antworking.logger.LoggerFactory;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;

public class DemoUserAbstractEnhanceStatement extends AbstractDynamicVariableEnhanceStatement {
    AwLog log = LoggerFactory.getLogger(DemoUserAbstractEnhanceStatement.class);

    @Override
    public ElementMatcher<? super TypeDescription> doMatcherClass() {
        return ElementMatchers.named("com.xxw.test.controller.IndexController");
    }

    @Override
    public ElementMatcher<? super MethodDescription> doMatcherMethod() {
        return ElementMatchers.named("test2");
    }

    @Override
    public DynamicType.Builder<?> doDefine(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {
        log.debug("define DemoUserAbstractEnhanceStatement");
        return builder.method(matcherMethod())
                .intercept(MethodDelegation.to(new AwObjectMethodIntercept(new AwUserMethodIntercept())));
    }
}
