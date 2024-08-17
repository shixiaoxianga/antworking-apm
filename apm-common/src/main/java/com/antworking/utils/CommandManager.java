package com.antworking.utils;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    public static Map<String,String> commandMap = new HashMap<String,String>();
    public static void analysisCmd(){
        String property = System.getProperty("apm.jvm.args");
        property = property.replace("[","").replace("]","");
        String[] args = property.split(",");

        for (String arg : args) {
            arg = arg.replace(" ","");
            if(arg.startsWith("-javaagent:")&&arg.contains("antworking-apm")){
                commandMap.put("javaagent",arg.replace("-javaagent:",""));
            }
        }

    }
}
