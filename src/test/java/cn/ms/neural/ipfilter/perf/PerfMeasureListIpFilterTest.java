package cn.ms.neural.ipfilter.perf;

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

@Ignore
public class PerfMeasureListIpFilterTest {

    @Test
    public void runPerformanceMeasurement() throws IOException {
        ListIpFilter filter = IpListUtil.createListIpFilterUsingIpList();
        MeasurementData data = new IpFilterPerfExecutor(filter).execute();
        data.printReport();
    }

}
