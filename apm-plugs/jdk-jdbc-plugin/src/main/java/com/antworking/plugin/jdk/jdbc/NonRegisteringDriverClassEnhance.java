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

/**
 * @author XiangXiaoWei
 * date 2022/6/14
 */
public class NonRegisteringDriverClassEnhance extends AbstractClassEnhance {
    private static final Logger log = LoggerFactory.getLogger(NonRegisteringDriverClassEnhance.class);
    private final static String prepared_statement_class = "com.mysql.cj.jdbc.NonRegisteringDriver";
    private final static String[] non_registering_driver_methods = new String[]{"connect"};

    @Override
    public void init() {

    }

    @Override
    public String interceptorClass() {
        return null;
    }

    @Override
    public ElementMatcher<? super MethodDescription> buildMethodMatchers() {
        return arrayToMatcher(non_registering_driver_methods);
    }

    @Override
    public ElementMatcher<? super TypeDescription> buildTypeMatchers() {
        return ElementMatchers.named(prepared_statement_class);
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
                    new MysqlConnectorConnectionProxy(result));
        }
        return result;
    }

    @Override
    public void invokeMethodException(Class<?> clazz, Method method, Object[] args, Throwable e, BaseCollectModel model, MethodDescribeModel methodDescribeModel) {
    }
}
