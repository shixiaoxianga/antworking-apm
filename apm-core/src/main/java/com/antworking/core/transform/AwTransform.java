package com.antworking.core.transform;

import com.antworking.core.enhance.AwEnhanceStatement;
import com.antworking.logger.AwLog;
import com.antworking.logger.LoggerFactory;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;

public class AwTransform implements AgentBuilder.Transformer {
    private AwLog log = LoggerFactory.getLogger(AwTransform.class);
    private final AwEnhanceStatement statement;

    public AwTransform(AwEnhanceStatement statement) {
        this.statement = statement;
    }

    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder,
                                            TypeDescription typeDescription,
                                            ClassLoader classLoader,
                                            JavaModule module,
                                            ProtectionDomain protectionDomain) {
        log.debug("transform class name:{}", typeDescription.getName());
        DynamicType.Builder<?> define = statement.define(builder, typeDescription, classLoader, module, protectionDomain);
        if (define == null) return builder;
        return define;
    }
}
