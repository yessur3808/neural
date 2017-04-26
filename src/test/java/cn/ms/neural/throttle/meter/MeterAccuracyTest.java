package cn.ms.neural.throttle.meter;

import cn.ms.neural.throttle.MeterListenerIpml;
import cn.ms.neural.throttle.limter.RealTimeLimiter;
import cn.ms.neural.throttle.support.IntervalModel;

public class MeterAccuracyTest {

    public static void main(String[] args) {
        RealTimeLimiter realTimeLimiter = new RealTimeLimiter(10);
        CloudMeter cloudMeter = CloudMeter.getSingleInstance();
        cloudMeter.setIntervalModel(IntervalModel.MINUTE);
        cloudMeter.registerListener(new MeterListenerIpml());

        while (true) {
            realTimeLimiter.acquire();
            cloudMeter.request();
        }
    }
}
