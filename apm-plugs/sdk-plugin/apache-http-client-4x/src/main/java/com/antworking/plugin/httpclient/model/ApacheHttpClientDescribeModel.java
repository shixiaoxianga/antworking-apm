package com.antworking.plugin.httpclient.model;

import com.antworking.model.collect.MethodDescribeModel;
import com.antworking.utils.JsonUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * @author XiangXiaoWei
 * date 2022/6/15
 */
public class ApacheHttpClientDescribeModel  {


    private String url;
    private String method;
    private String params;
    private String config;

    private String reqHeaders;
    private String respHeaders;
    private String respCode;

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getReqHeaders() {
        return reqHeaders;
    }

    public void setReqHeaders(String reqHeaders) {
        this.reqHeaders = reqHeaders;
    }

    public String getRespHeaders() {
        return respHeaders;
    }

    public void setRespHeaders(String respHeaders) {
        this.respHeaders = respHeaders;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    @Override
    public String toString() {
        return JsonUtil.toJsonString(this);
    }
}