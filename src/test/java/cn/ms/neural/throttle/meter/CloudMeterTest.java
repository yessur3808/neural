package cn.ms.neural.throttle.meter;

import cn.ms.neural.throttle.MeterListenerIpml;
import cn.ms.neural.throttle.limter.RealTimeLimiter;
import cn.ms.neural.throttle.support.IntervalModel;

public class CloudMeterTest {
    final static private RealTimeLimiter realTimeLimiter = new RealTimeLimiter(100);
    CloudMeter cloudMeter = CloudMeter.getSingleInstance();

//    @Test
    public void printStats() throws Exception {

        cloudMeter.registerListener(new MeterListenerIpml());

        cloudMeter.setIntervalModel(IntervalModel.ALL);
      /*  MeterTopic meterTopic = new MeterTopic();
        meterTopic.setTag("*");*/
        //cloudMeter.setAcquireMeterTopic("mytag66");
        MeterTopic meterTopic = new MeterTopic();
        meterTopic.setTag("mytag777");
        meterTopic.setType("sendMsg");
        cloudMeter.setAcquireMeterTopic(meterTopic);

        // cloudMeter.setAcquireMeterTopic("*");

        for (int i = 0; i < 1000000; i++) {
            if (i == 100) {
                realTimeLimiter.setRate(10);
                System.out.println("setRate(10)");
            } else if (i == 150) {
                realTimeLimiter.setRate(100);
                System.out.println("setRate(100)");
            } else if (i == 600) {
                realTimeLimiter.setRate(1000);
                System.out.println("setRate(1000)");
            }
            realTimeLimiter.acquire();

            MeterTopic meterTopic1 = new MeterTopic();
            meterTopic1.setTag("mytag777");
            meterTopic1.setType("sendMsg");
            cloudMeter.request(meterTopic1);

            cloudMeter.request();
            cloudMeter.request("mytag4");
            cloudMeter.request("mytag66");
            cloudMeter.request("mytag888");
        }
    }

}