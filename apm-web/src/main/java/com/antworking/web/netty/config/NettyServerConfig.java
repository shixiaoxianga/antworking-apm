package com.antworking.web.netty.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "antworking.netty",ignoreInvalidFields = true)
public class NettyServerConfig {

    private static Integer serverPort;
    private static String serverHost;
    private static Integer boosThread;
    private static Integer workThread;

    public  Integer getServerPort() {
        return serverPort;
    }

    public  void setServerPort(Integer serverPort) {
        NettyServerConfig.serverPort = serverPort;
    }

    public  String getServerHost() {
        return serverHost;
    }

    public  void setServerHost(String serverHost) {
        NettyServerConfig.serverHost = serverHost;
    }

    public  Integer getBoosThread() {
        return boosThread;
    }

    public  void setBoosThread(Integer boosThread) {
        NettyServerConfig.boosThread = boosThread;
    }

    public  Integer getWorkThread() {
        return workThread;
    }

    public  void setWorkThread(Integer workThread) {
        NettyServerConfig.workThread = workThread;
    }
}
