package com.antworking.core.enhance.jdbc.mysql_connector;

import com.antworking.core.AntWorkingContextManager;
import com.antworking.core.method.MethodTools;
import com.antworking.model.base.BaseCollectModel;
import com.antworking.model.base.error.ErrorDescribeModel;
import com.antworking.model.base.jdbc.JdbcDescribeModel;
import com.antworking.model.base.method.MethodDescribeModel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;

public class MysqlConnectorConnectionProxy implements InvocationHandler {

    private Object target;
    private BaseCollectModel model;
    private JdbcDescribeModel jdbc;
    private Throwable e;
    //statement
    MethodDescribeModel methodDescribeModel = null;
    private Method method;
    private Object[] args;
    private Object result;
    private boolean autoCommit;
    private final static String[] connection_agent_affair_methods = new String[]{"commit", "rollback"};
    private final static String connection_agent_set_auto_commit_method = "setAutoCommit";
    private final static String connection_agent_get_auto_commit_method = "getAutoCommit";
    private final static String[] connection_agent_statement_method = new String[]{"prepareStatement", "prepareCall"};


    public MysqlConnectorConnectionProxy(Object target, BaseCollectModel model) {
        this.target = target;
        this.model = model;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        this.method = method;
        this.args = args;
        try {
            this.result = method.invoke(target, args);
        } catch (Throwable e) {
            this.e = e;
            MethodTools.INSTANCE.end(e, model, target, args, method);
            throw e;
        }
        transactional();
        statement();
        return this.result;
    }


    public void transactional() {
/*        if (method.getName().equals(connection_agent_get_auto_commit_method)) {
            MethodDescribeModel getCommentMethod = new MethodDescribeModel();
            getCommentMethod.setName(method.getName());
            getCommentMethod.setClazz(target.getClass().getName());
            getCommentMethod.setReturnClazz(result.getClass().getName()+"_"+result);
            model.putMethods(getCommentMethod);
        }*/
/*
        if (method.getName().equals(connection_agent_set_auto_commit_method)) {
            MethodDescribeModel setCommentMethod = new MethodDescribeModel();
            setCommentMethod.setName(method.getName()+"_"+args[0]);
            setCommentMethod.setParamClazz(method.getParameterTypes());
            setCommentMethod.setClazz(target.getClass().getName());
            model.putMethods(setCommentMethod);
        }
*/

        for (String affairMethodName : connection_agent_affair_methods) {
            if (affairMethodName.equals(method.getName())) {
                MethodDescribeModel affairMethod = new MethodDescribeModel();
                affairMethod.setName(method.getName());
                affairMethod.setParamClazz(method.getParameterTypes());
                affairMethod.setClazz(target.getClass().getName());
                methodDescribeModel.setData(jdbc);
                model.putMethods(affairMethod);
                MethodTools.INSTANCE.end(null, model, target, args, method);
            }
        }
    }

    public void statement() {
        System.out.println(this.result);
        for (String statement : connection_agent_statement_method) {
            if (statement.equals(method.getName())) {
                handlerCommit();
                this.methodDescribeModel = new MethodDescribeModel();
                this.jdbc = new JdbcDescribeModel();
                this.result = Proxy.newProxyInstance(this.getClass().getClassLoader(),
                        new Class[]{java.sql.PreparedStatement.class, java.sql.Statement.class, java.sql.CallableStatement.class},
                        new MysqlConnectorStatementProxy(result, model, jdbc, methodDescribeModel));


                methodDescribeModel.setParam(args);
                methodDescribeModel.setName(method.getName());
                methodDescribeModel.setReturnClazz(method.getReturnType().getName());
                methodDescribeModel.setClazz(target.getClass().getName());
                if (e != null) {
                    ErrorDescribeModel error = new ErrorDescribeModel();
                    error.setTimeStamp(System.currentTimeMillis());
                    error.setStacks(e.getStackTrace());
                    error.setMessage(e.getMessage());
                    error.setClazz(e.getClass().getName());
                    methodDescribeModel.setError(error);
                }
                model.putMethods(methodDescribeModel);

                //自动提交
                if (getAutoCommit()) {
                    MethodTools.INSTANCE.end(null, model, target, args, method);
                    model.setStartTime(System.currentTimeMillis());
                    model.setChildes(new LinkedList<>());
                    model.setId(null);
                    model.setOrder(AntWorkingContextManager.getOrder());
                    model.setMethods(new LinkedList<>());
                }

            }
        }
    }

    private void handlerCommit() {
        MethodDescribeModel getCommentMethod = new MethodDescribeModel();

        final boolean autoCommit = getAutoCommit();
        getCommentMethod.setName("antworking_setCommit(" + autoCommit + ")");
        model.putMethods(getCommentMethod);
    }


    public Boolean getAutoCommit() {
        try {
            return ((Connection) target).getAutoCommit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
