//package com.antworking.plugin.tomcat;
//
//import org.apache.commons.io.IOUtils;
//import javax.servlet.ReadListener;
//import javax.servlet.ServletInputStream;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletRequestWrapper;
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//
//public class MyHttpServletRequestWrapper extends HttpServletRequestWrapper {
//
//    private byte[] body;
//
//    public static String getBody(MyHttpServletRequestWrapper wrapper){
//
//        byte[] b = new byte[0];
//        try {
//            ServletInputStream inputStream = wrapper.getInputStream();
//            b = new byte[inputStream.available()];
//            inputStream.read(b);
//            return new String(b, StandardCharsets.UTF_8);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//    public MyHttpServletRequestWrapper(HttpServletRequest request) {
//        super(request);
//        try {
//            body = IOUtils.toByteArray(request.getInputStream());
//        } catch (IOException ex) {
//            body = new byte[0];
//        }
//    }
//
//
//
//    @Override
//    public ServletInputStream getInputStream() throws IOException {
//        return new ServletInputStream() {
//            @Override
//            public boolean isFinished() {
//                return false;
//            }
//
//            @Override
//            public boolean isReady() {
//                return false;
//            }
//
//            @Override
//            public void setReadListener(ReadListener readListener) {
//
//            }
//
//            ByteArrayInputStream bais = new ByteArrayInputStream(body);
//
//            @Override
//            public int read() throws IOException {
//                return bais.read();
//            }
//        };
//    }
//
//}
