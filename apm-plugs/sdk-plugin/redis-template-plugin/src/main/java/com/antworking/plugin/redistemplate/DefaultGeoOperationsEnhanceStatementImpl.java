package com.antworking.plugin.redistemplate;

import com.antworking.core.enhance.AbstractEnhanceStatement;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;

public class DefaultGeoOperationsEnhanceStatementImpl extends AbstractEnhanceStatement {

    @Override
    public ElementMatcher<? super TypeDescription> doMatcherClass() {
        return null;
    }

    @Override
    public DynamicType.Builder<?> doDefine(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {
        return null;
    }
}
