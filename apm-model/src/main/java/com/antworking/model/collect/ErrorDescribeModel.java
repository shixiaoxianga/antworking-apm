package com.antworking.model.collect;

import com.antworking.util.JsonUtil;
import com.google.gson.Gson;

import java.util.Arrays;

public class ErrorDescribeModel {

    private String[] stacks;

    private String message;

    private String clazz;

    private Long timeStamp;


    public String[] getStacks() {
        return stacks;
    }

    public void setStacks(String[] stacks) {
        this.stacks = stacks;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return JsonUtil.toJsonString(this);
    }

    public void setStacks(StackTraceElement[] stackTrace) {
        this.stacks = Arrays.stream(stackTrace).map(Object::toString).toArray(String[]::new);
    }

}