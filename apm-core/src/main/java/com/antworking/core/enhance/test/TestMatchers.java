package com.antworking.core.enhance.test;

import com.antworking.core.matchers.AbstractMethodMatchers;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

public class TestMatchers extends AbstractMethodMatchers {
    @Override
    public ElementMatcher<? super MethodDescription> buildMethodMatchers() {
        return ElementMatchers.named("test2").or( ElementMatchers.named("test1"));
    }
}
