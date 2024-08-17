package com.antworking.core.tools;

import com.antworking.common.ConstantAw;
import com.antworking.core.classload.AntWorkingClassLoad;
import com.antworking.core.config.AwConfigManager;
import com.antworking.core.plugin.PluginManager;
import com.antworking.utils.CommandManager;

import java.io.File;

public class AwPathManager {

    public static String mysqlConfigName = "mysql.json";

    public static String getAwPath() {
        return new File(CommandManager.commandMap.get("javaagent")).getParent();
    }


    public static String getAwConfigPath(){
        return getAwPath() + File.separator + "config";
    }

    public static String getMysqlConfigPath(){
        return AwPathManager.getAwConfigPath() + File.separator + AwPathManager.mysqlConfigName;
    }
}
