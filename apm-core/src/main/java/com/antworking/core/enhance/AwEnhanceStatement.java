package com.antworking.core.enhance;

import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;

/**
 * @author AXiang
 * date 2022/12/29 15:26
 */
public interface AwEnhanceStatement {
    ElementMatcher<? super TypeDescription> matcherClass();

    DynamicType.Builder<?> define(DynamicType.Builder<?> builder,
                                  TypeDescription typeDescription,
                                  ClassLoader classLoader,
                                  JavaModule module,
                                  ProtectionDomain protectionDomain);
}
