package cn.ms.neural.common.spi;

/**
 * ExtensionFactory
 * 
 * @author lry
 */
@SPI
public interface ExtensionFactory {

    /**
     * Get extension.
     * 
     * @param type object type.
     * @param name object name.
     * @return object instance.
     */
    <T> T getExtension(Class<T> type, String name);

}
