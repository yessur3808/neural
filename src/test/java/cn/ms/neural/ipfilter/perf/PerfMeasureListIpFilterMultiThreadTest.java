package cn.ms.neural.ipfilter.perf;

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

@Ignore
public class PerfMeasureListIpFilterMultiThreadTest {

    @Test
    public void runPerformanceMeasurement() throws IOException {
        ListIpFilter filter = IpListUtil.createListIpFilterUsingIpList();
        MeasurementData data = new IpFilterPerfExecutor(filter).executeInMultithread(20);
        data.printReport();
    }

}
