package com.antworking.test;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class OSDemo {
    public static void main(String[] args) {
        OperatingSystemMXBean systemMXBean = ManagementFactory.getOperatingSystemMXBean();
        String arch = systemMXBean.getArch();
        String name = systemMXBean.getName();
        int availableProcessors = systemMXBean.getAvailableProcessors();
        double systemLoadAverage = systemMXBean.getSystemLoadAverage();
        String version = systemMXBean.getVersion();
        System.out.printf("架构：%s\n",arch);
        System.out.printf("名称：%s\n",name);
        System.out.printf("版本：%s\n",version);
        System.out.printf("虚拟机可用处理器数量：%s\n",availableProcessors);
        System.out.printf("最后一分钟系统平均负载：%s\n",systemLoadAverage);
    }
}
