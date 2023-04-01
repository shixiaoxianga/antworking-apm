package com.antworking.core.interceptor;

public abstract class AbstractAntWorkingDynamicVariable implements AntWorkingDynamicVariable {
    @Override
    public void set(Object o) {
        set0(o);
    }

    @Override
    public Object get() {
        return get0();
    }

    @Override
    public void write() {

    }

    public abstract void set0(Object o);
    public abstract Object get0();
}
