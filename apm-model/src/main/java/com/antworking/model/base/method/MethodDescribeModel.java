package com.antworking.model.base.method;

import com.antworking.model.base.error.ErrorDescribeModel;
import com.google.gson.Gson;

import java.util.Arrays;

public class MethodDescribeModel {

    private String name;

    private String[] param;

    private String clazz;

    private boolean crux = false;

    private Object data;

    private String returnClazz;

    private String[] paramClazz;


    private ErrorDescribeModel error;

    public ErrorDescribeModel getError() {
        return error;
    }

    public void setError(ErrorDescribeModel error) {
        this.error = error;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isCrux() {
        return crux;
    }

    public void setCrux(boolean crux) {
        this.crux = crux;
    }

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
    public void setParam(Object[] args) {
        this.param = Arrays.stream(args).map(Object::toString).toArray(String[]::new);
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
        return "MethodDescribeModel =====>  "+ new Gson().toJson(this);
    }

    public void setParamClazz(Class<?>[] parameterTypes) {
        this.paramClazz=Arrays.stream(parameterTypes).map(Object::toString).toArray(String[]::new);
    }
}
