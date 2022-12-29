package com.antworking.bytebuddy.jdk;

import com.antworking.bytebuddy.AgentListener;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.jar.JarFile;

public class BootstrapAgentAdvice {

    public static void main(String[] args) throws Exception {
        premain(null, net.bytebuddy.agent.ByteBuddyAgent.install());
        final ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(() -> {
            System.out.println("run...");
        });
        print();
    }

    public static void print() {
        System.out.println("print");
    }

    public static void premain(String arg, Instrumentation instrumentation) throws Exception {
        instrumentation.appendToSystemClassLoaderSearch(new JarFile("" +
                "/Users/xiangzhongwei/development/my-project/java-code/" +
                "antworking-apm/apm-test-model/jdk-intercept-test/target/" +
                "jdk-intercept-test-1.0.0.jar"));
        AgentBuilder builder = new AgentBuilder.Default()
                .with(new AgentListener())
                .disableClassFormatChanges()
                .ignore(ElementMatchers.nameStartsWith("com.intellij"))
                .type(ElementMatchers.named("java.util.concurrent.ThreadPoolExecutor"))
                .transform((builder1, typeDescription, classLoader, javaModule, protectionDomain) -> {
                    DynamicType.Builder.MethodDefinition.ImplementationDefinition<?> definition
                            = builder1
                            .method(ElementMatchers.any());
                    Object instance = null;
                    Class<?> aClass = null;
                    try {
                        aClass = Class.forName("com.antworking.apm.JdkAdvice");
                        instance = aClass.newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    assert instance != null;
                    try {
                        return definition.intercept(Advice.to(aClass));
//                        return definition.intercept(Advice.to(JdkAdviceTest.class));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        builder.installOn(instrumentation);
    }


}

