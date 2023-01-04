package com.antworking.test;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;

public class ThreadDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(1000);
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        int daemonThreadCount = threadMXBean.getDaemonThreadCount();
        int peakThreadCount = threadMXBean.getPeakThreadCount();
        long currentThreadCpuTime = threadMXBean.getCurrentThreadCpuTime();
        long[] monitorDeadlockedThreads = threadMXBean.findMonitorDeadlockedThreads();
        System.out.printf("守护线程数量：%s\npeak线程数量：%s\n当前线程CPU总时间：%sms\n死锁线程ID：%s\n",daemonThreadCount,
                peakThreadCount,
                currentThreadCpuTime/1000000, Arrays.toString(monitorDeadlockedThreads));
        Thread.sleep(200);
        System.out.println(threadMXBean.getCurrentThreadCpuTime()/1000000);
    }
}
