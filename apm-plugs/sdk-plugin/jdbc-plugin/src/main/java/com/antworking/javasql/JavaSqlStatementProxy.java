package com.antworking.javasql;
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

    private final static String[] prepared_statement_setMethods =
            new String[]{"setNull", "setBoolean", "setByte", "setShort",
                    "setInt", "setLong", "setFloat", "setDouble", "setBigDecimal",
                    "setString", "setBytes", "setDate", "setTime", "setTimestamp", "setAsciiStream",
                    "setUnicodeStream", "setBinaryStream", "clearParameters", "setObject",
                    "setArray", "setDate", "setTime", "setTimestamp", "setNull", "setNString", "setSQLXML"};
    private final static String[] prepared_statement_methods = new String[]{"execute", "executeUpdate", "executeQuery"};


    public JavaSqlStatementProxy(Object target,  boolean autoCommit) {
        this.target = target;
        this.autoCommit = autoCommit;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        this.method = method;
        this.args = args;

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
                //如果不是自动提交需要等待 提交或回滚事务结束
                if (autoCommit) {
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
            }
        }
    }
}
