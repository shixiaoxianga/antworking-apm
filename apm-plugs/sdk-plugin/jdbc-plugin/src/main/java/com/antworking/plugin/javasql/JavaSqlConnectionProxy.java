package com.antworking.plugin.javasql;

import com.antworking.core.collect.AwCollectManager;
import com.antworking.plugin.javasql.model.JdbcDescribeModel;
import com.antworking.model.collect.CollectDataBaseModel;
import com.antworking.model.collect.MethodDescribeModel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JavaSqlConnectionProxy implements InvocationHandler {

    private final static String[] connection_agent_statement_method = new String[]{"prepareStatement", "prepareCall"};
    private final static String[] connection_agent_affair_methods = new String[]{"commit", "rollback"};


    private Connection connection;
    private Object target;


    private Object methodResult;
    private Method method;
    private Object[] args;

    private Throwable e;

    public JavaSqlConnectionProxy(Object target) {
        this.connection = (Connection) target;
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        this.method = method;
        this.args = args;
        try {
            this.methodResult = method.invoke(target, args);
        } catch (Throwable e) {
            this.e = e;
            throw e;
        }
        statement();
        //处理事务
        transactional();
        return this.methodResult;
    }

    public void transactional() {
        for (String affairMethodName : connection_agent_affair_methods) {
            if (affairMethodName.equals(method.getName())) {
                List<CollectDataBaseModel> modelList = JavaSqlStatementProxy.jdbcSession.get();
                modelList.forEach(model -> {
                    ((JdbcDescribeModel)
                            (((MethodDescribeModel)
                                    model.getData())).getData())
                            .setAutoCommit(affairMethodName);
                });
                modelList.forEach(AwCollectManager::put);
                JavaSqlStatementProxy.jdbcSession.remove();
            }
        }
    }

    private void statement() {
        for (String statement : connection_agent_statement_method) {
            if (statement.equals(method.getName())) {
                proxyStatement();
            }
        }
    }


    public Boolean getAutoCommit() {
        try {
            return ((Connection) target).getAutoCommit();
        } catch (SQLException t) {
            t.printStackTrace();
        }
        return null;
    }

    private void proxyStatement() {
        try {
            this.methodResult = Proxy.newProxyInstance(this.getClass().getClassLoader(),
                    new Class[]{java.sql.PreparedStatement.class, java.sql.Statement.class, java.sql.CallableStatement.class},
                    new JavaSqlStatementProxy(getTarget(methodResult), getAutoCommit()
                            , new JdbcDescribeModel(args[0].toString(), "", connection.getMetaData().getURL(), new ArrayList<>())));
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    //获取原始对象
    public Object getTarget(Object proxy) {
        try {
            Field field = null;
            try {
                field = proxy.getClass().getSuperclass().getDeclaredField("h");
            } catch (NoSuchFieldException ex) {
                return proxy;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            field.setAccessible(true);
            //获取指定对象中此字段的值
            JavaSqlStatementProxy personProxy = (JavaSqlStatementProxy) field.get(proxy);
            Field person = personProxy.getClass().getDeclaredField("target");
            person.setAccessible(true);
            return person.get(personProxy);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


}
