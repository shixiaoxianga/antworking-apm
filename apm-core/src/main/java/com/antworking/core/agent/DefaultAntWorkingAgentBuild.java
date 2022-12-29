package com.antworking.core.agent;

import com.antworking.common.BootstrapInjectClass;
import com.antworking.core.enhance.EnhanceStatement;
import com.antworking.core.plugin.PluginManager;
import com.antworking.core.transform.AwTransform;
import com.antworking.logger.AwLog;
import com.antworking.logger.LoggerFactory;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.dynamic.loading.ClassInjector;
import net.bytebuddy.matcher.ElementMatchers;

import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.util.HashMap;
import java.util.Map;

/**
 * @author AXiang
 * date 2022/12/29 13:45
 */
public class DefaultAntWorkingAgentBuild implements AntWorkingAgentBuild {
    private final AwLog log = LoggerFactory.getLogger(DefaultAntWorkingAgentBuild.class);
    private final Map<String, byte[]> classByteMap = new HashMap<>();

    public DefaultAntWorkingAgentBuild() {
        for (String clazz : BootstrapInjectClass.awClass) {
            try {
                InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(clazz);
                assert inputStream != null;
                byte[] b = new byte[inputStream.available()];
                inputStream.read(b);
                classByteMap.put(clazz, b);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void ignore(AgentBuilder agentBuilder) {
        // TODO: 2022/12/29 额外添加配置忽略
        agentBuilder
                .ignore(ElementMatchers.named("net.bytebuddy"))
                .ignore(ElementMatchers.named("com.intellij"));
    }

    @Override
    public void listener(AgentBuilder agentBuilder) {
        agentBuilder.with(new AntWorkingAgentListener());
    }

    @Override
    public void initialization(AgentBuilder agentBuilder) {
        agentBuilder.with(AgentBuilder.InitializationStrategy.NoOp.INSTANCE);
    }

    @Override
    public void inject(AgentBuilder agentBuilder, Instrumentation instrumentation) {
        ClassInjector.UsingUnsafe.Factory factory = ClassInjector.UsingUnsafe.Factory.resolve(instrumentation);
        factory.make(null, null).injectRaw(classByteMap);
        agentBuilder.with(new AgentBuilder.InjectionStrategy.UsingUnsafe.OfFactory(factory));
    }

    @Override
    public AgentBuilder applyPlugin(AgentBuilder agentBuilder) {
        for (Class<?> clazz : PluginManager.enhanceStatement) {
            try {
                EnhanceStatement statement = (EnhanceStatement) clazz.newInstance();
                agentBuilder = agentBuilder.type(statement.matcherClass())
                        .transform(new AwTransform(statement));
            } catch (Throwable e) {
                log.error(e, "apply plugin error :{}", e.toString());
            }
        }
        return agentBuilder;
    }
}
