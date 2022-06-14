package com.antworking.agent;

import com.antworking.core.enhance.AbstractClassEnhance;
import com.antworking.core.interceptor.ClassEnhanceInterceptor;
import com.antworking.core.listener.AgentListener;
import com.antworking.util.FileReadUtil;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;
import org.junit.platform.commons.util.ClassFilter;
import org.junit.platform.commons.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

//@Slf4j
public class AntWorkingAgent {
    private static final Logger log = LoggerFactory.getLogger(AntWorkingAgent.class);

    public static void premain(String arg, Instrumentation instrumentation) {
        welcome();
        classEnhance(instrumentation);
//        test(instrumentation);
    }

    private static void test(Instrumentation instrumentation) {


        new AgentBuilder.Default()
                .disableClassFormatChanges()
                .ignore(ElementMatchers.nameStartsWith("java."))
                .type(ElementMatchers.nameStartsWith("com.xxw.test.controller"))
                .transform(new AgentBuilder.Transformer() {
                    @Override
                    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module) {
                        return builder
                                .method(ElementMatchers.any())
                                .intercept(MethodDelegation.to(new ClassEnhanceInterceptor(null)));
                    }
                })
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .with(new AgentBuilder.RedefinitionStrategy.Listener() {
                    @Override
                    public void onBatch(int index, List<Class<?>> batch, List<Class<?>> types) {
                        types.forEach(clazz -> {
                            System.out.println("onBatch" + clazz.getName());
                        });
                        batch.forEach(clazz -> {
                            System.out.println("onBatch" + clazz.getName());
                        });
                    }

                    @Override
                    public Iterable<? extends List<Class<?>>> onError(int index, List<Class<?>> batch, Throwable throwable, List<Class<?>> types) {
                        System.out.println("重新定义失败：" + index + "----" + throwable.toString());
                        System.out.println(throwable.getMessage());
                        types.forEach(clazz -> {
                            System.out.println("onError" + clazz.getName());
                        });
                        batch.forEach(clazz -> {
                            System.out.println("onError" + clazz.getName());
                        });
                        return null;
                    }

                    @Override
                    public void onComplete(int amount, List<Class<?>> types, Map<List<Class<?>>, Throwable> failures) {

                    }
                })
                // 指定需要拦截的类
                .with(new AgentListener())
                .installOn(instrumentation);
    }

    private static void classEnhance(Instrumentation instrumentation) {
        log.info("start scan enhanceClass...");
        List<Class<?>> enhanceClass = ReflectionUtils.findAllClassesInPackage(
                "com.antworking",
                ClassFilter.of(o -> {
                    return AbstractClassEnhance.class.isAssignableFrom(o)
                            && !Modifier.isAbstract(o.getModifiers()) && !o.isInterface();
                })
        );

        log.info("scan enhanceClass finish");
        AgentBuilder builder = new AgentBuilder.Default()
                .with(new AgentListener());
        for (Class<?> aClass : enhanceClass) {
            try {
                AbstractClassEnhance classEnhance = (AbstractClassEnhance) aClass.newInstance();
                builder = agentBuilder(builder, classEnhance);
            } catch (Exception e) {
                log.error("init classEnhance error :{}", e.toString());
            }
        }
        builder.installOn(instrumentation);
        log.info("antworking end...");
    }

    private static AgentBuilder agentBuilder(AgentBuilder builder, AbstractClassEnhance classEnhance) {
        // TODO: 2022/6/11 待处理
        return builder
                .type(ElementMatchers.named(classEnhance.getClassName()))
                .transform(new Transformation(classEnhance));
    }

    public static void welcome() {
        log.info(FileReadUtil.readResourcesFile("antworking"));
    }

    public static class Transformation implements AgentBuilder.Transformer {

        public AbstractClassEnhance classEnhance;

        public Transformation(AbstractClassEnhance classEnhance) {
            this.classEnhance = classEnhance;
        }

        @Override
        public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder,
                                                TypeDescription typeDescription,
                                                ClassLoader classLoader,
                                                JavaModule module) {
            System.out.println("=================="+typeDescription);
            return builder
                    .method(classEnhance.buildMethodMatchers())
                    .intercept(MethodDelegation.to(new ClassEnhanceInterceptor(classEnhance)));
        }
    }

}
