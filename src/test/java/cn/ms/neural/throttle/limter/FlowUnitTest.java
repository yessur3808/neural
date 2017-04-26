package cn.ms.neural.throttle.limter;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import cn.ms.neural.throttle.support.FlowUnit;

public class FlowUnitTest {

    @Test
    public void unitTest() {

        System.out.println(FlowUnit.BYTE);

        System.out.println(TimeUnit.NANOSECONDS);

        System.out.println(FlowUnit.PB.toByte(1));

        System.out.println(FlowUnit.TB.toByte(1));
        System.out.println("测试TPS流量统计能够支持最大流量T为：" + Long.MAX_VALUE/FlowUnit.TB.toByte(1));
    }
}