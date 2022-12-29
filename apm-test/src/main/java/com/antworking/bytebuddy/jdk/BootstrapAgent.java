package com.antworking.bytebuddy.jdk;

import com.antworking.bytebuddy.AgentListener;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.MethodNameEqualityResolver;
import net.bytebuddy.implementation.bind.annotation.BindingPriority;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.jar.JarFile;

public class BootstrapAgent {

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
        instrumentation.appendToBootstrapClassLoaderSearch(new JarFile("" +
                "/Users/xiangzhongwei/development/my-project/java-code/" +
                "antworking-apm/apm-test-model/jdk-intercept-test/target/" +
                "jdk-intercept-test-1.0.0.jar"));
        AgentBuilder builder = new AgentBuilder.Default()
                .with(new AgentListener())
                //重新定义策略
//                .with(AgentBuilder.InitializationStrategy.NoOp.INSTANCE)
//                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
//                .with(AgentBuilder.TypeStrategy.Default.REDEFINE)
                .ignore(ElementMatchers.nameStartsWith("com.intellij"))
                .type(ElementMatchers.named("java.util.concurrent.ThreadPoolExecutor"))
                .transform((builder1, typeDescription, classLoader, javaModule, protectionDomain) -> {
                    DynamicType.Builder.MethodDefinition.ImplementationDefinition<?> definition
                            = builder1
                            .method(ElementMatchers.any());
                    Object instance = null;
                    Class<?> aClass = null;
                    try {
                        aClass = Class.forName("com.antworking.apm.JdkInterceptor");
                        instance = aClass.newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    assert instance != null;
                    try {
                        MethodDelegation delegation = MethodDelegation.withDefaultConfiguration()
                                .filter(ElementMatchers.not(ElementMatchers.isDeclaredBy(Object.class))).to(instance);
                        return definition.intercept(delegation);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        builder.installOn(instrumentation);
    }


}

