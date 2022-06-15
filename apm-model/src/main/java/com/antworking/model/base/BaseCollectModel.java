package com.antworking.model.base;

import com.antworking.model.base.error.ErrorDescribeModel;
import com.antworking.model.base.method.MethodDescribeModel;
import com.google.gson.Gson;

/**
 * @author XiangXiaoWei
 * date 2022/6/11
 */
public class BaseCollectModel {
    private Long startTime;
    private Long endTime;
    private Long timeUse;
    private String host;
    private String appName;
    private Integer order;
    private Object data;
    private String node;
    private boolean crux;

    private MethodDescribeModel method;
    private ErrorDescribeModel error;


    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
        this.setTimeUse(endTime-this.startTime);
    }

    public Long getTimeUse() {
        return timeUse;
    }

    public void setTimeUse(Long timeUse) {
        this.timeUse = timeUse;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public boolean isCrux() {
        return crux;
    }

    public void setCrux(boolean crux) {
        this.crux = crux;
    }

    public MethodDescribeModel getMethod() {
        return method;
    }

    public void setMethod(MethodDescribeModel method) {
        this.method = method;
    }

    public ErrorDescribeModel getError() {
        return error;
    }

    public void setError(ErrorDescribeModel error) {
        this.error = error;
    }


    @Override
    public String toString() {
        return "BaseCollectModel =====>  "+ new Gson().toJson(this);
    }
}
