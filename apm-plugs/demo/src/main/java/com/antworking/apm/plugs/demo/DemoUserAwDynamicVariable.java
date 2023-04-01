package com.antworking.apm.plugs.demo;

import com.antworking.core.interceptor.AbstractAntWorkingDynamicVariable;

public class DemoUserAwDynamicVariable extends AbstractAntWorkingDynamicVariable {
    private String str;
    @Override
    public void set0(Object o) {
        str = (String) o;
    }

    @Override
    public Object get0() {
        return str;
    }


}
