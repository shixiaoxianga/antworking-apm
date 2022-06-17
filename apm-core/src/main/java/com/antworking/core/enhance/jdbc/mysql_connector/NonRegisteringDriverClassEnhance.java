package com.antworking.core.enhance.jdbc.mysql_connector;

import com.antworking.common.ConstantNode;
import com.antworking.core.AntWorkingContextManager;
import com.antworking.core.enhance.AbstractClassEnhance;
import com.antworking.core.tools.CollectionModelTools;
import com.antworking.model.base.BaseCollectModel;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
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
    public String getClassName() {
        return prepared_statement_class;
    }

    @Override
    public ElementMatcher<? super MethodDescription> buildMethodMatchers() {
        return arrayToMatcher(non_registering_driver_methods);
    }

    @Override
    public void invokeMethodBefore(Class<?> clazz, Method method, Object[] args, BaseCollectModel model) {
        model.setNode(ConstantNode.MYSQL_CONNECTOR);
        model.setCrux(true);
    }

    @Override
    public Object invokeMethodAfter(Class<?> clazz, Method method, Object[] args, Object result, BaseCollectModel model) {
        if (result instanceof java.sql.Connection) {
            return Proxy.newProxyInstance(this.getClass().getClassLoader(),
                    new Class[]{java.sql.Connection.class},
                    new MysqlConnectorConnectionProxy(result,model));
        }
        return result;
    }

    @Override
    public void invokeMethodException(Class<?> clazz, Method method, Object[] args, Throwable e, BaseCollectModel model) {
    }
}
