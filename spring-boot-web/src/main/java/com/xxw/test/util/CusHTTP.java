package com.xxw.test.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URI;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 简化http请求
 * new CusHTTP().url(url).method("PUT").params(map).setJson(false).call(String.class);
 * <dependency>
 * <groupId>org.apache.httpcomponents</groupId>
 * <artifactId>httpclient</artifactId>
 * <version>4.5.3</version>
 * </dependency>
 *
 * <dependency>
 * <groupId>org.apache.httpcomponents</groupId>
 * <artifactId>httpmime</artifactId>
 * <version>4.5.3</version>
 * </dependency>
 * <p>
 * 如果某些配置能复用，请配置 clientCfg
 * 关于无脑日志，测试时，用以下代码屏蔽：
 * LoggerContext fac = (LoggerContext) LoggerFactory.getILoggerFactory();
 * //获取全部的LoggerList =fac.getLoggerList()
 * //手动设置某个日志级别
 * fac.getLogger("ROOT").setLevel(Level.ERROR);
 */
public class CusHTTP {

    public static CusClientConfig defaultClientConfig = buildDefaultConfig(60 * 1000, 100);

    static final int DEFAULT_BUFFER_SIZE = 4096;

    public CusClientConfig clientCfg = null;

    private String characterForSend = "UTF-8";

    private long startRequestTime = 0;

    private long endRequestTime = 0;

    public CusHttpResult httpResult;

    // 请求需要的参数
    public String url;
    public String method = "GET";

    /**
     * 标记是否是json请求,将发送json请求体。上传文件请修改为false
     */
    public boolean json = false;

    /**
     * 路径参数,get delete 一般用这个。 可以是字符串  ||  List<NameValuePair> || Map<String,String> || JavaBean
     */
    public Object params;

    /**
     * post put方法参数一般用这个。 可以是 HttpEntity|| 字符串 || Map<String,File|BaseType|ContentBody></> || JavaBean
     */
    public Object data;

    /**
     * 头信息
     */
    public Map<String, String> headers;

    /**
     * 响应结果类型。 如 "" ,String.class,byte[].class,XXBean.class, new File("")
     * 如果是File，则自动下载到指定文件
     * 使用from-data上传文件时候使用httpclient上传，不要加header：(“Content-Type”, “multipart/form-data”)
     */
    public Object resultType = String.class;

    public CusHTTP() {
        super();
        this.clientCfg = defaultClientConfig;
    }

    /**
     * 额外配置，比如超时时间： config( buildDefaultConfig(90 * 1000, 100))
     *
     * @param clientCfg
     * @return
     */
    public CusHTTP config(CusClientConfig clientCfg) {
        this.clientCfg = clientCfg;
        return this;
    }

    /**
     * 设置url
     *
     * @param url
     * @return
     */
    public CusHTTP url(String url) {
        this.url = url;
        return this;
    }

    /**
     * 设置请求方法 GET POST PUT DELETE PATCH
     *
     * @param method
     * @return
     */
    public CusHTTP method(String method) {
        this.method = method.toUpperCase();
        return this;
    }

    public CusHTTP setJson(boolean json) {
        this.json = json;
        return this;
    }

    /**
     * 设置头信息
     *
     * @param headers
     * @return
     */
    public CusHTTP headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    /**
     * 设置参数
     * 如果是get delete 可以是字符串  ||  List<NameValuePair> || Map<String,String>
     * 如果是post put ，可以是 HttpEntity|| 字符串 || Map || JavaBean
     *
     * @param params
     * @return
     */
    public CusHTTP params(Object params) {
        this.params = params;
        return this;
    }

    public CusHTTP data(Object data) {
        this.data = data;
        return this;
    }

    /**
     * 使用默认返回类型  resultType = String.class;
     *
     * @param <T>
     * @return
     */
    public <T extends Object> T call() {
        return call(null);
    }


