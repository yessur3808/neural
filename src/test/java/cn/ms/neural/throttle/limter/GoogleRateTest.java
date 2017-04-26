package cn.ms.neural.throttle.limter;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.google.common.util.concurrent.RateLimiter;

public class GoogleRateTest {

    @Test
    public void test1() {
        RateLimiter rateLimiter = RateLimiter.create(1000);
        AtomicInteger num = new AtomicInteger(0);

        /*while (true) {
            rateLimiter.acquire(5000);
            printTime();
        }*/

        System.out.println("-----");

        rateLimiter.acquire(10000);
        // CloudTicker.sleepSeconds(1);

        int num1 = num.addAndGet(1);
        System.out.println("-----" + num1);
        printTime(num1);
        rateLimiter.acquire(100);

        printTime(num.addAndGet(1));
        rateLimiter.acquire(100);

        printTime(num.addAndGet(1));
        rateLimiter.acquire(100);

        printTime(num.addAndGet(1));
        rateLimiter.acquire(3000);

        printTime(num.addAndGet(1));
        rateLimiter.acquire(1000);

        printTime(num.addAndGet(1));
        rateLimiter.acquire(3000);

        printTime(num.addAndGet(1));
        rateLimiter.acquire(1000);

        printTime(num.addAndGet(1));
        rateLimiter.acquire(3000);

        printTime(num.addAndGet(1));
        rateLimiter.acquire();
        /*
        printTime();
        rateLimiter.acquire(500);
        CloudTicker.sleepSeconds(1);

        printTime();
        rateLimiter.acquire(500);
        CloudTicker.sleepSeconds(1);

        printTime();
        rateLimiter.acquire(500);
        CloudTicker.sleepSeconds(1);

        printTime();
        rateLimiter.acquire(100000);
        CloudTicker.sleepSeconds(1);*/
    }

    private static void printTime(int num) {
        System.out.println(num + ":" + new Date());
    }

    @Test
    public void test2() {
        RateLimiter rateLimiter = RateLimiter.create(0.000001);


        while (true) {
            rateLimiter.acquire();
            printTime(1);
        }
    }
}
