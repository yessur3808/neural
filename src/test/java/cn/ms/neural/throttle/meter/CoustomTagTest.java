package cn.ms.neural.throttle.meter;

import cn.ms.neural.throttle.MeterListenerIpml;
import cn.ms.neural.throttle.limter.RealTimeLimiter;

public class CoustomTagTest {
    public static void main(String[] args) {
        RealTimeLimiter realTimeLimiter = new RealTimeLimiter(10);
        CloudMeter cloudMeter = CloudMeter.getSingleInstance();
        cloudMeter.registerListener(new MeterListenerIpml());

        String str1 = "1";
        String str2 = "2";
        String tag = "tag-";
        int i = 0;
        while (true) {
            i++;
            realTimeLimiter.acquire();
            if (i % 5 == 0) {
                cloudMeter.request(tag + str1);
            } else {
                cloudMeter.request(tag + str2);
            }
        }
    }
}