    /**
     * 响应结果类型。 如 "" ,String.class,byte[].class,XXBean.class, new File("")
     *
     * @return
     */
    public <T extends Object> T call(Object resultType) {
        try {
            if (null != resultType) {
                this.resultType = resultType;
            }
            String method = this.method.toUpperCase();
            switch (method) {
                case "POST":
                    exeHttpEntiyEnclosingRequestBase(new HttpPost(getUrlWithParams()), data);
                    break;
                case "PUT":
                    exeHttpEntiyEnclosingRequestBase(new HttpPut(getUrlWithParams()), data);
                    break;
                case "PATCH":
                    exeHttpEntiyEnclosingRequestBase(new HttpPatch(getUrlWithParams()), data);
                    break;
//                case "DELETE":
//                    exeHttpEntiyEnclosingRequestBase(new HttpEntityRequest(getUrlWithParams()).setMethod(method), data);
//                    break;
                default:
                    exeHttpEntiyEnclosingRequestBase(new HttpEntityRequest(getUrlWithParams()).setMethod(method), data);
            }
        } catch (Exception ex) {
            onHttpComplete(ex, null, null, null);
        }
        return (T) httpResult.result;
    }

    protected String getUrlWithParams() {
        String url = this.url;
        try {
            if (null != params) {
                //params = 字符串?   List<NameValuePair> ? Map<String,String>?
                if (params instanceof String) {
                    if (!url.contains("?")) {
                        url += "?" + params;
                    } else {
                        url += "&" + params;
                    }
                } else {
                    List<NameValuePair> parameters = new ArrayList<>();
                    if (params instanceof List) {
                        parameters.addAll((Collection<? extends NameValuePair>) params);
                    } else if (params instanceof Map) {
                        ((Map) params).forEach((k, v) -> {
                            if (v != null) {
                                parameters.add(new BasicNameValuePair(String.valueOf(k), String.valueOf(v)));
                            }
                        });
                    } else {
                        //属性遍历
                        doWithFields(params.getClass(), f -> {
                            f.setAccessible(true);
                            Object value = f.get(params);
                            if (value != null) {
                                parameters.add(new BasicNameValuePair(f.getName(), String.valueOf(value)));
                            }
                        });
                    }
                    if (!parameters.isEmpty()) {
                        if (!url.contains("?")) {
                            //						url+="?"+params.getNormalParamsStr();
                            url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(parameters, characterForSend));
                        } else {
                            //						url+="&"+params.getNormalParamsStr();
                            url += "&" + EntityUtils.toString(new UrlEncodedFormEntity(parameters, characterForSend));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * 添加请求体。
     *
     * @param closeableList
     * @param requestBase
     * @param data
     * @throws Exception
     */
    public void addHttpBody(List<Closeable> closeableList, HttpEntityEnclosingRequestBase requestBase, Object data) throws Exception {
        if (null != data) {
            if (data instanceof HttpEntity) {
                requestBase.setEntity((HttpEntity) data);
            } else {
                // json formData
                if (json || data instanceof String) {
                    String jsonStr = null;
                    if (data instanceof String) {
                        jsonStr = (String) data;
                    } else {
                        jsonStr = clientCfg.mapper.writeValueAsString(data);
                    }
                    requestBase.setEntity(new StringEntity(jsonStr, ContentType.create("application/json", characterForSend)));
                } else {
                    //form data 或者别的
                    Map<String, Object> formMap = new HashMap<>();
                    ValueWrapper<Boolean> hasFile = new ValueWrapper<>(false);
                    if (data instanceof List) {
                        for (Object p : (List) data) {
                            if (p instanceof NameValuePair) {
                                NameValuePair nv = (NameValuePair) p;
                                if (null != nv.getValue()) {
                                    formMap.put(nv.getName(), nv.getValue());
                                }
                            }
                        }
                    } else if (data instanceof Map) {
                        ((Map<? extends String, ?>) data).forEach((k, v) -> {
                            if (v != null) {
                                formMap.put(k, v);
                            }
                            if (isFileData(v)) {
                                hasFile.value = true;
                            }
                        });
                    } else {
                        //获取字段属性，依次转map
                        doWithFields(data.getClass(), f -> {
                            f.setAccessible(true);
                            Object value = f.get(data);
                            if (value != null) {
                                formMap.put(f.getName(), value);
                            }
                            if (isFileData(value)) {
                                hasFile.value = true;
                            }
                        });
                    }
                    if (hasFile.value) {
                        MultipartEntityBuilder builder = MultipartEntityBuilder
                                .create();
                        for (Map.Entry<String, Object> e : formMap.entrySet()) {
                            Object val = e.getValue();
                            if (val instanceof File) {
                                builder.addPart(e.getKey(), new FileBody((File) val));
                            } else if (val instanceof ContentBody) {
                                builder.addPart(e.getKey(), (ContentBody) val);
                            } else if (val instanceof AbstractResource) {
                                AbstractResource ar = (AbstractResource) val;
                                InputStream is = ar.getInputStream();
                                closeableList.add(is);
                                builder.addPart(e.getKey(), new InputStreamBody(is, ar.getFilename()));
                            } else if (val instanceof InputStream) {
                                InputStream is = (InputStream) val;
                                closeableList.add(is);
                                builder.addPart(e.getKey(), new InputStreamBody(is, e.getKey()));
                            } else {
                                builder.addPart(e.getKey(), new StringBody(String.valueOf(val), ContentType.TEXT_PLAIN));
                            }
                        }
                        requestBase.setEntity(builder.build());
                    } else if (!formMap.isEmpty()) {
                        List<BasicNameValuePair> finalFormData = new ArrayList<>();
                        for (Map.Entry<String, Object> e : formMap.entrySet()) {
                            finalFormData.add(new BasicNameValuePair(e.getKey(), String.valueOf(e.getValue())));
                        }
                        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(finalFormData, characterForSend);
                        requestBase.setEntity(entity);
                    }
                }
            }
        }
    }

    private void exeHttpEntiyEnclosingRequestBase(HttpEntityEnclosingRequestBase requestBase, Object params) {
        CloseableHttpResponse response = null;
        List<Closeable> closeableList = new ArrayList<>();
        try {
            onGetHttpRequest(requestBase);
            this.addHttpBody(closeableList, requestBase, params);
            resetHttpEntity(requestBase);
            startRequestTime = System.currentTimeMillis();
            response = clientCfg.client.execute(requestBase);
            Header[] headers = response.getAllHeaders();
            HttpEntity entity = response.getEntity();
            onHttpComplete(null, response.getStatusLine(), entity, headers);
        } catch (Exception e) {
            onHttpComplete(e, null, null, null);
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            closeableList.forEach(it -> close(it));
            close(response);
        }
    }

    /**
     * 允许重新设置请求体
     *
     * @param requestBase
     */
    protected void resetHttpEntity(HttpEntityEnclosingRequestBase requestBase) {

    }

    private boolean isFileData(Object obj) {
        return obj instanceof File || obj instanceof FileBody || obj instanceof InputStream || obj instanceof Resource;
    }

    private void onGetHttpRequest(HttpRequestBase base) {
        httpResult = null;
        base.setConfig(this.clientCfg.requestConfig);

        //json请求时，能自动添加json header
        boolean shouldAddJsonHeader = json;

        // add header
        if (null != headers && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                String value = String.valueOf(entry.getValue());
                base.addHeader(entry.getKey(), value);
                if (value.contains("application/json")) {
                    shouldAddJsonHeader = false;
                }
            }
        }
        if (shouldAddJsonHeader) {
            base.addHeader("Content-Type", "application/json");
        }
    }

    private void onHttpComplete(Exception ex, StatusLine statusLine,
                                HttpEntity entity, Header[] headers) {
        endRequestTime = System.currentTimeMillis();
        // 记录最新一次的请求。
        httpResult = new CusHttpResult(ex, statusLine, entity);

        if (resultType instanceof OnComplete) {
            httpResult.result = entity;
            ((OnComplete) resultType).onComplete(this, ex, statusLine, entity, headers);
            return;
        }

        if (entity == null) {
            return;
        }
        //需要根据结果处理 响应结果类型。 如 "" ,String.class,byte[].class,XXBean.class, new File("")
        try {
            if (resultType instanceof File) {
                saveFile((File) resultType, entity, headers);
                httpResult.result = resultType;
            } else if (byte[].class.equals(resultType)) {
                httpResult.result = EntityUtils.toByteArray(httpResult.entity);
            } else {
                String s = EntityUtils.toString(httpResult.entity, characterForSend);
                httpResult.result = s;
                boolean isString = resultType instanceof String || String.class.equals(resultType);
                if (!isString) {
                    if (resultType instanceof Class) {
                        httpResult.result = clientCfg.mapper.readValue(s, (Class<? extends Object>) resultType);
                    } else if (resultType instanceof JavaType) {
                        httpResult.result = clientCfg.mapper.readValue(s, (JavaType) resultType);
                    } else if (resultType instanceof TypeReference) {
                        httpResult.result = clientCfg.mapper.readValue(s, (TypeReference) resultType);
                    }
                }
            }
        } catch (Exception ex2) {
            httpResult.ex = ex2;
        }
    }


    /**
     * 请求是否成功,仅根据http状态码200判断
     *
     * @return
     */
    public boolean isSuccess() {
        return httpResult != null && httpResult.statusLine != null && httpResult.statusLine.getStatusCode() == 200;
    }

    /**
     * 获取此次的异常
     *
     * @return
     */
    public Exception getError() {
        return httpResult == null ? null : httpResult.ex;
    }



    /**
     * 将结果保存到文件
     *
     * @param file
     */
    public void saveFile(File file, HttpEntity entity, Header[] headers) throws Exception {
        file = new File(file.getAbsolutePath());
        if (!file.exists()) {
            File p = file.getParentFile();
            if (p != null && !p.exists()) {
                p.mkdirs();
            }
            file.createNewFile();
        }
        Args.notNull(entity, "Entity");
        final InputStream inStream = entity.getContent();
        if (inStream == null) {
            return;
        }
        FileOutputStream fos = null;
        try {
            Args.check(entity.getContentLength() <= Integer.MAX_VALUE,
                    "HTTP entity too large to be buffered in memory");
            fos = new FileOutputStream(file);
            final byte[] tmp = new byte[DEFAULT_BUFFER_SIZE];
            int l;
            while ((l = inStream.read(tmp)) != -1) {
                fos.write(tmp, 0, l);
            }
        } finally {
            close(fos);
            close(inStream);
        }
    }

    /**
     * 获取请求耗时
     *
     * @return
     */
    public int getConsumeTime() {
        int duration = (int) (endRequestTime - startRequestTime);
        return duration > 0 ? duration : 0;
    }

    /**
     * 获取输入流。 可以给定硬盘路径或者classpath:开头的路径。
     *
     * @param filePath
     * @return
     */
    public static InputStream getFileInputStream(String filePath) {
        try {
            if (filePath.startsWith(ResourceUtils.CLASSPATH_URL_PREFIX)) {
                filePath = filePath.substring(ResourceUtils.CLASSPATH_URL_PREFIX.length());
                return new ClassPathResource(filePath).getInputStream();
            }
            File file = ResourceUtils.getFile(filePath);
            if (!file.exists()) {
                return null;
            }
            FileInputStream fis = new FileInputStream(file);
            return fis;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void close(Closeable is) {
        if (is == null) {
            return;
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void doWithFields(Class targetClass, FieldCallback fc)
            throws IllegalArgumentException {
        // Keep backing up the inheritance hierarchy.
        do {
            // Copy each field declared on this class unless it's static or file.
            Field[] fields = targetClass.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                try {
                    fc.doWith(fields[i]);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            targetClass = targetClass.getSuperclass();
        }
        while (targetClass != null && targetClass != Object.class);
    }

    public static class CusHttpResult {
        public Exception ex;
        public StatusLine statusLine;
        public HttpEntity entity;

        //最终结果
        public Object result;

        public CusHttpResult(Exception ex, StatusLine statusLine, HttpEntity entity) {
            super();
            this.ex = ex;
            this.statusLine = statusLine;
            this.entity = entity;
        }
    }


    //Callback interface invoked on each field in the hierarchy.
    public interface FieldCallback {
        //Perform an operation using the given field.
        //@param field the field to operate on
        void doWith(Field field) throws Exception;
    }

    public static class ValueWrapper<T> {
        public T value;

        public ValueWrapper(T value) {
            this.value = value;
        }
    }


    public static class CusClientConfig {
        PoolingHttpClientConnectionManager connMgr;
        RequestConfig requestConfig;
        CloseableHttpClient client = null;
        ObjectMapper mapper;
    }

    /**
     * @param timeout  超时时间 ms
     * @param poolSize 最大线程池大小
     * @return
     */
    public static CusClientConfig buildDefaultConfig(int timeout, int poolSize) {
        CusClientConfig rs = new CusClientConfig();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        rs.mapper = mapper;

        // 设置连接池
        PoolingHttpClientConnectionManager defaultConnMgr = new PoolingHttpClientConnectionManager();
        // 设置连接池大小
        defaultConnMgr.setMaxTotal(poolSize);
        defaultConnMgr.setDefaultMaxPerRoute(defaultConnMgr.getMaxTotal());

        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // 设置连接超时
        configBuilder.setConnectTimeout(timeout);
        // 设置读取超时
        configBuilder.setSocketTimeout(timeout);
        // 设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(timeout);
        // 在提交请求之前 测试连接是否可用
        configBuilder.setStaleConnectionCheckEnabled(true);

        rs.connMgr = defaultConnMgr;
        rs.requestConfig = configBuilder.build();
        rs.client = createHttpClient(null, null,
                defaultConnMgr);

        return rs;
    }

    /**
     * 获取httpClient
     *
     * @param keystorePath
     * @param pwd
     * @param connMgr
     * @return
     */
    public static CloseableHttpClient createHttpClient(String keystorePath,
                                                       String pwd, PoolingHttpClientConnectionManager connMgr) {
        CloseableHttpClient httpClient = null;
        HttpClientBuilder clientBuilder = HttpClients.custom();


        if (keystorePath != null) {
            // 配置自己的证书
            InputStream keystoreIs = null;
            SSLConnectionSocketFactory sslsf = null;
            try {
                // 加载自己的key
                KeyStore keyStore = KeyStore.getInstance(KeyStore
                        .getDefaultType());
                keystoreIs = getFileInputStream(keystorePath);
                keyStore.load(keystoreIs, null == pwd ? null : pwd.toCharArray());
                // 相信自己的CA和所有自签名的证书
                SSLContext sslcontext = new SSLContextBuilder()
                        .loadTrustMaterial(keyStore,
                                new TrustSelfSignedStrategy()).build();

                // 只允许使用TLSv1协议
//                sslsf = new SSLConnectionSocketFactory(
//                        sslcontext,
//                        new String[]{"TLSv1"},
//                        null,
//                        SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

                sslsf = new SSLConnectionSocketFactory(sslcontext,
                        new String[]{"TLSv1"},
                        null,
                        (hostName, sslSession) -> true);

                clientBuilder.setSSLSocketFactory(sslsf);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                close(keystoreIs);
            }
        } else {
            try {
                TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
                    public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        return true;
                    }
                };

                SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                        .loadTrustMaterial(null, acceptingTrustStrategy)
                        .build();

                SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

                clientBuilder.setSSLSocketFactory(csf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (null != connMgr)
            clientBuilder.setConnectionManager(connMgr);

        //                //忽略域名校验--12/24 王贞成，如果有问题，请注释掉改行
        clientBuilder.setSSLHostnameVerifier((hostName, sslSession) -> true);

        httpClient = clientBuilder.build();
        return httpClient;
    }

    interface OnComplete {
        void onComplete(CusHTTP http, Exception ex, StatusLine statusLine,
                        HttpEntity entity, Header[] headers);
    }

    /**
     * 将日志级别调整到Error
     */
    public static void cfgLogLevelError() {
        LoggerContext fac = (LoggerContext) LoggerFactory.getILoggerFactory();
        //获取全部的LoggerList =fac.getLoggerList()
        //手动设置某个日志级别
        fac.getLogger("ROOT").setLevel(Level.ERROR);
    }


    /**
     * 带body的HttpGet
     */
    public static class HttpEntityRequest extends HttpEntityEnclosingRequestBase {

        public String methodName = "GET";

        public HttpEntityRequest() {
            super();
        }

        public HttpEntityRequest(final URI uri) {
            super();
            setURI(uri);
        }

        public HttpEntityRequest setMethod(String methodName) {
            this.methodName = methodName;
            return this;
        }

        /**
         * @throws IllegalArgumentException if the uri is invalid.
         */
        public HttpEntityRequest(final String uri) {
            super();
            setURI(URI.create(uri));
        }

        @Override
        public String getMethod() {
            return methodName;
        }

    }

//    public static void main(String[] args) {
//        try {
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("hello1", Math.random());
//            map.put("hello2", Math.random());
//
//
//            String url = "http://localhost:9003/bjcrcc/zebj";
//            String rs = new CusHTTP().url(url).method("PUT").params(map).setJson(false).call(String.class);
//
//            System.out.println("========================1=");
//            System.out.println(rs);
//            System.out.println("=========================");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
