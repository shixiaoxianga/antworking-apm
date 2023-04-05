package com.antworking.apm.tps.httpclient;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class InternalRequestAdapter {
        private final Object target;

        private final Method _getRequestLine;
        private final Method _getAllHeader;
        private final static String targetClassName = "org.apache.http.HttpRequest";

        public InternalRequestAdapter(Object target) {
            this.target = target;
            try {
                Class<?> targetClass = target.getClass().getClassLoader().loadClass(targetClassName);
                _getRequestLine = targetClass.getMethod("getRequestLine");
                _getAllHeader = targetClass.getMethod("getAllHeaders");
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException("error :" + e.getMessage() + ". probable cause the target is not belong javax.servlet.http.HttpServletRequest ");
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("error :" + e.getMessage() + ". probable cause the target is not belong javax.servlet.http.HttpServletRequest ");
            }
        }

        public String getMethod(){
            try {
                Object invoke = _getRequestLine.invoke(target);
                Object getMethod = invoke.getClass().getMethod("getMethod").invoke(invoke);
                return (String) getMethod;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        public String getUri(){
            try {
                Object invoke = _getRequestLine.invoke(target);
                Object getMethod = invoke.getClass().getMethod("getUri").invoke(invoke);
                return (String) getMethod;
            } catch (Exception e) {
                throw new RuntimeException(e);
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
        public String protocolVersion(){
            try {
                Object invoke = _getRequestLine.invoke(target);
                Object getMethod = invoke.getClass().getMethod("getProtocolVersion").invoke(invoke);
                return getMethod.toString();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }



    }
