package com.antworking.core.enhance.http.java_net.sendhttp.https;




import com.antworking.core.enhance.http.java_net.sendhttp.ProxyHttpUrlConnection;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

public class Handler extends sun.net.www.protocol.https.Handler {


    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) super.openConnection(url);
        return new ProxyHttpUrlConnection(connection,url);
    }

    @Override
    protected URLConnection openConnection(URL url, Proxy proxy) throws IOException {
        return super.openConnection(url, proxy);
    }
}