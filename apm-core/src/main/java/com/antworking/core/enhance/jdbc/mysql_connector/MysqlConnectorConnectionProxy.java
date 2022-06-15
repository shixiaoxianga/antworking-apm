package com.antworking.core.enhance.jdbc.mysql_connector;

import com.antworking.core.method.MethodTools;
import com.antworking.model.base.BaseCollectModel;
import com.antworking.model.base.jdbc.JdbcDescribeModel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MysqlConnectorConnectionProxy implements InvocationHandler {

    private Object target;
    private BaseCollectModel model;
    private JdbcDescribeModel jdbc;

    private Method method;
    private Object[] args;
    private Object result;
    private JdbcDescribeModel.JdbcNode jdbcNode = null;
    private final static String[] connection_agent_affair_methods = new String[]{"commit", "rollback"};
    private final static String connection_agent_set_auto_commit_method = "setAutoCommit";
    private final static String connection_agent_get_auto_commit_method = "getAutoCommit";


    public MysqlConnectorConnectionProxy(Object target, BaseCollectModel model) {
        this.target = target;
        this.model = model;
        this.jdbcNode = new JdbcDescribeModel.JdbcNode();
        this.jdbc = new JdbcDescribeModel();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        this.method = method;
        this.args = args;
        try {
            this.result = method.invoke(target, args);
        } catch (Throwable e) {
            MethodTools.INSTANCE.end(e, model, target, args, method);
            throw e;
        }
        transactional();
        statement();
        return this.result;
    }


    public void transactional() {
        if (method.getName().equals(connection_agent_get_auto_commit_method)) {
            jdbc.setAutoCommit(true);
        }
        if (method.getName().equals(connection_agent_set_auto_commit_method)) {
            jdbc.setAutoCommit((boolean) args[0]);
        }

        for (String affairMethod : connection_agent_affair_methods) {
            if (affairMethod.equals(method.getName())) {
                jdbc.putNodes(jdbcNode);
            }
        }
    }

    public void statement() {
        System.out.println(this.result);
        if (this.result instanceof java.sql.PreparedStatement) {
            this.result = Proxy.newProxyInstance(this.getClass().getClassLoader(),
                    new Class[]{java.sql.PreparedStatement.class},
                    new MysqlConnectorStatementProxy(result, model, jdbcNode));
        }
    }

}
