package cn.ms.neural.common.setting.dialect;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import cn.ms.neural.common.setting.getter.BasicTypeGetter;
import cn.ms.neural.common.setting.getter.OptBasicTypeGetter;
import cn.ms.neural.common.utils.StringUtils;

/**
 * Properties文件读取封装类
 * 
 * @author lry
 */
public final class Props extends Properties implements BasicTypeGetter<String>, OptBasicTypeGetter<String>{
	
	private static final long serialVersionUID = 1935981579709590740L;
	
	//----------------------------------------------------------------------- 私有属性 start
	/** 属性文件的URL */
	private URL propertiesFileUrl;
	//----------------------------------------------------------------------- 私有属性 end
	
	//----------------------------------------------------------------------- 构造方法 start
	/**
	 * 获得Classpath下的Properties文件
	 * 
	 * @param resource 资源（相对Classpath的路径）
	 * @return Properties
	 */
	public static Properties getProp(String resource) {
		return new Props(resource);
	}
	
	/**
	 * 构造，使用相对于Class文件根目录的相对路径
	 * @param pathBaseClassLoader 相对路径（相对于当前项目的classes路径）
	 */
	public Props(String pathBaseClassLoader){
		if(null == pathBaseClassLoader) {
			pathBaseClassLoader = "";
		}
		
		final URL url = this.getClass().getResource(pathBaseClassLoader);
		if(url == null) {
			throw new RuntimeException(String.format("Can not find properties file: [%s]", pathBaseClassLoader));
		}
		this.load(url);
	}
	
