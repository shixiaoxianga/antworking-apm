package com.antworking.apm.interfaces;

import com.antworking.apm.AwUser;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Modifier;

public class IAwCollectHandler implements AwCollectHandler {
    public String object;
    @Override
    public void set(String o) {
        object = o;
    }
    @Override
    public String get() {
      return object;
    }

    public static void main(String[] args) {
        AgentBuilder builder =
                new AgentBuilder.Default()
                        .ignore(ElementMatchers.nameStartsWith("com.intellij"))
                        .with(AgentBuilder.Listener.StreamWriting.toSystemOut())
                        .type(ElementMatchers.named("com.antworking.apm.AwUser"))
                        .transform((build, typeDescription, classLoader, javaModule, protectionDomain) -> {
                            return build.method((ElementMatchers.named("run")))
                                    .intercept(MethodDelegation
                                            .withDefaultConfiguration()
                                            .filter(ElementMatchers.not(ElementMatchers.isDeclaredBy(Object.class)))
                                            .to(new InterceptorObject()))
                                    .defineField("oaw", IAwCollectHandler.class, Modifier.PUBLIC);
                        })
                        .transform((build, typeDescription, classLoader, javaModule, protectionDomain) -> {
                            return build.method((ElementMatchers.named("run1")))
                                    .intercept(MethodDelegation
                                            .withDefaultConfiguration()
                                            .filter(ElementMatchers.not(ElementMatchers.isDeclaredBy(Object.class)))
                                            .to(new InterceptorStatic()));
                        });
        builder.installOn(ByteBuddyAgent.install());
        new AwUser().run();
        AwUser.run1();
    }
}