package com.antworking.core.common;

import com.antworking.model.core.AppNode;

/**
 * @author XiangXiaoWei
 * date 2022/6/29
 */
public class ConstantAppNode {
    public final static String _TOMCAT = "Tomcat";
    public final static String _MYSQL_DRIVE = "MysqlDrive";
    public final static String _SQL_DRIVE_CONNECT = "JavaSqlDriveConnect";
    public final static String _REDIS_TEMPLATE = "RedisTemplate";

    public final static AppNode TOMCAT = new AppNode(1, _TOMCAT);
    public final static AppNode SQL_DRIVE_CONNECT = new AppNode(2, _SQL_DRIVE_CONNECT);

    public final static AppNode SQL_DRIVE_STATEMENT = new AppNode(3, "JavaSqlStatement");

    public final static AppNode REDIS_TEMPLATE = new AppNode(4, _REDIS_TEMPLATE);

}
