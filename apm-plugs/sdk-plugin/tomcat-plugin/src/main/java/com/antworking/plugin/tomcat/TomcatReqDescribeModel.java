package com.antworking.plugin.tomcat;

import com.antworking.utils.JsonUtil;

import java.util.List;
import java.util.Map;

/**
 * @author XiangXiaoWei
 * date 2022/6/17
 */
public class TomcatReqDescribeModel {


    private String methodName;
    private String paramType;
    private Object reqParam;
    private String reqBody;
    private String reqUrl;
    private String reqUri;
    private String reqMethod;
    private String clientIp;
    private Integer repCode;
    private Map<String,String> headers;

    private List<String> stackTraces;
//    private List<TomcatStackTraceModel> stackTraces;

//    public List<TomcatStackTraceModel> getStackTraces() {
//        return stackTraces;
//    }

//    public void setStackTraces(List<TomcatStackTraceModel> stackTraces) {
//        this.stackTraces = stackTraces;
//    }

    public String getReqBody() {
        return reqBody;
    }

    public void setReqBody(String reqBody) {
        this.reqBody = reqBody;
    }

    public List<String> getStackTraces() {
        return stackTraces;
    }

    public void setStackTraces(List<String> stackTraces) {
        this.stackTraces = stackTraces;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }



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