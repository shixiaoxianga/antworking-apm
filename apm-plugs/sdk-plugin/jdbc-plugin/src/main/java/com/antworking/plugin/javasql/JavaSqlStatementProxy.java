package com.antworking.plugin.javasql;

import com.antworking.core.collect.AwCollectManager;
import com.antworking.core.common.ConstantAppNode;
import com.antworking.plugin.javasql.model.JdbcDescribeModel;
import com.antworking.model.collect.CollectDataBaseModel;
import com.antworking.model.collect.MethodDescribeModel;
import com.antworking.utils.TimeUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class JavaSqlStatementProxy implements InvocationHandler {

    private Object target;
    private Throwable e;
    private Method method;
    private Object[] args;
    private Object result;
    private boolean autoCommit;
    private long beginTime;

    private JdbcDescribeModel jdbcDescribeModel;
    public final static ThreadLocal<List<CollectDataBaseModel>> jdbcSession = new ThreadLocal<>();

    private final static String[] prepared_statement_setMethods =
            new String[]{"setNull", "setBoolean", "setByte", "setShort",
                    "setInt", "setLong", "setFloat", "setDouble", "setBigDecimal",
                    "setString", "setBytes", "setDate", "setTime", "setTimestamp", "setAsciiStream",
                    "setUnicodeStream", "setBinaryStream", "clearParameters", "setObject",
                    "setArray", "setDate", "setTime", "setTimestamp", "setNull", "setNString", "setSQLXML"};
    private final static String[] prepared_statement_methods = new String[]{"execute", "executeUpdate", "executeQuery"};


    public JavaSqlStatementProxy(Object target, boolean autoCommit, JdbcDescribeModel jdbcDescribeModel) {
        this.target = target;
        this.autoCommit = autoCommit;
        this.jdbcDescribeModel = jdbcDescribeModel;
        if (jdbcSession.get() == null) {
            jdbcSession.set(new ArrayList<>());
        }
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        this.method = method;
        this.args = args;
        beginTime = TimeUtil.getCurrentTimeNano();
        try {
            result = method.invoke(target, args);
        } catch (Throwable e) {
            this.e = e;
            throw e;
        }
        setVal();
        execute();
        return result;
    }


    private void execute() {
        for (String execute : prepared_statement_methods) {
            if (execute.equals(method.getName())) {
                MethodDescribeModel methodDescribeModel = new MethodDescribeModel();
                methodDescribeModel.setName(method.getName());
                methodDescribeModel.setParam(args);
                methodDescribeModel.setClazz(target.getClass().getName());
                methodDescribeModel.setReturnClazz(result.getClass().getName());
                methodDescribeModel.setData(jdbcDescribeModel);
                CollectDataBaseModel model = CollectDataBaseModel.init(AwCollectManager.get() != null,
                        methodDescribeModel,
                        ConstantAppNode.SQL_DRIVE_STATEMENT,
                        Thread.currentThread().getName(),
                        AwCollectManager.getTraceId());
                model.setBeginTime(beginTime);
                //如果不是自动提交需要等待 提交或回滚事务结束
                if (autoCommit) {
                    jdbcDescribeModel.setAutoCommit("autoCommit");
                    model.setEndTime(TimeUtil.getCurrentTimeNano());
                    AwCollectManager.put(model);
                } else {
                    model.setEndTime(TimeUtil.getCurrentTimeNano());
                    jdbcSession.get().add(model);
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
                String val = null;
                if (o != null) val = o.toString();
                jdbcDescribeModel.getParams().add(new JdbcDescribeModel.ParamValues(i, val));
            }
        }
    }
}
