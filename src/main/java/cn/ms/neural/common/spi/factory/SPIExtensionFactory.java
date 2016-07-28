package cn.ms.neural.common.spi.factory;

import cn.ms.neural.common.spi.ExtensionFactory;
import cn.ms.neural.common.spi.ExtensionLoader;
import cn.ms.neural.common.spi.SPI;

/**
 * SpiExtensionFactory
 * 
 * @author lry
 */
public class SPIExtensionFactory implements ExtensionFactory {

    public <T> T getExtension(Class<T> type, String name) {
        if (type.isInterface() && type.isAnnotationPresent(SPI.class)) {
            ExtensionLoader<T> loader = ExtensionLoader.getExtensionLoader(type);
            if (loader.getSupportedExtensions().size() > 0) {
                return loader.getAdaptiveExtension();
            }
        }
        return null;
    }

}
