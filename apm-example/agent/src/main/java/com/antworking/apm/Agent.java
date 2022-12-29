package com.antworking.apm;

import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;


public class Agent {
    public static void main(String[] args) {
        premain(args, ByteBuddyAgent.install());
        new User().run();
        User.run1();
    }

    private static void print() {
        System.out.println("print test");
    }

    public static void premain(String[] args, Instrumentation instrumentation) {
        AgentBuilder builder = new AgentBuilder.Default()
                .ignore(ElementMatchers.nameStartsWith("com.intellij"))
                .with(AgentBuilder.Listener.StreamWriting.toSystemOut())
                .type(ElementMatchers.named("com.antworking.apm.User"))
                .transform((build, typeDescription, classLoader, javaModule, protectionDomain) -> {
                    return build
                            .method(ElementMatchers.named("run1"))
                            .intercept(MethodDelegation
                                    .withDefaultConfiguration()
                                    .filter(ElementMatchers.not(ElementMatchers.isDeclaredBy(Object.class)))
                                    .to(new Interceptor()));
                });
        builder.installOn(instrumentation);
    }

}