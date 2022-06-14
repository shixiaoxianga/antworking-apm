package com.antworking.model.base.method;

import java.util.Arrays;

public class MethodDescribeModel {

    private String name;

    private String[] param;
    private String clazz;

    private String returnClazz;

    private String[] paramClazz;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getParam() {
        return param;
    }

    public void setParam(String[] param) {
        this.param = param;
    }

    public String getReturnClazz() {
        return returnClazz;
    }

    public void setReturnClazz(String returnClazz) {
        this.returnClazz = returnClazz;
    }

    public String[] getParamClazz() {
        return paramClazz;
    }

    public void setParamClazz(String[] paramClazz) {
        this.paramClazz = paramClazz;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    @Override
    public String toString() {
        return "MethodDescribeModel{" +
                "name='" + name + '\'' +
                ", param=" + Arrays.toString(param) +
                ", clazz='" + clazz + '\'' +
                ", returnClazz='" + returnClazz + '\'' +
                ", paramClazz=" + Arrays.toString(paramClazz) +
                '}';
    }
}
