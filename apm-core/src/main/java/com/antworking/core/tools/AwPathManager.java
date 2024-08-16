package com.antworking.core.tools;

import com.antworking.common.ConstantAw;
import com.antworking.core.classload.AntWorkingClassLoad;
import com.antworking.core.config.AwConfigManager;
import com.antworking.core.plugin.PluginManager;

import java.io.File;

public class AwPathManager {

    public static String mysqlConfigName = "mysql.json";

    public static String getAwPath() {
//        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
//        if (!trace[trace.length-1].getClassName().contains("com.antworking")) {
//            String agentJarPath = new File(AwPathManager.class.getProtectionDomain()
//                    .getCodeSource()
//                    .getLocation()
//                    .getPath()).getParent();
//            return  "C:\\Users\\48597\\IdeaProjects\\antworking-apm\\antworking-apm";
//        }
//        return  System.getProperty("user.dir") + File.separator + ConstantAw.APP_FOLDER;
        return System.getProperty("user.dir") + File.separator + ConstantAw.APP_FOLDER;
    }


    public static String getAwConfigPath(){
        return getAwPath() + File.separator + "config";
    }

    public static String getMysqlConfigPath(){
        return AwPathManager.getAwConfigPath() + File.separator + AwPathManager.mysqlConfigName;
    }
}
