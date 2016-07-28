package cn.ms.neural.common.spi;

import org.junit.Assert;
import org.junit.Test;

import cn.ms.neural.common.spi.impl.ExtServiceImpl1;
import cn.ms.neural.common.spi.impl.ExtServiceImpl2;

public class SPITest {

	@Test
	public void testDefaultAdaptiveExtension() {
		ExtensionLoader<ExtService> extensionLoader=ExtensionLoader.getExtensionLoader(ExtService.class);
		ExtService extService=extensionLoader.getAdaptiveExtension();
		Assert.assertTrue(extService instanceof ExtServiceImpl1);
	}
	
	@Test
	public void testImplAdaptiveExtension() {
		ExtService extService1=ExtensionLoader.getExtensionLoader(ExtService.class, "default");
		Assert.assertTrue(extService1 instanceof ExtServiceImpl1);
		
		ExtService extService2=ExtensionLoader.getExtensionLoader(ExtService.class, "impl2");
		Assert.assertTrue(extService2 instanceof ExtServiceImpl2);
	}
	
	
}
