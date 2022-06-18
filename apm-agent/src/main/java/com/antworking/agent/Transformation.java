package com.antworking.agent;

import com.antworking.core.enhance.AbstractClassEnhance;
import com.antworking.core.interceptor.ClassEnhanceInterceptor;
import net.bytebuddy.agent.builder.AgentBuilder;
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
            return builder
                    .method(classEnhance.buildMethodMatchers())
                    .intercept(MethodDelegation.to(new ClassEnhanceInterceptor(classEnhance)));
        }
    }