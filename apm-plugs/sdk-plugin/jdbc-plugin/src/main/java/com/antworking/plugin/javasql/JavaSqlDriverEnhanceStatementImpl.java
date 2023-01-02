package com.antworking.plugin.javasql;

import com.antworking.core.enhance.AbstractEnhanceStatement;
import com.antworking.core.interceptor.AwMethodIntercept;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;

public class JavaSqlDriverEnhanceStatementImpl extends AbstractEnhanceStatement {
    private final static String[] prepared_statement_class = {
            "com.mysql.cj.jdbc.NonRegisteringDriver",
            "com.microsoft.sqlserver.jdbc.SQLServerDriver"
    };
    private final static String[] driver_methods = new String[]{"connect"};

    @Override
    public ElementMatcher<? super TypeDescription> doMatcherClass() {
        return ElementMatchers.named(prepared_statement_class[0])
                .or(ElementMatchers.named(prepared_statement_class[1]));
    }

    @Override
    public DynamicType.Builder<?> doDefine(DynamicType.Builder<?> builder,
                                         TypeDescription typeDescription,
                                         ClassLoader classLoader,
                                         JavaModule module,
                                         ProtectionDomain protectionDomain) {
        return builder.method(ElementMatchers.named(driver_methods[0]))
                .intercept(MethodDelegation.withDefaultConfiguration()
                        .filter(ElementMatchers.named(defaultInterceptMethodName()))
                        .to(new AwMethodIntercept(new JavaSqlDriverInterceptMethodHandler())));
    }
}
