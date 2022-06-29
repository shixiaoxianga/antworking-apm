package com.antworking.core.enhance.http.java_net.sendhttp;


public class HttpProxy {

    private static String PROTOCOL_HANDLER = "java.protocol.handler.pkgs";
    private static String HANDLERS_PACKAGE = "com.antworking.core.enhance.http.java_net.sendhttp";

    public static void registerProtocol() {
        String handlers = System.getProperty(PROTOCOL_HANDLER, "");
        System.setProperty(PROTOCOL_HANDLER,
                ((handlers == null || handlers.isEmpty()) ?
                        HANDLERS_PACKAGE : handlers + "|" + HANDLERS_PACKAGE));
    }


}
