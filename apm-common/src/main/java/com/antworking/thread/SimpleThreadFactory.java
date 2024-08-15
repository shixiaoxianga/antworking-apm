package com.antworking.thread;


import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * describe：自定义线程创建
 * @author XiangXiaoWei
 * date 2022/6/8
 */
public class SimpleThreadFactory extends Timer implements ThreadFactory {

    private final String baseName;

    /**
     * 时间间隔(一天)
     */
    private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;
//    private static final long PERIOD_DAY = 4000;

    private final AtomicInteger integer = new AtomicInteger(0);


    public SimpleThreadFactory(String baseName) {
        this.baseName = baseName;
        initTimer();
    }

    private void initTimer() {
        Calendar calendar = Calendar.getInstance();
        //凌晨0点
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        //第一次执行定时任务的时间
        Date date=calendar.getTime();
        //如果第一次执行定时任务的时间 小于当前的时间
        //此时要在 第一次执行定时任务的时间加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
        if (date.before(new Date())) {
            date = this.addDay(date, 1);
        }
        //安排指定的任务在指定的时间开始进行重复的固定延迟执行。
        schedule(new TimerTask() {
            @Override
            public void run() {
                integer.set(0);
            }
        },date,PERIOD_DAY);
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r,baseName+"-"+System.currentTimeMillis()+"-"+integer.getAndIncrement());
    }

    // 增加或减少天数
    private Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }

    public static void main(String[] args) throws InterruptedException {
        SimpleThreadFactory test = new SimpleThreadFactory("test");
        for (int i = 0; i < 20; i++) {
            TimeUnit.SECONDS.sleep(2);
            test.newThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName());
                }
            }).start();
        }
    }


}
