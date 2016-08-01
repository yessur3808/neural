package cn.ms.neural.common.spi;

public class Extension<S> {

	public static <S> S getExtensionLoader(Class<S> service, String name) {
		return ExtensionLoader.getExtensionLoader(service, name);
	}
	
	public static <S> ExtensionLoader<S> load(Class<S> service, ClassLoader loader) {
		return ExtensionLoader.load(service, loader);
	}

	public static <S> ExtensionLoader<S> getExtensionLoader(Class<S> service) {
		return ExtensionLoader.getExtensionLoader(service);
	}
	
}
