package com.antworking.bytebuddy.test;

import com.antworking.bytebuddy.annotations.SimpleMethodIntercept;
import com.antworking.bytebuddy.target.Apple;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.matcher.NameMatcher;
import net.bytebuddy.matcher.StringMatcher;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Iterator;

public class ApiTest {
    @Test
    public void createClass() throws Exception{
        //方法类型的描述 定义返回类型
        TypeDescription.Generic result = new TypeDescription.Generic.OfNonGenericType.ForLoadedType(String.class);
        final Iterator<TypeDefinition> iterator = result.iterator();
        final DynamicType.Unloaded<Object> make = new ByteBuddy()
                .subclass(Object.class)
                .name("com.xxw.generate.Test")
                .defineMethod("test", iterator.next(), Visibility.PUBLIC)
                .intercept(FixedValue.value("Hello World!"))
                .make();
        make.saveIn(new File("E:\\JavaCode\\antworking\\apm-test\\target"));
        Class<?> dynamicType = make
                .load(ApiTest.class.getClassLoader())
                .getLoaded();
        final Object o = dynamicType.newInstance();
        final Method method = dynamicType.getMethod("test");
        System.out.println(method.invoke(o));
    }


    @Test
    public void enhanceClass() throws Exception{
        ElementMatcher<? super MethodDescription> matcher = new NameMatcher<>(new StringMatcher("getEat",StringMatcher.Mode.CONTAINS));
//        Implementation impl =new Implementation.Composable (new ByteCodeAppender.Simple())
        final DynamicType.Unloaded<Apple> make = new ByteBuddy()
                .redefine(Apple.class)
                .name("com.antworking.bytebuddy.target.Apple$antworking")
//                .method(ElementMatchers.named("getEat"))
                .method(matcher)
                .intercept(FixedValue.value("蛋糕"))
//                .intercept(impl)
                .make();
        make.saveIn(new File("E:\\JavaCode\\antworking\\apm-test\\target"));
    }

    @Test
    public void redefineMethod() throws Exception{
        final DynamicType.Unloaded<Apple> make = new ByteBuddy()
                .subclass(Apple.class)
                .name("com.antworking.apm.test.Apple$enhance")
                .method(ElementMatchers.named("getEat").or(ElementMatchers.named("setEat")))
                .intercept(MethodDelegation.to(new SimpleMethodIntercept()))
                .make();

        make.saveIn(new File("E:\\JavaCode\\antworking\\apm-test\\target"));
        final Class<?> loaded = make.load(this.getClass().getClassLoader()).getLoaded();
        final Object o = loaded.newInstance();
        final Method getEat = loaded.getMethod("getEat");
        final Method setEat = loaded.getMethod("setEat",String.class);
        setEat.invoke(o,"香蕉");
        final Object invoke = getEat.invoke(o);
        System.out.println(invoke);
    }
}
