package com.antworking.common;

public class BootstrapInjectClass {


    public static String[] awClass = {
            "com.antworking.logger.AwLog",
            "com.antworking.logger.AbstractAwLog",
            "com.antworking.logger.LoggerFactory",
            "com.antworking.logger.AwLogSysOut",
            "com.antworking.logger.AwLogWriteFile",
    };
    public static String[] byteBuddyClass = {
            AwConstant.PACKAGE_NAME_SPACE + ".net.bytebuddy.implementation.bind.annotation.AllArguments",
            AwConstant.PACKAGE_NAME_SPACE + ".net.bytebuddy.implementation.bind.annotation.Origin",
            AwConstant.PACKAGE_NAME_SPACE + ".net.bytebuddy.implementation.bind.annotation.RuntimeType",
            AwConstant.PACKAGE_NAME_SPACE + ".net.bytebuddy.implementation.bind.annotation.SuperCall",
            AwConstant.PACKAGE_NAME_SPACE + ".net.bytebuddy.implementation.bind.annotation.AllArguments$Assignment",
    };

}
