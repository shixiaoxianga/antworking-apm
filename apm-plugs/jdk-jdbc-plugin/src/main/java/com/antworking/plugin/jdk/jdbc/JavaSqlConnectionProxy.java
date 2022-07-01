package com.antworking.plugin.jdk.jdbc;

import com.antworking.common.ConstantNode;
import com.antworking.core.tools.CollectionModelTools;
import com.antworking.model.base.BaseCollectModel;
import com.antworking.model.base.error.ErrorDescribeModel;
import com.antworking.model.base.method.MethodDescribeModel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

public class JavaSqlConnectionProxy implements InvocationHandler {

    private final static String[] connection_agent_statement_method = new String[]{"prepareStatement", "prepareCall"};
    private final static String[] connection_agent_affair_methods = new String[]{"commit", "rollback"};



    private Connection connection;
    private Object target;


    private Object methodResult;
    private Method method;
    private Object[] args;
    private Throwable e;

    private ThreadLocal<BaseCollectModel> jdbcSession;

    public JavaSqlConnectionProxy(Object target,ThreadLocal<BaseCollectModel> jdbcSession) {
        this.connection = (Connection) target;
        this.target = target;
        this.jdbcSession = jdbcSession;
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
                //当前方法信息
                MethodDescribeModel affairMethod = new MethodDescribeModel();
                affairMethod.setName(method.getName());
                affairMethod.setParamClazz(method.getParameterTypes());
                affairMethod.setClazz(target.getClass().getName());

                BaseCollectModel model = jdbcSession.get();
                if (model !=null) {
                    model.putMethods(affairMethod);
                    jdbcSession.remove();
                    CollectionModelTools.INSTANCE.childrenEnd(e, model, target, args, method);
                }
            }
        }
    }

    private void statement() {
        for (String statement : connection_agent_statement_method) {
            if (statement.equals(method.getName())) {

                //初始化基本信息
                BaseCollectModel jdbcModel = new BaseCollectModel();
                MethodDescribeModel methodDescribeModel = new MethodDescribeModel();
                jdbcModel.setNode(ConstantNode.MYSQL_CONNECTOR);
                jdbcModel.setCrux(true);
                CollectionModelTools.INSTANCE.createBaseCollectModel(jdbcModel, method, args, target.getClass(), methodDescribeModel);

                proxyStatement(jdbcModel);

                handlerCommit(jdbcModel);

                //记录当前方法信息
                saveMethod(jdbcModel, methodDescribeModel);

                jdbcSession.set(jdbcModel);
            }
        }
    }

    private void saveMethod(BaseCollectModel jdbcModel, MethodDescribeModel methodDescribeModel) {
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
    }

    private void handlerCommit(BaseCollectModel jdbcModel) {
        MethodDescribeModel getCommentMethod = new MethodDescribeModel();
        final boolean autoCommit = getAutoCommit();
        getCommentMethod.setName("antworking_setCommit(" + autoCommit + ")");
        jdbcModel.putMethods(getCommentMethod);
    }

    private void proxyStatement(BaseCollectModel jdbcModel) {
        this.methodResult = Proxy.newProxyInstance(this.getClass().getClassLoader(),
                new Class[]{java.sql.PreparedStatement.class, java.sql.Statement.class, java.sql.CallableStatement.class},
                new JavaSqlStatementProxy(getTarget(methodResult), jdbcModel, getAutoCommit()));
    }


    public Boolean getAutoCommit() {
        try {
            return ((Connection) target).getAutoCommit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
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
