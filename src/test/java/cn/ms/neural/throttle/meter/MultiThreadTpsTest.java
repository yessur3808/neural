package cn.ms.neural.throttle.meter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.ms.neural.throttle.MeterListenerIpml;
import cn.ms.neural.throttle.limter.CloudTicker;
import cn.ms.neural.throttle.limter.RealTimeLimiter;
import cn.ms.neural.throttle.support.IntervalModel;

public class MultiThreadTpsTest {
    final static int threadNums = 10;
    final static ExecutorService executorService = Executors.newFixedThreadPool(threadNums);
    final static RealTimeLimiter limiter = new RealTimeLimiter(100);
    // final static CloudMeter cloudMeter = CloudFactory.createCloudMeter();

    public static void main(String[] args) {
        for (int i = 0; i < threadNums; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    CloudMeter cloudMeter = CloudMeter.getSingleInstance();
                    /*cloudMeter.setIntervalModel(IntervalModel.ALL);
                    cloudMeter.setAcquireMeterTopic("topicTag", "producer");*/
                    acquireAll1(cloudMeter);
                    while (true) {
                        //                        limiter.acquire();
                        CloudTicker.sleepMillis(100);
                        cloudMeter.registerListener(new MeterListenerIpml());
                        cloudMeter.request("topicTag", "producer");
                        cloudMeter.request("topicTag", "consumer");
                    }
                }
            });
        }
    }

    /**
     * 设置订阅所有的统计信息
     */
    public static void acquireAll(CloudMeter cloudMeter) {
        cloudMeter.setIntervalModel(IntervalModel.ALL);
    }

    /**
     * 设置订阅所有的统计信息
     */
    public static void acquireAll1(CloudMeter cloudMeter) {
        cloudMeter.setAcquireMeterTopic("*");
    }

    /**
     * 设置订阅所有Topic的tag==topicTag的统计信息
     */
    public static void acquire1(CloudMeter cloudMeter) {
        cloudMeter.setAcquireMeterTopic("topicTag");
    }

    /**
     * 设置订阅所有Topic的tag==topicTag，key==producer的统计信息
     */
    public static void acquire2(CloudMeter cloudMeter) {
        cloudMeter.setAcquireMeterTopic("topicTag", "producer");
    }
    
}
