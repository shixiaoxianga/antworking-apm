package com.antworking.agent;

import com.antworking.core.config.AwConfigManager;
import com.antworking.core.factory.AntWorkingFactory;
import com.antworking.core.plugin.PluginManager;
import com.antworking.core.tools.AwPathManager;
import com.antworking.logger.AwLog;
import com.antworking.logger.LoggerFactory;
import com.antworking.utils.CommandManager;
import com.antworking.utils.FileReadUtil;
import net.bytebuddy.agent.builder.AgentBuilder;

import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.Properties;

import net.bytebuddy.agent.ByteBuddyAgent;

public class AntWorkingAgentApp {
    private static final AwLog log = LoggerFactory.getLogger(AntWorkingAgentApp.class);

    public static void main(String[] args) {

        premain(null, ByteBuddyAgent.install());
    }

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        welcome();

        String[] jvmArgs = ManagementFactory.getRuntimeMXBean().getInputArguments().toArray(new String[0]);

        for (String _arg : jvmArgs) {
            System.out.println("JVM Options: "+_arg);
        }

        Properties props = System.getProperties();
        System.out.println("\nSystem Properties:");
        props.list(System.out);
        System.setProperty("apm.jvm.args", Arrays.toString(jvmArgs));

        CommandManager.analysisCmd();
//        new ClientRun().run();
        AwConfigManager.initConfig();
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
        agentBuilder = AntWorkingFactory.INSTANCE.factoryAWBuild().ignore(agentBuilder);
        agentBuilder = AntWorkingFactory.INSTANCE.factoryAWBuild().strategy(agentBuilder);
        agentBuilder = AntWorkingFactory.INSTANCE.factoryAWBuild().listener(agentBuilder);
        agentBuilder = AntWorkingFactory.INSTANCE.factoryAWBuild().ignore(agentBuilder);
        agentBuilder = AntWorkingFactory.INSTANCE.factoryAWBuild().inject(agentBuilder, instrumentation);
        agentBuilder = AntWorkingFactory.INSTANCE.factoryAWBuild().applyPlugin(agentBuilder);
        agentBuilder.installOn(instrumentation);
    }


    @SuppressWarnings("all")
    public static void welcome() {
        log.info(FileReadUtil.readResourcesFile("antworking"));
    }


}
