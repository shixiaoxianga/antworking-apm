package com.antworking.plugin.tomcat;

import com.antworking.util.JsonUtil;
import com.google.gson.Gson;

/**
 * @author XiangXiaoWei
 * date 2022/6/17
 */
public class TomcatReqDescribeModel {


    private String methodName;
    private String paramType;
    private Object reqParam;
    private String reqUrl;
    private String reqUri;
    private String reqMethod;
    private String clientIp;
    private Integer repCode;

    public String getReqUri() {
        return reqUri;
    }

    public void setReqUri(String reqUri) {
        this.reqUri = reqUri;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }


    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public Object getReqParam() {
        return reqParam;
    }

    public void setReqParam(Object reqParam) {
        this.reqParam = reqParam;
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }

    public String getReqMethod() {
        return reqMethod;
    }

    public void setReqMethod(String reqMethod) {
        this.reqMethod = reqMethod;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }


    public Integer getRepCode() {
        return repCode;
    }

    public void setRepCode(Integer repCode) {
        this.repCode = repCode;
    }

    @Override
    public String toString() {
        return JsonUtil.toJsonString(this);
    }
}