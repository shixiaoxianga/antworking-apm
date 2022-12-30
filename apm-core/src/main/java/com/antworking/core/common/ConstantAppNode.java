package com.antworking.core.common;

import com.antworking.model.core.AppNode;

/**
 * @author XiangXiaoWei
 * date 2022/6/29
 */
public class ConstantAppNode {
    public final static String _TOMCAT = "Tomcat";
    public final static String _MYSQL_DRIVE = "MysqlDrive";

    public final static AppNode TOMCAT = new AppNode(1,_TOMCAT);
    public final static AppNode MYSQL_DRIVE = new AppNode(2,_MYSQL_DRIVE);

}
