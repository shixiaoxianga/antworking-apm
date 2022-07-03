package com.antworking.bytebuddy.jdk;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.modifier.Ownership;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassInjector;
import net.bytebuddy.implementation.LoadedTypeInitializer;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.pool.TypePool;
import net.bytebuddy.utility.JavaModule;

import java.io.*;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Collections;

public class BootstrapAgent {

    public static void main(String[] args) throws Exception {
        premain(null, net.bytebuddy.agent.ByteBuddyAgent.install());
/*        final ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(()->{

        });*/
        //        URL url = new URL("https://qzone-music.qq.com/fcg-bin/cgi_playlist_xml.fcg?uin=QQ%E5%8F%B7%E7%A0%81&json=1&g_tk=1916754934");
        URL url = new URL("https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=13176008332");
//        URL url = new URL("http://www.baidu.com");
        URLConnection conn = url.openConnection();
        conn.connect();
        InputStream input = conn.getInputStream();
        StringBuffer result = new StringBuffer();
        BufferedReader in = null;
        in = new BufferedReader(new InputStreamReader(input));
        String line;
        while ((line = in.readLine()) != null) {
            result.append(line);
        }

        System.out.println("r:"+result);
      
    }

    public static void premain(String arg, Instrumentation instrumentation) throws Exception {
        File temp = Files.createTempDirectory("tmp").toFile();
        ClassInjector.UsingInstrumentation.of(temp, ClassInjector.UsingInstrumentation.Target.BOOTSTRAP, instrumentation)
                .inject(Collections.singletonMap(
                        new TypeDescription.ForLoadedType(GeneralInterceptor.class),
                        ClassFileLocator.ForClassLoader.read(GeneralInterceptor.class)));

        String interceptor = "com.antworking.bytebuddy.jdk.GeneralInterceptor";
        ClassFileLocator locator = ClassFileLocator.ForClassLoader.ofSystemLoader();
        TypePool pool = TypePool.Default.of(locator);
        final TypeDescription target = pool.describe(interceptor).resolve();

//        File temp = Files.createTempDirectory("tmp").toFile();
        ClassInjector.UsingInstrumentation
                .of(temp, ClassInjector.UsingInstrumentation.Target.BOOTSTRAP, instrumentation)
                .injectRaw(Collections.singletonMap(interceptor, locator.locate(interceptor).resolve()));

        File tem = build(instrumentation);

        new AgentBuilder.Default()
                .ignore(ElementMatchers.nameStartsWith("net.bytebuddy."))
                .with(new AgentBuilder.InjectionStrategy.UsingInstrumentation(instrumentation, temp))
                .with(new AgentBuilder.InjectionStrategy.UsingInstrumentation(instrumentation, tem))
                .type(ElementMatchers.nameEndsWith("java.util.concurrent.ThreadPoolExecutor")
                        .or(ElementMatchers.nameEndsWith("HttpsURLConnectionImpl")))
                .transform(new AgentBuilder.Transformer() {
                    @Override
                    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule) {
                        return builder.defineField("delegator", target, Ownership.STATIC, Visibility.PUBLIC)
                                .initializer(new LoadedTypeInitializer() {
                                    @Override
                                    public void onLoad(Class<?> type) {
                                        try {
                                            Field field = type.getDeclaredField("delegator");
                                            field.setAccessible(true);
                                            field.set(null, Class.forName(interceptor, false, null)
                                                    .getConstructor(Class.forName("com.antworking.bytebuddy.jdk.BootstrapAgent")).newInstance(new BootstrapAgent()));
                                        } catch (Exception e) {
                                            throw new RuntimeException(e);
                                        }
                                    }

                                    @Override
                                    public boolean isAlive() {
                                        return true;
                                    }
                                })
                                .method(ElementMatchers.any())
                                .intercept(MethodDelegation.toField("delegator"));
                    }
                })
                .installOn(instrumentation);
    }

    private static File build(Instrumentation instrumentation) {
        File temp = null;
        try {
            temp = Files.createTempDirectory("tmp").toFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ClassInjector.UsingInstrumentation.of(temp, ClassInjector.UsingInstrumentation.Target.BOOTSTRAP, instrumentation)
                .inject(Collections.singletonMap(
                        new TypeDescription.ForLoadedType(BootstrapAgent.class),
                        ClassFileLocator.ForClassLoader.read(BootstrapAgent.class)));

        String interceptor = "com.antworking.bytebuddy.jdk.BootstrapAgent";
        ClassFileLocator locator = ClassFileLocator.ForClassLoader.ofSystemLoader();
        TypePool pool = TypePool.Default.of(locator);
        final TypeDescription target = pool.describe(interceptor).resolve();

//        File temp = Files.createTempDirectory("tmp").toFile();
        try {
            ClassInjector.UsingInstrumentation
                    .of(temp, ClassInjector.UsingInstrumentation.Target.BOOTSTRAP, instrumentation)
                    .injectRaw(Collections.singletonMap(interceptor, locator.locate(interceptor).resolve()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }

}

