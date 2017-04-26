package cn.ms.neural.throttle.meter;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import cn.ms.neural.throttle.limter.CloudTicker;
import cn.ms.neural.throttle.support.AcquireStatus;
import cn.ms.neural.throttle.support.IntervalModel;
import cn.ms.neural.throttle.support.MeterListener;

public class CloudFactoryTest {
//    @Test
    public void createCloudMeter() throws Exception {
        CloudMeter cloudMeter = CloudMeter.getSingleInstance();
        cloudMeter.setIntervalModel(IntervalModel.ALL);
        cloudMeter.registerListener(new MeterListener() {
            @Override
            public AcquireStatus acquireStats(List<Meterinfo> meterinfos) {
                if (meterinfos.size() > 0) {
                    System.out.println(meterinfos);
                }
                return AcquireStatus.ACQUIRE_SUCCESS;
            }
        });

        for (int i = 0; i < 10000; i++) {
            CloudTicker.sleepMicros(1);
            cloudMeter.request();
        }
    }

//    @Test
    public void createCloudMeterAcquireLater() throws Exception {
        CloudMeter cloudMeter = CloudMeter.getSingleInstance();
        cloudMeter.setIntervalModel(IntervalModel.ALL);
        cloudMeter.registerListener(new MeterListener() {
            final AtomicInteger receiveNum = new AtomicInteger(0);

            @Override
            public AcquireStatus acquireStats(List<Meterinfo> meterinfos) {
                receiveNum.addAndGet(meterinfos.size());
                if (meterinfos.size() > 0) {
                    // 模拟第10-20条数据重复消费
                    if (receiveNum.get() > 10 && receiveNum.get() < 20) {
                        for (Meterinfo info : meterinfos) {
                            System.out.println("REACQUIRE_LATER" + info);
                        }
                        return AcquireStatus.REACQUIRE_LATER;
                    }
                    for (Meterinfo info : meterinfos) {
                        System.out.println(info);
                    }
                }

                return AcquireStatus.ACQUIRE_SUCCESS;
            }
        });
        for (int i = 0; i < 10000; i++) {
            CloudTicker.sleepSeconds(1);
            cloudMeter.request();
        }
    }
}