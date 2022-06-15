package com.antworking.core.enhance.jdbc.mysql_connector;

import com.antworking.core.method.MethodTools;
import com.antworking.model.base.BaseCollectModel;
import com.antworking.model.base.jdbc.JdbcDescribeModel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MysqlConnectorStatementProxy implements InvocationHandler {

    private Object target;


    private Method method;
    private Object[] args;
    private Object result;
    private BaseCollectModel model;
    private JdbcDescribeModel.JdbcNode jdbcNode;
    private final static String[] prepared_statement_setMethods =
            new String[]{"setNull", "setBoolean", "setByte", "setShort",
                    "setInt", "setLong", "setFloat", "setDouble", "setBigDecimal",
                    "setString", "setBytes", "setDate", "setTime", "setTimestamp", "setAsciiStream",
                    "setUnicodeStream", "setBinaryStream", "clearParameters", "setObject",
                    "setArray", "setDate", "setTime", "setTimestamp", "setNull", "setNString", "setSQLXML"};
    private final static String[] prepared_statement_methods = new String[]{"execute", "executeUpdate", "executeQuery"};

    public MysqlConnectorStatementProxy(Object target, BaseCollectModel model, JdbcDescribeModel.JdbcNode jdbcNode) {
        this.target = target;
        this.model = model;
        this.jdbcNode = jdbcNode;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        this.method = method;
        this.args = args;
        try {
            result = method.invoke(target, args);
        } catch (Throwable e) {
            MethodTools.INSTANCE.end(e, model, target, args, method);
            throw e;
        }
        setVal();
        execute();
        return result;
    }

    private void execute() {
        for (String execute : prepared_statement_methods) {
            if (execute.equals(method.getName())) {
                MethodTools.INSTANCE.end(null, model, target, args, method);
                break;
            }
        }
    }
/*    private void end(Throwable e){
        model.setEndTime(System.currentTimeMillis());
        if(e!=null){
            ErrorDescribeModel error = new ErrorDescribeModel();
            error.setTimeStamp(System.currentTimeMillis());
            error.setStacks(Arrays.stream(e.getStackTrace()).map(Object::toString).toArray(String[]::new));
            error.setMessage(e.getMessage());
            error.setClazz(e.getClass().getName());

            MethodDescribeModel methodDescribeModel =
                    MethodErrorHandler.INSTANCE.buildMethodDes(target.getClass(),args,method);
            methodDescribeModel.setError(error);
            model.putMethods(methodDescribeModel);
        }
    }*/

    private void setVal() {
        for (String param : prepared_statement_setMethods) {
            if (param.equals(method.getName())) {
                int i = (int) args[0];
                Object o = args[1];
                jdbcNode.putParams(new JdbcDescribeModel.JdbcNode.ParamValues(i, o.toString()));
            }
        }
    }




}
