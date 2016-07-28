package cn.ms.neural.common.spi.factory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.ms.neural.common.spi.ExtensionFactory;
import cn.ms.neural.common.spi.ExtensionLoader;

/**
 * AdaptiveExtensionFactory
 * 
 * @author lry
 */
//@Adaptive
public class AdaptiveExtensionFactory implements ExtensionFactory {
    
    private final List<ExtensionFactory> factories;
    
    public AdaptiveExtensionFactory() {
        ExtensionLoader<ExtensionFactory> loader = ExtensionLoader.getExtensionLoader(ExtensionFactory.class);
        List<ExtensionFactory> list = new ArrayList<ExtensionFactory>();
        for (String name : loader.getSupportedExtensions()) {
            list.add(loader.getAdaptiveExtension(name));
        }
        factories = Collections.unmodifiableList(list);
    }

    public <T> T getExtension(Class<T> type, String name) {
        for (ExtensionFactory factory : factories) {
            T extension = factory.getExtension(type, name);
            if (extension != null) {
                return extension;
            }
        }
        return null;
    }

}
