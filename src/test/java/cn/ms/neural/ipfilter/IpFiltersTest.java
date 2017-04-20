package cn.ms.neural.ipfilter;

import org.junit.Test;

import cn.ms.neural.ipfilter.CachedIpFilter;
import cn.ms.neural.ipfilter.ConfIpFilter;
import cn.ms.neural.ipfilter.IpFilter;
import cn.ms.neural.ipfilter.IpFilterConf;
import cn.ms.neural.ipfilter.IpFilters;
import static org.junit.Assert.assertTrue;

public class IpFiltersTest {
    @Test
    public void createIpFilterWithConfig() {
        IpFilter filter = IpFilters.create(createConfig());
        assertTrue(filter instanceof ConfIpFilter);
    }

    @Test
    public void createCachedIpFilterWithConfig() {
        IpFilter filter = IpFilters.createCached(createConfig());
        assertTrue(filter instanceof CachedIpFilter);
    }

    private IpFilterConf createConfig() {
        IpFilterConf ipFilterConf = new IpFilterConf();
        ipFilterConf.allow("1.2.3.4");
        return ipFilterConf;
    }
}
