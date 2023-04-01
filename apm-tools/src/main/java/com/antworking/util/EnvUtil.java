package com.antworking.util;

import java.net.URL;

public class EnvUtil {
    public static boolean isRunInJar() {
        String resourcePath = "/" + EnvUtil.class.getPackage().getName().replace(".", "/");
        URL url = EnvUtil.class.getResource(resourcePath);
        System.out.printf("RunEnvironment url: %s\n", url);
        if (url.getProtocol().equals("jar")) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println(isRunInJar());
    }
}
