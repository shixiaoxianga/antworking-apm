package com.antworking.agent;

import com.antworking.common.ConstantAgent;
import com.antworking.core.classload.AntWorkingClassLoad;
import com.antworking.core.enhance.AbstractClassEnhance;
import com.antworking.core.interceptor.ClassEnhanceInterceptor;
import com.antworking.core.listener.AgentListener;
import com.antworking.util.FileReadUtil;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;
import org.junit.platform.commons.util.ClassFilter;
import org.junit.platform.commons.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

//@Slf4j
public class AntWorkingAgent {
    private static final Logger log = LoggerFactory.getLogger(AntWorkingAgent.class);



    public static void premain(String arg, Instrumentation instrumentation) {
        welcome();
        classEnhance(instrumentation);
    }

    private static void classEnhance(Instrumentation instrumentation) {
        log.info("start scan enhanceClass...");
        AntWorkingClassLoad.scanPlugin();
        log.info("scan enhanceClass finish");
        AgentBuilder builder = new AgentBuilder.Default()
                .ignore(ElementMatchers.nameStartsWith("com.intellij"))
                .ignore(ElementMatchers.nameStartsWith("com.antworking"))
                .with(new AgentListener());
        for (Class<?> aClass : AntWorkingClassLoad.plugins) {
            try {
                AbstractClassEnhance classEnhance = (AbstractClassEnhance) aClass.newInstance();
                classEnhance.init();
                builder = agentBuilder(builder, classEnhance);
            } catch (Exception e) {
                log.error("init classEnhance error :{}", e.toString());
            }
            builder.installOn(instrumentation);
        }
        log.info("antworking end...");
    }



    private static AgentBuilder agentBuilder(AgentBuilder builder, AbstractClassEnhance classEnhance) {
        return builder
                .type(classEnhance.buildTypeMatchers())
                .transform(new Transformation(classEnhance));
    }

    public static void welcome() {
        log.info(FileReadUtil.readResourcesFile("antworking"));
    }



}
