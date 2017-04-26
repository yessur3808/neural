package cn.ms.neural.throttle.limter;

import java.util.Date;

import org.junit.Test;

import cn.ms.neural.throttle.MeterListenerIpml;
import cn.ms.neural.throttle.meter.CloudMeter;

public class LimiterDelayExecutorTest {

    public static void main(String[] args) {
        final RealTimeLimiter realTimeLimiter = new RealTimeLimiter(LimiterDelayConstants.ONCE_PER_HOUR);

        while (true) {
            realTimeLimiter.acquire();
            System.out.println(new Date());
        }
    }

    @Test
    public void test1() {
        final RealTimeLimiter realTimeLimiter = new RealTimeLimiter(LimiterDelayConstants.ONCE_PER_SECOND);
        int i = 0;
        while (true) {
            i++;
            if (i % 10 == 0) {
                System.out.println("ONCE_PER_MINUTE");
                realTimeLimiter.setRate(LimiterDelayConstants.ONCE_PER_MINUTE);
            } else {
                realTimeLimiter.setRate(LimiterDelayConstants.ONCE_PER_SECOND);

            }
            realTimeLimiter.acquire();
            System.out.println(new Date());
        }
    }

    @Test
    public void test3() {
        final RealTimeLimiter realTimeLimiter = new RealTimeLimiter(LimiterDelayConstants.ONCE_PER_MINUTE);
        CloudMeter cloudMeter = CloudMeter.getSingleInstance();
        cloudMeter.registerListener(new MeterListenerIpml());

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // CloudTicker.sleepSeconds(10);
                // 此处启动线程设置1000必须等到一分钟后才生效（因为初始化为每分钟执行一次，不允许中断）
                // 不同线程设置rate是不可以中断等待抢占设置速率
                System.out.println("---1000");
                realTimeLimiter.setRate(1000);
            }
        });
        thread.start();

        // 同一个线程设置rate是可以抢占设置速率
        /*System.out.println("---100");
        realTimeLimiter.setRate(100);*/

        while (true) {
            realTimeLimiter.acquire();
            cloudMeter.request();
        }
    }
}
