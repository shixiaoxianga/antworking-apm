package com.antworking.core.agent;

import com.antworking.common.BootstrapInjectClass;
import com.antworking.core.enhance.AwEnhanceStatement;
import com.antworking.core.plugin.PluginInst;
import com.antworking.core.plugin.PluginManager;
import com.antworking.core.transform.AwTransform;
import com.antworking.logger.AwLog;
import com.antworking.logger.LoggerFactory;
import com.antworking.core.tools.ClassUtil;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.dynamic.loading.ClassInjector;
import net.bytebuddy.matcher.ElementMatchers;

import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author AXiang
 * date 2022/12/29 13:45
 */
public enum DefaultAntWorkingAgentBuild implements AntWorkingAgentBuild {
    INSTANCE;
    private final AwLog log = LoggerFactory.getLogger(DefaultAntWorkingAgentBuild.class);
    private final Map<String, byte[]> classByteMap = new LinkedHashMap<>();


    @Override
    public AgentBuilder ignore(AgentBuilder agentBuilder) {
        // TODO: 2022/12/29 额外添加配置忽略
        return agentBuilder
                .ignore(ElementMatchers.named("net.bytebuddy"))
                .ignore(ElementMatchers.named("com.intellij"));
    }

    @Override
    public AgentBuilder listener(AgentBuilder agentBuilder) {
        return agentBuilder.with(new AntWorkingAgentListener());
    }

    private void initInjectClass() {
        for (String clazz : BootstrapInjectClass.awClass) {
            try {
                InputStream inputStream = ClassUtil.getClassInputStream(clazz);
                byte[] b = new byte[inputStream.available()];
                inputStream.read(b);
                classByteMap.put(clazz, b);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        for (String clazz : BootstrapInjectClass.byteBuddyClass) {
            try {
                InputStream inputStream = ClassUtil.getClassInputStream(clazz);
                byte[] b = new byte[inputStream.available()];
                inputStream.read(b);
                classByteMap.put(clazz, b);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public AgentBuilder inject(AgentBuilder agentBuilder, Instrumentation instrumentation) {
        initInjectClass();
        ClassInjector.UsingUnsafe.Factory factory = ClassInjector.UsingUnsafe.Factory.resolve(instrumentation);
        factory.make(null, null).injectRaw(classByteMap);
        return agentBuilder.with(new AgentBuilder.InjectionStrategy.UsingUnsafe.OfFactory(factory));
    }

    @Override
    public AgentBuilder applyPlugin(AgentBuilder agentBuilder) {
        for (PluginInst inst : PluginManager.pluginInst) {
            try {
                List<AwEnhanceStatement> statements = inst.getStatements();
                for (AwEnhanceStatement statement : statements) {
                    agentBuilder = agentBuilder.type(statement.matcherClass()).transform(new AwTransform(statement));
                }
            } catch (Throwable e) {
                log.error(e, "apply plugin error :{}", e.toString());
            }
        }
        return agentBuilder;
    }

    @Override
    public AgentBuilder strategy(AgentBuilder agentBuilder) {
        return agentBuilder.with(AgentBuilder.TypeStrategy.Default.REBASE);
    }
}
