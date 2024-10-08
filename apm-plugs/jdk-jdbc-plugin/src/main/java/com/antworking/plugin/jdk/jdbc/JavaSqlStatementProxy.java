package com.antworking.plugin.jdk.jdbc;

import com.antworking.core.tools.CollectionModelTools;
import com.antworking.model.base.BaseCollectModel;
import com.antworking.model.base.error.ErrorDescribeModel;
import com.antworking.model.base.jdbc.JdbcDescribeModel;
import com.antworking.model.base.method.MethodDescribeModel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

public class JavaSqlStatementProxy implements InvocationHandler {

    private Object target;
    private Throwable e;
    private Method method;
    private Object[] args;
    private Object result;
    private boolean autoCommit;

    private BaseCollectModel jdbcModel;
    private MethodDescribeModel methodDescribeModel;
    private JdbcDescribeModel jdbcDescribeModel;

    private final static String[] prepared_statement_setMethods =
            new String[]{"setNull", "setBoolean", "setByte", "setShort",
                    "setInt", "setLong", "setFloat", "setDouble", "setBigDecimal",
                    "setString", "setBytes", "setDate", "setTime", "setTimestamp", "setAsciiStream",
                    "setUnicodeStream", "setBinaryStream", "clearParameters", "setObject",
                    "setArray", "setDate", "setTime", "setTimestamp", "setNull", "setNString", "setSQLXML"};
    private final static String[] prepared_statement_methods = new String[]{"execute", "executeUpdate", "executeQuery"};


    public JavaSqlStatementProxy(Object target, BaseCollectModel jdbcModel, boolean autoCommit, JdbcDescribeModel jdbcDescribeModel) {
        this.target = target;
        //此作用域由connection控制
        this.jdbcModel = jdbcModel;
        this.autoCommit = autoCommit;

        this.jdbcDescribeModel = jdbcDescribeModel;
        //初始化节点对象数据
        this.initObj();
    }

    private void initObj() {
        this.methodDescribeModel = new MethodDescribeModel();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        this.method = method;
        this.args = args;

        methodDescribeModel.setStartTime(System.currentTimeMillis());

        try {
            result = method.invoke(target, args);
        } catch (Throwable e) {
            this.e = e;
            CollectionModelTools.INSTANCE.childrenEnd(e, jdbcModel, target, args, method);
            throw e;
        }

        methodDescribeModel.setEndTime(System.currentTimeMillis());
        setVal();
        execute();
        return result;
    }


    private void execute() {
        for (String execute : prepared_statement_methods) {
            if (execute.equals(method.getName())) {
                methodDescribeModel.setData(jdbcDescribeModel);
                saveMethod(jdbcModel, methodDescribeModel);
                //如果不是自动提交需要等待 提交或回滚事务结束
                if (autoCommit) {
                    CollectionModelTools.INSTANCE.childrenEnd(null, jdbcModel, target, args, method);
                }
                break;
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

    private void setVal() {
        for (String param : prepared_statement_setMethods) {
            if (param.equals(method.getName())) {
                int i = (int) args[0];
                Object o = args[1];
                String val = null;
                if (o != null) val = o.toString();
                jdbcDescribeModel.putParams(new JdbcDescribeModel.ParamValues(i, val));
            }
        }
    }
}
