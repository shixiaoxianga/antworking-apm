package com.antworking.core.enhance.http.java_net.sendhttp.http;




import com.antworking.core.enhance.http.java_net.sendhttp.ProxyHttpUrlConnection;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

public class Handler extends sun.net.www.protocol.http.Handler {
        @Override
        protected URLConnection openConnection(URL url, Proxy proxy) throws IOException {
            HttpURLConnection connection = (HttpURLConnection) super.openConnection(url, proxy);
            return new ProxyHttpUrlConnection(connection, url);
        }

        @Override
        protected URLConnection openConnection(URL url) throws IOException {
            return openConnection(url, null);
        }
    }