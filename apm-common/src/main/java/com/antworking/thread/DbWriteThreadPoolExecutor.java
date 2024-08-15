package com.antworking.thread;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * describe：构建线程池
 * @author XiangXiaoWei
 * date 2022/6/8
 */
public class DbWriteThreadPoolExecutor {

    private static final Lock lock = new ReentrantLock();

    /**
     * 处理器数量
     */
    private static final int cpuNum = Runtime.getRuntime().availableProcessors();

    private static volatile ExecutorService executor = null;

    public static ExecutorService getThreadPool(String threadName,
                                                Integer corePoolSize,
                                                Integer coreMaxPollSize,
                                                Integer keepAliveTime,
                                                Integer queueSize){
        lock.lock();
        try {
            if(executor ==null){
                BlockingDeque< Runnable > queue = new LinkedBlockingDeque<>(queueSize);
                executor = new java.util.concurrent.ThreadPoolExecutor(
                        corePoolSize ,
                        coreMaxPollSize,
                        keepAliveTime,
                        TimeUnit.MINUTES,
                        queue,
                        new SimpleThreadFactory(threadName),
                        //抛异常
                        new java.util.concurrent.ThreadPoolExecutor.AbortPolicy()
                        );
            }
            return executor;
        }finally {
            lock.unlock();
        }
    }

//    public static void main(String[] args) {
//        ExecutorService executorService = DbWriteThreadPoolExecutor.getThreadPool();
//
//        for (int i = 0; i < 20; i++) {
//            assert executorService != null;
//            executorService.submit(()->{
//                System.out.println("s b z z h");
//            });
//        }
//
//    }
}
