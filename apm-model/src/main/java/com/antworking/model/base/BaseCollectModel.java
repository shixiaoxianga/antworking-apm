package com.antworking.model.base;

import com.antworking.model.base.method.MethodDescribeModel;
import com.antworking.util.JsonUtil;
import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;

/**
 * @author XiangXiaoWei
 * date 2022/6/11
 */
public class BaseCollectModel {
    private String id;
    private Long startTime;
    private Long endTime;
    private Long timeUse;
    private String host;
    private String appName;
    private Integer order;
    private String node;
    private String label;
    private boolean crux;

    private List<MethodDescribeModel> methods;

    private List<BaseCollectModel> childes;

    public List<BaseCollectModel> getChildes() {
        return childes;
    }

    public void setChildes(List<BaseCollectModel> childes) {
        this.childes = childes;
    }

    public void putChildes(BaseCollectModel children) {
        if (childes == null) {
            childes = new LinkedList<BaseCollectModel>();
        }
        this.childes.add(children);
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
        this.setTimeUse(endTime - this.startTime);
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

    public List<MethodDescribeModel> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodDescribeModel> methods) {
        this.methods = methods;
    }

    public void putMethods(MethodDescribeModel methods) {
        if (this.methods == null) this.methods = new LinkedList<>();
        this.methods.add(methods);
    }


    @Override
    public String toString() {
        return JsonUtil.toJsonString(this);
    }

    public void reset() {
       this.id =null;
       this.startTime=null;
       this.endTime=null;
       this.timeUse=null;
       this.host=null;
       this.appName=null;
       this.order=null;
       this.node=null;
       this.label=null;
       this.crux=false;
    }
}
