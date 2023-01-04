
package com.antworking.test;
 
import com.sun.management.OperatingSystemMXBean;
 
import java.io.File;
import java.io.FileOutputStream;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
 
import java.lang.management.ThreadMXBean;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
 
public class JVMRuntimeInfoDemo {
 
    private static String timeMinutes;
 
    private static ArrayList<String> buff;
 
    private static Long index = 1l;
 
    public static void main(String[] args) throws InterruptedException {
 
        LinkedList<String> queue = new LinkedList<String>();
 
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdfMinutes = new SimpleDateFormat("yyyyMMdd-HHmmss");
 
        Long time = System.currentTimeMillis();
 
        timeMinutes = sdfMinutes.format(time);
 
        buff = new ArrayList<>(1024);
 
        for (int i = 0; i < 60; i++) {
            byte[] bytes = new byte[1024000];
 
            buff.add(bytes.toString());
 
            String dateTime = sdf.format(System.currentTimeMillis());
 
            saveRuntimeInfo(queue);
 
            Thread.sleep(3000);
        }
    }
 
    public static void saveRuntimeInfo(LinkedList<String> queue) {
 
        String fileName = "runtime" + timeMinutes + ".log";
        String filePath = getConfFile(fileName);
 
        try {
 
//            File confFile = new File(filePath);
//            if (!confFile.exists()) {
//                confFile.createNewFile();
//            }
 
//            FileOutputStream outputStream = new FileOutputStream(confFile);
 
            String configInfo = "";
 
            // 记录操作系统内存
            configInfo = getOSMemoryInfo(configInfo);
 
            // 记录虚拟机内存
            configInfo = getJVMMemoryInfo(configInfo);
 
            // 堆内存信息
            configInfo = getHeapInfo(configInfo);
 
            // 垃圾收集信息
            configInfo = getGCInfo(configInfo);
 
            // 线程信息
            configInfo = getThreadInfo(configInfo);
 
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 
            String dateTime = sdf.format(System.currentTimeMillis());
 
            // String poll = queue.poll();
            configInfo += "\n" + index + " " + dateTime + "\n\n";
 
            index = index + 1;
 
            if (index >= 100) {
                index = 1l;
            }
 
            System.out.println(configInfo);
 
            queue.addLast(configInfo);
 
            while (queue.size() > 30) {
                queue.removeFirst();
            }
 
            // 遍历
            for (String poll : queue) {
                System.out.println(poll.getBytes(StandardCharsets.UTF_8));
//                outputStream.write(poll.getBytes(StandardCharsets.UTF_8));
            }
 
//            outputStream.close();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    // 获取日志文件路径
    public static String getConfFile(String fileName) {
        URL resource1 = JVMRuntimeInfoDemo.class.getResource("/");
 
        String path = resource1.getPath();
 
        int endPos = path.indexOf(".jar!");
        if (endPos > 0)
            path = path.substring(0, endPos);
 
        endPos = path.lastIndexOf("/");
        if (path.startsWith("file:/"))
            path = path.substring(6, endPos);
        else if (path.startsWith("/"))
            path = path.substring(1, endPos);
 
        path = path + "/" + fileName;
 
        return path;
    }
 
    public static String getOSMemoryInfo(String baseInfo) {
        int byteToMb = 1024 * 1024;
 
        // 操作系统级内存情况查询
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        String os = System.getProperty("os.name");
        long physicalFree = osmxb.getFreePhysicalMemorySize() / byteToMb;
        long physicalTotal = osmxb.getTotalPhysicalMemorySize() / byteToMb;
        long physicalUse = physicalTotal - physicalFree;
 
        baseInfo = baseInfo + "\n======操作系统内存信息=======";
 
        baseInfo = baseInfo + "\n操作系统的版本：" + os;
 
        baseInfo = baseInfo + "\n操作系统内存已使用：" + physicalFree + " MB";
 
        baseInfo = baseInfo + "\n操作系统内存剩余：" + physicalUse + " MB";
 
        baseInfo = baseInfo + "\n操作系统总内存：" + physicalTotal + " MB";
 
        return baseInfo;
    }
 
    public static String getJVMMemoryInfo(String baseInfo) {
        // 虚拟机级内存情况查询
        long vmFree = 0;
        long vmUse = 0;
        long vmTotal = 0;
        long vmMax = 0;
        int byteToMb = 1024 * 1024;
        Runtime rt = Runtime.getRuntime();
        vmTotal = rt.totalMemory() / byteToMb;
        vmFree = rt.freeMemory() / byteToMb;
        vmMax = rt.maxMemory() / byteToMb;
        vmUse = vmTotal - vmFree;
 
        baseInfo = baseInfo + "\n======JVM内存信息=======";
        baseInfo += "\nJVM内存已用的空间为：" + vmUse + " MB";
        baseInfo += "\n" + "JVM内存的空闲空间为：" + vmFree + " MB";
        // jvm当前分配空间
        baseInfo += "\n" + "JVM总内总存空间为：" + vmTotal + " MB";
        // 与jvm配置有关
        baseInfo += "\n" + "JVM总内存最大空间为：" + vmMax + " MB";
 
        return baseInfo;
    }
 
    // 堆空间信息
    public static String getHeapInfo(String baseInfo) {
        int byteToMb = 1024 * 1024;
 
        MemoryMXBean memorymbean = ManagementFactory.getMemoryMXBean();
        MemoryUsage usage = memorymbean.getHeapMemoryUsage();
 
        baseInfo = baseInfo + "\n======堆内存信息=======";
 
        baseInfo += "\n初始堆大小:" + usage.getInit() / byteToMb + "MB";
        baseInfo += "\n最大值:" + usage.getMax() / byteToMb + "MB";
        baseInfo += "\n已使用:" + usage.getUsed() / byteToMb + "MB";
 
        return baseInfo;
    }
 
    // 垃圾收集信息
    public static String getGCInfo(String baseInfo) {
        List<GarbageCollectorMXBean> gcmList = ManagementFactory.getGarbageCollectorMXBeans();
 
        baseInfo = baseInfo + "\n======垃圾收集信息=======";
        for (GarbageCollectorMXBean gcm : gcmList) {
            baseInfo += "\n垃圾收集器名称：" + gcm.getName();
            long collectionTime = gcm.getCollectionTime();
            long collectionCount = gcm.getCollectionCount();
            baseInfo += "\n垃圾收集次数：" + collectionCount + " 次,垃圾收集时间：" + collectionTime + "毫秒";
        }
 
        return baseInfo;
    }
 
    // 线程信息
    public static String getThreadInfo(String baseInfo) {
        ThreadMXBean tm = (ThreadMXBean) ManagementFactory.getThreadMXBean();
 
        baseInfo = baseInfo + "\n======线程信息=======";
 
        baseInfo += "\n线程数：" + tm.getThreadCount();
        baseInfo += "\n最大线程数：" + tm.getPeakThreadCount();
        baseInfo += "\n守护线程数：" + tm.getDaemonThreadCount();
 
        return baseInfo;
    }
 
 
}