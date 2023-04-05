package com.antworking.apm.tps.httpclient;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Arrays;

public class InternalResponseAdapter {
        private final Object target;

        private final Method _getStatusLine;
        private final Method _getAllHeader;
        private final static String targetClassName = "org.apache.http.client.methods.CloseableHttpResponse";

        public InternalResponseAdapter(Object target) {
            this.target = target;
            try {
                Class<?> targetClass = target.getClass().getClassLoader().loadClass(targetClassName);
                _getStatusLine = targetClass.getMethod("getStatusLine");
                _getAllHeader = targetClass.getMethod("getAllHeaders");
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException("error :" + e.getMessage() + ". probable cause the target is not belong javax.servlet.http.HttpServletRequest ");
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("error :" + e.getMessage() + ". probable cause the target is not belong javax.servlet.http.HttpServletRequest ");
            }
        }
    public String getAllHeader(){
        try {
            Object invoke = _getAllHeader.invoke(target);
            if (Array.getLength(invoke)==0) {
                return "";
            }
            return Arrays.toString((Object[])invoke);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

        public String getCode(){
            try {
                Object invoke = _getStatusLine.invoke(target);
                Object getMethod = invoke.getClass().getMethod("getStatusCode").invoke(invoke);
                return getMethod.toString();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }



    }
