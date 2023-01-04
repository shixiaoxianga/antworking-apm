package com.antworking.test;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MemoryPoolDemo {
    public static void main(String[] args) {
        ScheduledExecutorService executors = new ScheduledThreadPoolExecutor(10);
        executors.scheduleAtFixedRate(() -> {
            byte[] bytes = new byte[1024*1024];
            List<MemoryPoolMXBean> memoryPoolMXBeans =
                    ManagementFactory.getMemoryPoolMXBeans();
            for (MemoryPoolMXBean poolMXBean : memoryPoolMXBeans) {
                MemoryUsage usage = poolMXBean.getUsage();
                String poolMXBeanName = poolMXBean.getName();
//                long usageThreshold = poolMXBean.getUsageThreshold();
                String[] managerNames = poolMXBean.getMemoryManagerNames();
                System.out.printf("内存池名称：%s\n",poolMXBeanName);
//                System.out.printf("内存池阈值：%s\n",usageThreshold);
                System.out.printf("内存池管理名称：%s\n", Arrays.toString(managerNames));
                System.out.printf("初始内存大小：%sMB \n最大内存大小：%sMB \n已使用内存大小：%sMB \n可使用内存大小：%sMB \n",
                        usage.getInit()/1024/1024,usage.getMax()/1024/1024,
                        usage.getUsed()/1024/1024,usage.getCommitted()/1024/1024);
                System.out.println("--------------------------");
            }
            System.out.println("===============================");
        }, 0, 50000, TimeUnit.SECONDS);
    }

}
