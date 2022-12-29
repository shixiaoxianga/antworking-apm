package com.antworking.core.enhance;

import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;

public abstract class AbstractEnhanceStatement implements EnhanceStatement {

    @Override
    public ElementMatcher<? super TypeDescription> matcherClass() {
        return doMatcherClass();
    }

    public abstract ElementMatcher<? super TypeDescription> doMatcherClass();

    @Override
    public DynamicType.Builder<?> define(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {
        return doDefine(builder, typeDescription, classLoader, module, protectionDomain);
    }

    public abstract DynamicType.Builder<?> doDefine(DynamicType.Builder<?> builder,
                                                    TypeDescription typeDescription,
                                                    ClassLoader classLoader,
                                                    JavaModule module,
                                                    ProtectionDomain protectionDomain);
}
