package com.antworking.test;

import javax.management.ObjectName;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class GcDemo {
    public static void main(String[] args) {
        //-Xmx10m
        //-Xms10m
        ScheduledExecutorService executors = new ScheduledThreadPoolExecutor(10);
        executors.scheduleAtFixedRate(() -> {
            byte[] bytes = new byte[1024 * 1024];
            List<GarbageCollectorMXBean> collectorMXBeans =
                    ManagementFactory.getGarbageCollectorMXBeans();
            for (GarbageCollectorMXBean gc : collectorMXBeans) {
                String name = gc.getName();
                ObjectName objectName = gc.getObjectName();
                String[] poolNames = gc.getMemoryPoolNames();
                long collectionCount = gc.getCollectionCount();
                long collectionTime = gc.getCollectionTime();
                System.out.printf("\r内存管理器名称: %s\n", name);
                System.out.printf("\r内存池: %s\n", Arrays.toString(poolNames));
                System.out.printf("\r垃圾收集次数: %s\n", collectionCount);
                System.out.printf("\r垃圾收集时间: %sms\n", collectionTime);


                System.out.println("====================================");
            }
        }, 0, 2, TimeUnit.SECONDS);
    }
}
