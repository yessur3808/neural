package cn.ms.neural.throttle;

import java.util.List;

import cn.ms.neural.throttle.meter.Meterinfo;
import cn.ms.neural.throttle.support.AcquireStatus;
import cn.ms.neural.throttle.support.MeterListener;

public class MeterListenerIpml implements MeterListener {
    @Override
    public AcquireStatus acquireStats(List<Meterinfo> meterinfos) {
        for (Meterinfo info : meterinfos) {
            System.out.println(info);
        }
        return AcquireStatus.ACQUIRE_SUCCESS;
    }
}
