package com.antworking.core.enhance.test;

import com.antworking.core.enhance.AbstractClassEnhance;
import com.antworking.core.matchers.AbstractMethodMatchers;

public class TestClassEnhance extends AbstractClassEnhance {

    private String CLASS_NAME="com.xxw.test.controller.IndexController";

    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public boolean isInterface() {
        return false;
    }

    @Override
    public String getClassName() {
        return CLASS_NAME;
    }

    @Override
    public AbstractMethodMatchers getMethodMatchers() {
        return new TestMatchers();
    }
}
