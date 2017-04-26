package cn.ms.neural.throttle.support;

import java.util.List;

import cn.ms.neural.throttle.meter.Meterinfo;

/**
 * MeterListener 用于异步接收Meter统计数据.
 *
 * @author lry
 */
public interface MeterListener {

    /**
     * 推送统计信息
     * 注意：如果订阅了分钟模型的推送，则需要再2分钟之后才能打印出第一分钟的统计数值
     */
    AcquireStatus acquireStats(List<Meterinfo> meterinfos);
    
}
