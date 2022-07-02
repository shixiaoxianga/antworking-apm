package com.antworking.core;

import com.antworking.core.filter.IFilter;
import com.antworking.core.processor.MessageProcessor;
import com.antworking.core.tools.CollectionModelTools;
import com.antworking.model.base.BaseCollectModel;
import org.junit.platform.commons.util.ClassFilter;
import org.junit.platform.commons.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author XiangXiaoWei
 * date 2022/6/11
 */
public class AntWorkingContextManager {
    private static final Logger log = LoggerFactory.getLogger(AntWorkingContextManager.class);

    private final static List<IFilter> filters = new ArrayList<>();
    private final static List<MessageProcessor> processors = new ArrayList<>();

    private static final ThreadLocal<BaseCollectModel> session = new ThreadLocal<>();
    private static final ThreadLocal<AtomicInteger> invokerOrder = new ThreadLocal<>();

    AntWorkingContextManager() {

        initFilter();
        initMessageProcessor();
    }


    public static BaseCollectModel get() {
        synchronized (session) {
            return session.get();
        }
    }

    public static void linkModel(BaseCollectModel model) {
        BaseCollectModel threadLocalModel = get();
        if(threadLocalModel!=null){
            threadLocalModel.putChildes(model);
        }
    }

    public static void set(BaseCollectModel model) {
        synchronized (session){
            session.set(model);
        }
    }

    private void initMessageProcessor() {
        List<Class<?>> processClass = ReflectionUtils.findAllClassesInPackage(
                "com.antworking",
                ClassFilter.of(o -> MessageProcessor.class.isAssignableFrom(o) && !
                        o.isInterface())
        );
        processClass.forEach(clazz -> {
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
        processClass.forEach(clazz -> {
            try {
                filters.add((IFilter) clazz.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    public static void push(BaseCollectModel data) {
        try {
            // TODO: 2022/7/1 push
        } finally {
            remove();
        }
    }

    public static int getOrder() {
        if (invokerOrder.get() == null) {
            invokerOrder.set(new AtomicInteger(0));
        }
        return invokerOrder.get().getAndIncrement();
    }

    public static void remove() {
        session.remove();
        invokerOrder.remove();
    }


}
