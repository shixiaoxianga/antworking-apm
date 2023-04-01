package com.antworking.core.enhance;

import com.antworking.common.AwConstant;
import com.antworking.core.interceptor.AntWorkingDynamicVariable;
import com.antworking.core.interceptor.AwObjectMethodIntercept;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.utility.JavaModule;

import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;

public abstract class AbstractDynamicVariableEnhanceStatement implements AwEnhanceStatement {

    @Override
    public ElementMatcher<? super TypeDescription> matcherClass() {
        return doMatcherClass();
    }

    public ElementMatcher<? super MethodDescription> matcherMethod() {
        return doMatcherMethod();
    }

    public abstract ElementMatcher<? super TypeDescription> doMatcherClass();

    public abstract ElementMatcher<? super MethodDescription> doMatcherMethod();

    @Override
    public DynamicType.Builder<?> define(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {
        builder = builder
                .defineField(AwConstant.VARIABLE_NAME, AntWorkingDynamicVariable.class, Modifier.PUBLIC);
        return doDefine(builder, typeDescription, classLoader, module, protectionDomain);
    }

    public abstract DynamicType.Builder<?> doDefine(DynamicType.Builder<?> builder,
                                                    TypeDescription typeDescription,
                                                    ClassLoader classLoader,
                                                    JavaModule module,
                                                    ProtectionDomain protectionDomain);

    public String defaultInterceptMethodName() {
        return "interceptor";
    }
}