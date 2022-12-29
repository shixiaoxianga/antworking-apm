package com.antworking.logger;

public class LoggerFactory {
    public static AwLog getLogger(Class<?> clazz){
        return new AwLogSysOut(clazz);
    }
}
