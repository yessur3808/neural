package cn.ms.neural.throttle.limter;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import cn.ms.neural.throttle.support.FlowUnit;

public class FlowTest {

    public static void main(String[] args) {
        FlowLimiter flowLimiterPerMinute = new FlowLimiter(1000, FlowUnit.BYTE, TimeUnit.MINUTES.toSeconds(1));

        for (int i = 0; i < 10000; i++) {
            flowLimiterPerMinute.acquire(1000, FlowUnit.BYTE);
            System.out.println(new Date());
        }
    }
}