	/**
	 * 构造
	 * @param propertiesFile 配置文件对象
	 */
	public Props(File propertiesFile){
		if (propertiesFile == null) {
			throw new RuntimeException("Null properties file!");
		}
		URL url = null;
		try {
			url = propertiesFile.toURI().toURL();
			if(url == null) {
				throw new RuntimeException(String.format("Can not find Setting file: [%s]", propertiesFile.getAbsolutePath()));
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		this.load(url);
	}
	
	/**
	 * 构造，相对于classes读取文件
	 * @param path 相对路径
	 * @param clazz 基准类
	 */
	public Props(String path, Class<?> clazz){
		final URL url = clazz.getResource(path);
		if(url == null) {
			throw new RuntimeException(String.format("Can not find Setting file: [%s]", path));
		}
		this.load(url);
	}
	
	/**
	 * 构造，使用URL读取
	 * @param propertiesUrl 属性文件路径
	 */
	public Props(URL propertiesUrl){
		this.load(propertiesUrl);
	}
	//----------------------------------------------------------------------- 构造方法 end
	
	/**
	 * 初始化配置文件
	 * @param propertiesFileUrl 配置文件URL
	 */
	public  void load(URL propertiesFileUrl){
		if(propertiesFileUrl == null){
			throw new RuntimeException("Null properties file url define!");
		}
		InputStream in = null;
		try {
			in = propertiesFileUrl.openStream();
			super.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		this.propertiesFileUrl = propertiesFileUrl;
	}
	
	/**
	 * 重新加载配置文件
	 */
	public void reload() {
		this.load(propertiesFileUrl);
	}
	
	//----------------------------------------------------------------------- Get start
	@Override
	public Object getObj(String key, Object defaultValue) {
		return getStr(key, null == defaultValue ? null : defaultValue.toString());
	}

	@Override
	public Object getObj(String key) {
		return getObj(key, null);
	}
	
	@Override
	public String getStr(String key, String defaultValue){
		return super.getProperty(key, defaultValue);
	}
	
	@Override
	public String getStr(String key){
		return super.getProperty(key);
	}
	
	@Override
	public Integer getInt(String key, Integer defaultValue){
		try {
			Integer rs=Integer.valueOf(key);
			return rs==null?defaultValue:rs;
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	@Override
	public Integer getInt(String key){
		return getInt(key, null);
	}
	
	@Override
	public Boolean getBool(String key, Boolean defaultValue){
		try {
			Boolean rs=Boolean.valueOf(key);
			return rs==null?defaultValue:rs;
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	@Override
	public Boolean getBool(String key){
		return getBool(key, null);
	}
	
	@Override
	public Long getLong(String key, Long defaultValue){
		try {
			Long rs=Long.valueOf(key);
			return rs==null?defaultValue:rs;
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	@Override
	public Long getLong(String key){
		return getLong(key, null);
	}
	
	@Override
	public Character getChar(String key, Character defaultValue) {
		final String value = getStr(key);
		if(StringUtils.isBlank(value)) {
			return defaultValue;
		}
		return value.charAt(0);
	}
	
	@Override
	public Character getChar(String key) {
		return getChar(key, null);
	}
	
	@Override
	public Float getFloat(String key) {
		return getFloat(key, null);
	}
	
	@Override
	public Float getFloat(String key, Float defaultValue) {
		try {
			Long rs=Long.valueOf(key);
			return rs==null?defaultValue:rs;
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	@Override
	public Double getDouble(String key, Double defaultValue) throws NumberFormatException {
		try {
			Long rs=Long.valueOf(key);
			return rs==null?defaultValue:rs;
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	@Override
	public Double getDouble(String key) throws NumberFormatException {
		return getDouble(key, null);
	}
	
	@Override
	public Short getShort(String key, Short defaultValue) {
		try {
			Short rs=Short.valueOf(key);
			return rs==null?defaultValue:rs;
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	@Override
	public Short getShort(String key) {
		return getShort(key, null);
	}

	@Override
	public Byte getByte(String key, Byte defaultValue) {
		try {
			Byte rs=Byte.valueOf(key);
			return rs==null?defaultValue:rs;
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	@Override
	public Byte getByte(String key) {
		return getByte(key, null);
	}

	@Override
	public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
		final String valueStr = getStr(key);
		if(StringUtils.isBlank(valueStr)) {
			return defaultValue;
		}
		
		try {
			return new BigDecimal(valueStr);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	@Override
	public BigDecimal getBigDecimal(String key) {
		return getBigDecimal(key, null);
	}

	@Override
	public BigInteger getBigInteger(String key, BigInteger defaultValue) {
		final String valueStr = getStr(key);
		if(StringUtils.isBlank(valueStr)) {
			return defaultValue;
		}
		
		try {
			return new BigInteger(valueStr);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	@Override
	public BigInteger getBigInteger(String key) {
		return getBigInteger(key, null);
	}
	
	//----------------------------------------------------------------------- Get end
	
	//----------------------------------------------------------------------- Set start
	/**
	 * 设置值，无给定键创建之。设置后未持久化
	 * @param key 属性键
	 * @param value 属性值
	 */
	public void setProperty(String key, Object value){
		super.setProperty(key, value.toString());
	}
	
	/**
	 * 持久化当前设置，会覆盖掉之前的设置
	 * @param absolutePath 设置文件的绝对路径
	 */
	public void store(String absolutePath){
		try {
			//FileUtil.touch(absolutePath);
			File file = new File(absolutePath);
			if (false == file.exists()) {
				final File parentFile = file.getParentFile();
				if (null != parentFile) {
					parentFile.mkdirs();
				}
				file.createNewFile();
			}
			
			super.store(new BufferedOutputStream(new FileOutputStream(file)), null);
		} catch (FileNotFoundException e) {
			//不会出现这个异常
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 存储当前设置，会覆盖掉以前的设置
	 * @param path 相对路径
	 * @param clazz 相对的类
	 */
	public void store(String path, Class<?> clazz){
		this.store(getAbsolutePath(path, clazz));
	}
	//----------------------------------------------------------------------- Set end
	
	/**
	 * 获取绝对路径<br/>
	 * 此方法不会判定给定路径是否有效（文件或目录存在）
	 * 
	 * @param path 相对路径
	 * @param baseClass 相对路径所相对的类
	 * @return 绝对路径
	 */
	public static String getAbsolutePath(String path, Class<?> baseClass) {
		if (path == null) {
			path = "";
		}
		if (baseClass == null) {
			return getAbsolutePath(path);
		}
		return removePrefix("file:", baseClass.getResource(path).getPath());
	}
	
	/**
	 * 去掉指定前缀
	 * 
	 * @param str 字符串
	 * @param prefix 前缀
	 * @return 切掉后的字符串，若前缀不是 preffix， 返回原字符串
	 */
	public static String removePrefix(String str, String prefix) {
		if(StringUtils.isBlank(str) || StringUtils.isBlank(prefix)){
			return str;
		}
		
		if (str.startsWith(prefix)) {
			return str.substring(prefix.length());
		}
		return str;
	}

	/**
	 * 获取绝对路径，相对于classes的根目录<br>
	 * 如果给定就是绝对路径，则返回原路径，原路径把所有\替换为/
	 * 
	 * @param path 相对路径
	 * @return 绝对路径
	 */
	public static String getAbsolutePath(String path) {
		if (path == null) {
			path = "";
		} else {
			path = path.replaceAll("[/\\\\]{1,}", "/");
			if (path.startsWith("/") || path.matches("^[a-zA-Z]:/.*")) {
				// 给定的路径已经是绝对路径了
				return path;
			}
		}
		// 相对路径
		ClassLoader classLoader = Props.class.getClassLoader();
		URL url = classLoader.getResource(path);
		return url != null ? url.getPath() : Props.class.getResource("").getPath() + path;
	}
}
