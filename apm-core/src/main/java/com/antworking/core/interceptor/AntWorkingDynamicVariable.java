package com.antworking.core.interceptor;

public interface AntWorkingDynamicVariable {
    void set(Object o);

    Object get();

    void write();
}
