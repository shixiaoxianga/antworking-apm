package com.antworking.core.enhance.jdbc.mysql_connector;

import com.antworking.core.tools.CollectionModelTools;
import com.antworking.model.base.BaseCollectModel;
import com.antworking.model.base.jdbc.JdbcDescribeModel;
import com.antworking.model.base.method.MethodDescribeModel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MysqlConnectorStatementProxy implements InvocationHandler {

    private Object target;
    private Throwable e;


    private Method method;
    private Object[] args;
    private Object result;
    private BaseCollectModel jdbcModel;
    private JdbcDescribeModel jdbc;
    private boolean autoCommit;
    MethodDescribeModel methodDescribeModel;
    private final static String[] prepared_statement_setMethods =
            new String[]{"setNull", "setBoolean", "setByte", "setShort",
                    "setInt", "setLong", "setFloat", "setDouble", "setBigDecimal",
                    "setString", "setBytes", "setDate", "setTime", "setTimestamp", "setAsciiStream",
                    "setUnicodeStream", "setBinaryStream", "clearParameters", "setObject",
                    "setArray", "setDate", "setTime", "setTimestamp", "setNull", "setNString", "setSQLXML"};
    private final static String[] prepared_statement_methods = new String[]{"execute", "executeUpdate", "executeQuery"};

    public MysqlConnectorStatementProxy(Object target, BaseCollectModel model, JdbcDescribeModel jdbc, MethodDescribeModel methodDescribeModel, Boolean autoCommit) {
        this.target = target;
        this.jdbcModel = model;
        this.jdbc = jdbc;
        this.methodDescribeModel = methodDescribeModel;
        this.autoCommit = autoCommit;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        this.method = method;
        this.args = args;
        try {
            result = method.invoke(target, args);
        } catch (Throwable e) {
            this.e=e;
            CollectionModelTools.INSTANCE.childrenEnd(e, jdbcModel, target, args, method);
            throw e;
        }
        setVal();
        execute();
        return result;
    }

    private void execute() {
        for (String execute : prepared_statement_methods) {
            if (execute.equals(method.getName())) {
                methodDescribeModel.setData(jdbc);
                //自动提交
                if (autoCommit) {
                    System.out.println("因为开启了自动提交，提交子节点model");
                    CollectionModelTools.INSTANCE.childrenEnd(null, jdbcModel, target, args, method);
                    this.methodDescribeModel = new MethodDescribeModel();
//                    CollectionModelTools.INSTANCE.createBaseCollectModel(jdbcModel, method, args, target.getClass(),null);
                }
                break;
            }
        }
    }

    private void setVal() {
        for (String param : prepared_statement_setMethods) {
            if (param.equals(method.getName())) {
                int i = (int) args[0];
                Object o = args[1];
                jdbc.putParams(new JdbcDescribeModel.ParamValues(i, o.toString()));
            }
        }
    }


}
