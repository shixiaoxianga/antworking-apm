package com.antworking.agent;

import com.antworking.core.classload.AntWorkingClassLoad;
import com.antworking.core.factory.AntWorkingFactory;
import com.antworking.core.plugin.PluginManager;
import com.antworking.logger.AwLog;
import com.antworking.logger.LoggerFactory;
import com.antworking.util.FileReadUtil;
import net.bytebuddy.agent.builder.AgentBuilder;

import java.lang.instrument.Instrumentation;

import net.bytebuddy.agent.ByteBuddyAgent;

public class AntWorkingAgentApp {
    private static final AwLog log = LoggerFactory.getLogger(AntWorkingAgentApp.class);

    public static void main(String[] args) {
        premain(null, ByteBuddyAgent.install());
    }

    public static void premain(String arg, Instrumentation instrumentation) {
        welcome();
        initPlugin();
        initByteBuddy(instrumentation);
        log.info("AntWorking init end...");
    }

    private static void initPlugin() {
        log.info("AntWorking start scan plugin...");
        PluginManager.scanPlugin();
        log.info("AntWorking scan plugin finish");
    }

    private static void initByteBuddy(Instrumentation instrumentation) {
        AgentBuilder agentBuilder = new AgentBuilder.Default();
//        AntWorkingFactory.INSTANCE.factoryAWBuild().ignore(agentBuilder);
//        AntWorkingFactory.INSTANCE.factoryAWBuild().initialization(agentBuilder);
        AntWorkingFactory.INSTANCE.factoryAWBuild().listener(agentBuilder);
//        AntWorkingFactory.INSTANCE.factoryAWBuild().ignore(agentBuilder);
//        AntWorkingFactory.INSTANCE.factoryAWBuild().inject(agentBuilder, instrumentation);
        agentBuilder = AntWorkingFactory.INSTANCE.factoryAWBuild().applyPlugin(agentBuilder);
        agentBuilder.installOn(instrumentation);
    }


    @SuppressWarnings("all")
    public static void welcome() {
        log.info(FileReadUtil.readResourcesFile("antworking"));
    }


}
