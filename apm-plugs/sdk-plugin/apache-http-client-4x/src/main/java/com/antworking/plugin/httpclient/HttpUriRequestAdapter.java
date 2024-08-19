package com.antworking.plugin.httpclient;

import com.antworking.utils.JsonUtil;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class HttpUriRequestAdapter {
        private final Object target;
        private final Method _getMethod;
        private final Method _getURI;
        private final Method _getAllHeaders;
        private final static String targetClassName = "org.apache.http.client.methods.HttpUriRequest";

        public HttpUriRequestAdapter(Object target) {
            this.target = target;
            try {
                Class<?> targetClass = target.getClass().getClassLoader().loadClass(targetClassName);
                _getMethod = targetClass.getMethod("getMethod");
                _getURI = targetClass.getMethod("getURI");
                _getAllHeaders = targetClass.getMethod("getAllHeaders");

            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException("error :" + e.getMessage() + ". probable cause the target is not belong javax.servlet.http.HttpServletRequest ");
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("error :" + e.getMessage() + ". probable cause the target is not belong javax.servlet.http.HttpServletRequest ");
            }
        }

        public String  getMethod() {
            try {
                return (String) _getMethod.invoke(target);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        public String  getUrl() {
            try {
                return _getURI.invoke(target).toString();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public String getHeaders() {
            try {
                return JsonUtil.toJsonString(_getAllHeaders.invoke(target));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

