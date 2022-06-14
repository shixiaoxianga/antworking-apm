package com.antworking.core;

import com.antworking.core.filter.IFilter;
import com.antworking.core.processor.MessageProcessor;
import com.antworking.model.base.BaseCollectModel;
import org.junit.platform.commons.util.ClassFilter;
import org.junit.platform.commons.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author XiangXiaoWei
 * date 2022/6/11
 */
public class AntWorkingContextManager {



    private final static List<IFilter> filters = new ArrayList<>();
    private final static List<MessageProcessor> processors = new ArrayList<>();

    AntWorkingContextManager() {

        initFilter();
        initMessageProcessor();
    }

    private void initMessageProcessor() {
        List<Class<?>> processClass = ReflectionUtils.findAllClassesInPackage(
                "com.antworking",
                ClassFilter.of(o -> MessageProcessor.class.isAssignableFrom(o) && !
                        o.isInterface())
        );
        processClass.forEach(clazz->{
            try {
                processors.add((MessageProcessor) clazz.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void initFilter() {
        List<Class<?>> processClass = ReflectionUtils.findAllClassesInPackage(
                "com.antworking",
                ClassFilter.of(o -> IFilter.class.isAssignableFrom(o) && !
                        o.isInterface())
        );
        processClass.forEach(clazz->{
            try {
                filters.add((IFilter) clazz.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    private static final ThreadLocal<BaseCollectModel> session = new ThreadLocal<>();
    private static final ThreadLocal<AtomicInteger> invokerOrder = new ThreadLocal<>();

    public static void push(BaseCollectModel data){
        try {

        } finally {
            invokerOrder.remove();
            session.remove();
        }
    }
    public static int getOrder(){
        if(invokerOrder.get()==null){
            invokerOrder.set(new AtomicInteger(0));
        }
        return invokerOrder.get().getAndIncrement();
    }

    public static void remove(){
        session.remove();
    }


}
