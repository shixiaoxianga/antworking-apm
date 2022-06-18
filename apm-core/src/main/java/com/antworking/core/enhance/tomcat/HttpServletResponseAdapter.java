package com.antworking.core.enhance.tomcat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

public class HttpServletResponseAdapter {
        private final Object target;

        private  Method _getResponseBody;
        private  Method _getResponseCode;

        private final static String targetClassName = "javax.servlet.http.HttpServletResponse";

        public HttpServletResponseAdapter(Object target) {
            this.target = target;
            try {
                Class<?> targetClass = target.getClass().getClassLoader().loadClass(targetClassName);
                _getResponseBody= targetClass.getMethod("getOutputStream");
                _getResponseCode= targetClass.getMethod("getStatus");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public String getResponseBody() {
            try {
                InputStream inputStream = (InputStream) _getResponseBody.invoke(target);
                StringBuilder result = new StringBuilder();
                BufferedReader in = null;
                in = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = in.readLine()) != null) {
                    result.append(line);
                }
                inputStream.reset();
                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }


        public Integer getResponseCode() {
            try {
                return (Integer) _getResponseCode.invoke(target);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }