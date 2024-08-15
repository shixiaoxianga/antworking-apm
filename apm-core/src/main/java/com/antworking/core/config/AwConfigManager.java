package com.antworking.core.config;

import com.antworking.common.ConstantAw;
import com.antworking.core.classload.AntWorkingClassLoad;
import com.antworking.core.tools.AwPathManager;
import com.antworking.core.tools.JdbcHelper;
import com.antworking.core.tools.JdbcUtil;
import com.antworking.utils.JsonUtil;

import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;

public class AwConfigManager {

    public static MysqlConfig mysqlConfig;

    static {
//        initConfig();
    }

    public static void initConfig(){
       initMysql();
    }

    private static void initMysql() {
        String mysqlConfigPath = AwPathManager.getMysqlConfigPath();
        try (FileInputStream inputStream = new FileInputStream(mysqlConfigPath);){
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            String config = new String(bytes, StandardCharsets.UTF_8);
            mysqlConfig = JsonUtil.fromJson(config,MysqlConfig.class);
//            Class<?> clazz = AntWorkingClassLoad.INSTANCE.loadClass("com.antworking.core.tools.JdbcHelper");
//            Constructor<?> constructor = clazz.getDeclaredConstructor(String.class, String.class,String.class);
            // 创建对象
//            JdbcUtil.jdbcHelper  = (JdbcHelper) constructor.newInstance(mysqlConfig.getUrl(),mysqlConfig.getUser(),mysqlConfig.getPassword());
            JdbcUtil.jdbcHelper  = new JdbcHelper(mysqlConfig.getUrl(),mysqlConfig.getUser(),mysqlConfig.getPassword());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
