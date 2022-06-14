package com.antworking.core.enhance.jdbc.mysql_connector;

import com.antworking.common.ConstantNode;
import com.antworking.core.AntWorkingContextManager;
import com.antworking.core.enhance.AbstractClassEnhance;
import com.antworking.core.matchers.AbstractMethodMatchers;
import com.antworking.model.base.BaseCollectModel;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author XiangXiaoWei
 * date 2022/6/14
 */
public class NonRegisteringDriverClassEnhance extends AbstractClassEnhance {
    private final static String prepared_statement_class = "com.mysql.cj.jdbc.NonRegisteringDriver";
/*    private final static String[] prepared_statement_setMethods =
            new String[]{"setNull", "setBoolean", "setByte","setShort",
                    "setInt","setLong","setFloat","setDouble","setBigDecimal",
                    "setString","setBytes","setDate","setTime","setTimestamp","setAsciiStream",
                    "setUnicodeStream","setBinaryStream","clearParameters","setObject",
                    "setArray","setDate","setTime","setTimestamp","setNull","setNString","setSQLXML"};
    private final static String[] prepared_statement_methods = new String[]{"execute", "executeUpdate", "executeQuery"};*/
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
    public void invokeMethodBefore(Class<?> clazz, Method method, Object[] args,BaseCollectModel model) {
        model.setNode(ConstantNode.MYSQL_CONNECTOR);
        model.setCrux(true);
    }

    @Override
    public void invokeMethodAfter(Class<?> clazz, Method method, Object[] args, Object result,BaseCollectModel model) {
        System.out.println(model);
    }

    @Override
    public void invokeMethodException(Class<?> clazz, Method method, Object[] args, Throwable e,BaseCollectModel model) {

    }
}
