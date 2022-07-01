package com.antworking.agent;

import com.antworking.core.classload.AntWorkingClassLoad;
import com.antworking.core.enhance.AbstractClassEnhance;
import com.antworking.core.interceptor.ClassEnhanceInterceptor;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.utility.JavaModule;

public class Transformation implements AgentBuilder.Transformer {

    public AbstractClassEnhance classEnhance;

    public Transformation(AbstractClassEnhance classEnhance) {
        this.classEnhance = classEnhance;
    }

    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder,
                                            TypeDescription typeDescription,
                                            ClassLoader classLoader,
                                            JavaModule module) {
        DynamicType.Builder.MethodDefinition.ImplementationDefinition<?> definition = builder
                .method(classEnhance.buildMethodMatchers());

        if (classEnhance.interceptorClass() == null) {
            return definition
                    .intercept(MethodDelegation.to(new ClassEnhanceInterceptor(classEnhance)));
        }
        try {
            final Class<?> aClass = Class.forName(classEnhance.interceptorClass(), true, new AntWorkingClassLoad());
            return definition
                    .intercept(Advice.to(aClass));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return builder;
    }
}