package com.antworking.plugin.tomcat;

//import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


public class HttpServletRequestAdapter {
        private final Object target;
        private final Method _getRequestURI;
        private final Method _getRequestURL;
        private final Method    _getParameterMap;
        private final Method _getMethod;
        private final Method _getHeader;
        private final Method _getRemoteAddr;
        private final Method _getRequestMethod;
        private final Method _getReaderMethod;
        private final Method _getInputStreamMethod;
        private final Method _getHeaderNames;
        private final Method _getReader;
        private final static String targetClassName = "javax.servlet.http.HttpServletRequest";

        public HttpServletRequestAdapter(Object target) {
            this.target = target;
            try {
                Class<?> targetClass = target.getClass().getClassLoader().loadClass(targetClassName);
                _getRequestURI = targetClass.getMethod("getRequestURI");
                _getParameterMap = targetClass.getMethod("getParameterMap");
                _getMethod = targetClass.getMethod("getMethod");
                _getHeader = targetClass.getMethod("getHeader", String.class);
                _getRemoteAddr = targetClass.getMethod("getRemoteAddr");
                _getRequestURL = targetClass.getMethod("getRequestURL");
                _getRequestMethod = targetClass.getMethod("getMethod");
                _getReaderMethod = targetClass.getMethod("getReader");
                _getInputStreamMethod = targetClass.getMethod("getInputStream");
                _getHeaderNames = targetClass.getMethod("getHeaderNames");
                _getReader = targetClass.getMethod("getReader");

            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException("error :" + e.getMessage() + ". probable cause the target is not belong javax.servlet.http.HttpServletRequest ");
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("error :" + e.getMessage() + ". probable cause the target is not belong javax.servlet.http.HttpServletRequest ");
            }
        }


        public String getRequestURI() {
            try {
                return (String) _getRequestURI.invoke(target);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public String getRequestURL() {
            try {
                return _getRequestURL.invoke(target).toString();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public Map<String, String[]> getParameterMap() {
            try {
                Map<String, String[]> invoke = (Map<String, String[]>)_getParameterMap.invoke(target);
                Map<String, String[]> map_new = new HashMap<>();
                //遍历集合
                for (String s : invoke.keySet()) {
                    String[] values = invoke.get(s);
                    map_new.put(s, values);
                }
                return map_new;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        public String getRequestMethod() {
            try {
                return (String) _getRequestMethod.invoke(target);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        public Map<String,String> getHeaders() {
            Map<String,String> headerMap = new HashMap<>();
            try {
                Enumeration<String> invoke = (Enumeration<String>) _getHeaderNames.invoke(target);
                while (invoke.hasMoreElements()) {
                    String key = invoke.nextElement();
                    Object header = _getHeader.invoke(target, key);
                    headerMap.put(key, header.toString());
                }
                return headerMap;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
//        public MyHttpServletRequestWrapper getReqWrapper() {
//            try {
//                MyHttpServletRequestWrapper wrapper = new MyHttpServletRequestWrapper((HttpServletRequest) target);
//                return wrapper;
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }

        public String getMethod() {
            try {
                return (String) _getMethod.invoke(target);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public String getHeader(String name) {
            try {
                return (String) _getHeader.invoke(target, name);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public String getRemoteAddr() {
            try {
                return (String) _getRemoteAddr.invoke(target);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
/*
        public String getRequestBody() {
            try {
                StringBuilder data = new StringBuilder();
                String line = null;
                BufferedReader reader = null;

                reader = (BufferedReader)_getReaderMethod.invoke(target);
                while (null != (line = reader.readLine()))
                    data.append(line);
                return data.toString();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
*/

        public String getRequestBody() {
            try {
                //CoyoteInputStream
                InputStream is = null;
                is = (InputStream) _getInputStreamMethod.invoke(target);
                StringBuilder sb = new StringBuilder();
                byte[] b = new byte[is.available()];
                for (int n; (n = is.read(b)) != -1; ) {
                    sb.append(new String(b, 0, n));
                }
                return sb.toString();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public String getClientIp() {
            String ip = getHeader("x-forwarded-for");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = getRemoteAddr();
            }
            return ip;
        }

    }

