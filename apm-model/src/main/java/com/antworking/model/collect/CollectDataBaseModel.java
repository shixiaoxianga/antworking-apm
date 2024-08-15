package com.antworking.model.collect;

import com.antworking.model.core.AppNode;
import com.antworking.utils.TimeUtil;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
public class CollectDataBaseModel {
    private String id;
    private String traceId;
    private Boolean isWeb;

    private Integer order;

    private Long beginTime;

    private Long endTime;
    private Long useTime;
    private Object data;
    private AppNode appNode;

    private String threadName;
    private ErrorDescribeModel error;

    public static CollectDataBaseModel init(boolean isWeb, Object data, AppNode appNode, String threadName,String traceId) {
        CollectDataBaseModel model = new CollectDataBaseModel(UUID.randomUUID().toString(), isWeb, data, appNode);
        model.setBeginTime(TimeUtil.getCurrentTimeNano());
        model.setThreadName(threadName);
        model.setTraceId(traceId);
        return model;
    }

    public CollectDataBaseModel(String id, boolean isWeb, Object data, AppNode appNode) {
        this.id = id;
        this.isWeb = isWeb;
        this.data = data;
        this.appNode = appNode;
    }

    public CollectDataBaseModel(String id, boolean isWeb, Object data, AppNode appNode, ErrorDescribeModel error) {
        this.id = id;
        this.isWeb = isWeb;
        this.data = data;
        this.appNode = appNode;
        this.error = error;
    }

    public Boolean getWeb() {
        return isWeb;
    }

    public void setWeb(Boolean web) {
        isWeb = web;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isWeb() {
        return isWeb;
    }

    public void setWeb(boolean web) {
        isWeb = web;
    }

    public Long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
        this.useTime = endTime - beginTime;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public AppNode getAppNode() {
        return appNode;
    }

    public void setAppNode(AppNode appNode) {
        this.appNode = appNode;
    }

    public ErrorDescribeModel getError() {
        return error;
    }

    public void setError(ErrorDescribeModel error) {
        this.error = error;
    }

    public Long getUseTime() {
        return useTime;
    }

    public void setUseTime(Long useTime) {
        this.useTime = useTime;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }
}
