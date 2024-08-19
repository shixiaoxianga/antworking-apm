package com.antworking.plugin.httpclient;

import com.antworking.utils.JsonUtil;
import org.apache.http.Header;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CloseableHttpResponseAdapter {
        private final Object target;
        private final Method _getStatusLine;
        private final Method _getAllHeaders;
        private final static String targetClassName = "org.apache.http.client.methods.CloseableHttpResponse";

        public CloseableHttpResponseAdapter(Object target) {
            this.target = target;
            try {
                Class<?> targetClass = target.getClass().getClassLoader().loadClass(targetClassName);
                _getAllHeaders = targetClass.getMethod("getAllHeaders");
                _getStatusLine = targetClass.getMethod("getStatusLine");

            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException("error :" + e.getMessage() + ". probable cause the target is not belong javax.servlet.http.HttpServletRequest ");
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("error :" + e.getMessage() + ". probable cause the target is not belong javax.servlet.http.HttpServletRequest ");
            }
        }



        public String getHeaders() {
            try {
                Object[] headers;
                headers = (Header[]) _getAllHeaders.invoke(target);
                return JsonUtil.toJsonString(Arrays.stream(headers).map(Object::toString).collect(Collectors.toList()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    public String getStatusCode() {
        try {
            return _getStatusLine.invoke(target).toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

