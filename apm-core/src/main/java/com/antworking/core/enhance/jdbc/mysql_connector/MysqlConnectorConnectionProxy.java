package com.antworking.core.enhance.jdbc.mysql_connector;

import com.antworking.common.ConstantNode;
import com.antworking.core.AntWorkingContextManager;
import com.antworking.core.tools.CollectionModelTools;
import com.antworking.model.base.BaseCollectModel;
import com.antworking.model.base.error.ErrorDescribeModel;
import com.antworking.model.base.jdbc.JdbcDescribeModel;
import com.antworking.model.base.method.MethodDescribeModel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;

public class MysqlConnectorConnectionProxy implements InvocationHandler {

    //应该有线程安全问题
    private BaseCollectModel jdbcModel;
    private JdbcDescribeModel jdbc;
    //statement des
    MethodDescribeModel methodDescribeModel = null;


    private Object target;
    private Method method;
    private Object[] args;
    private Object result;
    private Throwable e;
    private boolean autoCommit;
    private final static String[] connection_agent_affair_methods = new String[]{"commit", "rollback"};
    private final static String connection_agent_set_auto_commit_method = "setAutoCommit";
    private final static String connection_agent_get_auto_commit_method = "getAutoCommit";
    private final static String[] connection_agent_statement_method = new String[]{"prepareStatement", "prepareCall"};


    public MysqlConnectorConnectionProxy(Object target) {
        this.target = target;
        //作用域于事务
        init();


    }
    private void init(){
        this.jdbcModel = new BaseCollectModel();
        jdbcModel.setNode(ConstantNode.MYSQL_CONNECTOR);
        jdbcModel.setCrux(true);
        //没有开启自动提交只构建一个实例
        CollectionModelTools.INSTANCE.createBaseCollectModel(jdbcModel, method, args, target.getClass(), null);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        this.method = method;
        this.args = args;
        try {
            this.result = method.invoke(target, args);
        } catch (Throwable e) {
            this.e = e;
            end();
            throw e;
        }
        transactional();
        statement();
        return this.result;
    }


    public void transactional() {
        for (String affairMethodName : connection_agent_affair_methods) {
            if (affairMethodName.equals(method.getName())) {
                MethodDescribeModel affairMethod = new MethodDescribeModel();
                affairMethod.setName(method.getName());
                affairMethod.setParamClazz(method.getParameterTypes());
                affairMethod.setClazz(target.getClass().getName());
                methodDescribeModel.setData(jdbc);
                jdbcModel.putMethods(affairMethod);
                end();
                //下一个做准备
                init();
            }
        }
    }


    public void statement() {
        for (String statement : connection_agent_statement_method) {
            if (statement.equals(method.getName())) {

                //自动提交事务的作用域
                if (getAutoCommit()) {
                    jdbcModel = new BaseCollectModel();
                    jdbcModel.setNode(ConstantNode.MYSQL_CONNECTOR);
                    jdbcModel.setCrux(true);
                    CollectionModelTools.INSTANCE.createBaseCollectModel(jdbcModel, method, args, target.getClass(), methodDescribeModel);
                }
                this.methodDescribeModel = new MethodDescribeModel();
                //作用域于statement
                this.jdbc = new JdbcDescribeModel();

                handlerCommit();
                this.result = Proxy.newProxyInstance(this.getClass().getClassLoader(),
                        new Class[]{java.sql.PreparedStatement.class, java.sql.Statement.class, java.sql.CallableStatement.class},
                        new MysqlConnectorStatementProxy(result, jdbcModel, jdbc, methodDescribeModel, getAutoCommit()));


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
                jdbcModel.putMethods(methodDescribeModel);


/*                //自动提交
                if (getAutoCommit()) {

                    end();

                    methodDescribeModel = new MethodDescribeModel();
                    this.model = new BaseCollectModel();
                    CollectionModelTools.INSTANCE.createBaseCollectModel(model, method, args, target.getClass(), methodDescribeModel);
                }*/

            }
        }
    }

    private void end() {
        CollectionModelTools.INSTANCE.childrenEnd(e, jdbcModel, target, args, method);
    }

    private void handlerCommit() {
        MethodDescribeModel getCommentMethod = new MethodDescribeModel();

        final boolean autoCommit = getAutoCommit();
        getCommentMethod.setName("antworking_setCommit(" + autoCommit + ")");
        jdbcModel.putMethods(getCommentMethod);
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
