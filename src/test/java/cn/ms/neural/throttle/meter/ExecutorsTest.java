package cn.ms.neural.throttle.meter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.ms.neural.throttle.limter.BucketTicker;

public class ExecutorsTest {
    private final static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    static int num1 = 0;
    static int num2 = 0;

    public static void main(String[] args) {
        test1();
        test2();
    }

    public static void test1() {
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                printTime("test1: ");
                num1++;
                if (num1 % 3 == 0) {
                    BucketTicker.sleepMillis(3800);
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public static void test2() {
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                num2++;
                printTime("------------ test2: ");
                if (num2 % 5 == 0) {
                    BucketTicker.sleepMillis(500);
                }
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    private static void printTime(String tag) {
        System.out.println(tag + System.currentTimeMillis());
    }
}
