package com.antworking.plugin.jdk.jdbc;

import com.antworking.core.enhance.AbstractClassEnhance;
import com.antworking.model.base.BaseCollectModel;
import com.antworking.model.base.method.MethodDescribeModel;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JavaSqlDriverClassEnhance extends AbstractClassEnhance {
    private static final Logger log = LoggerFactory.getLogger(JavaSqlDriverClassEnhance.class);
    private final static String[] prepared_statement_class = {"com.mysql.cj.jdbc.NonRegisteringDriver","com.microsoft.sqlserver.jdbc.SQLServerDriver"};
    private final static String[] driver_methods = new String[]{"connect"};

    private final static ThreadLocal<BaseCollectModel> jdbcSession = new ThreadLocal<>();

    @Override
    public void init() {

    }

    @Override
    public String interceptorClass() {
        return null;
    }

    @Override
    public ElementMatcher<? super MethodDescription> buildMethodMatchers() {
        return arrayMethodToMatcher(driver_methods);
    }

    @Override
    public ElementMatcher<? super TypeDescription> buildTypeMatchers() {
        return ElementMatchers.named(prepared_statement_class[0]).or(ElementMatchers.named(prepared_statement_class[1]));
    }

    @Override
    public BaseCollectModel invokeMethodBefore(Class<?> clazz, Method method, Object[] args, BaseCollectModel model, MethodDescribeModel methodDescribeModel) {
        return model;
    }

    @Override
    public Object invokeMethodAfter(Class<?> clazz, Method method, Object[] args, Object result, BaseCollectModel model, MethodDescribeModel methodDescribeModel) {
        if (result instanceof java.sql.Connection) {
            return Proxy.newProxyInstance(this.getClass().getClassLoader(),
                    new Class[]{java.sql.Connection.class},
                    new JavaSqlConnectionProxy(result,jdbcSession));
        }
        return result;
    }

    @Override
    public void invokeMethodException(Class<?> clazz, Method method, Object[] args, Throwable e, BaseCollectModel model, MethodDescribeModel methodDescribeModel) {

    }
}
