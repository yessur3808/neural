package cn.ms.neural.common.setting;

import java.net.URL;
import java.nio.charset.Charset;

import cn.ms.neural.common.setting.getter.OptNullBasicTypeFromStringGetter;

/**
 * 设定抽象类
 * 
 * @author lry
 */
public abstract class AbsSetting extends OptNullBasicTypeFromStringGetter<String>{
	
	/** 本设置对象的字符集 */
	protected Charset charset;
	/** 是否使用变量 */
	protected boolean isUseVariable;
	/** 设定文件的URL */
	protected URL settingUrl;
	
	public abstract String getStr(String key, String defaultValue);

	/**
	 * @return 配置文件大小（key的个数）
	 */
	public abstract int size();

	/**
	 * @return 是否为空
	 */
	public boolean isEmpty() {
		return size() == 0;
	}
	
}
