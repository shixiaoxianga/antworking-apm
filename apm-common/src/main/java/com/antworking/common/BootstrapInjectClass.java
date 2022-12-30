package com.antworking.common;

public class BootstrapInjectClass {


    public static String[] awClass = {
            "com.antworking.core.interceptor.AwMethodIntercept",
            "com.antworking.core.handler.IMethodInterceptHandler",
            "com.antworking.core.handler.AbstractMethodInterceptHandler",
            "com.antworking.logger.AwLog",
            "com.antworking.logger.AbstractAwLog",
            "com.antworking.logger.LoggerFactory",
            "com.antworking.logger.AwLogSysOut",
            "com.antworking.logger.AwLogWriteFile",
    };
    public static String[] byteBuddyClass = {
            ConstantAw.PACKAGE_NAME_SPACE + ".net.bytebuddy.implementation.bind.annotation.AllArguments",
            ConstantAw.PACKAGE_NAME_SPACE + ".net.bytebuddy.implementation.bind.annotation.Origin",
            ConstantAw.PACKAGE_NAME_SPACE + ".net.bytebuddy.implementation.bind.annotation.RuntimeType",
            ConstantAw.PACKAGE_NAME_SPACE + ".net.bytebuddy.implementation.bind.annotation.SuperCall",
            ConstantAw.PACKAGE_NAME_SPACE + ".net.bytebuddy.implementation.bind.annotation.AllArguments$Assignment",
    };

}
