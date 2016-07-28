package cn.ms.neural.common.setting.dialect;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.ms.neural.common.logger.ILogger;
import cn.ms.neural.common.logger.LoggerManager;
import cn.ms.neural.common.setting.AbsSetting;
import cn.ms.neural.common.setting.getter.Conver;
import cn.ms.neural.common.utils.StringUtils;

/**
 * 分组设置工具类。 用于支持设置文件<br>
 *  1、支持变量，默认变量命名为 ${变量名}，变量只能识别读入行的变量，例如第6行的变量在第三行无法读取
 *  2、支持分组，分组为中括号括起来的内容，中括号以下的行都为此分组的内容，无分组相当于空字符分组<br>
 *  		若某个key是name，加上分组后的键相当于group.name
 *  3、注释以#开头，但是空行和不带“=”的行也会被跳过，但是建议加#
 *  4、store方法不会保存注释内容，慎重使用
 * @author lry
 * 
 */
public class BasicSetting extends AbsSetting{
	private final static ILogger log = LoggerManager.getSysBootLog();
	
	final Map<String, String> map = new ConcurrentHashMap<String, String>();
	
	/** 默认字符集 */
	public final static String DEFAULT_CHARSET = "utf8";
	/** 数组类型值默认分隔符 */
	public final static String DEFAULT_DELIMITER= ",";

	/** 注释符号（当有此符号在行首，表示此行为注释） */
	private final static String COMMENT_FLAG_PRE = "#";
	/** 赋值分隔符（用于分隔键值对） */
	private final static String ASSIGN_FLAG = "=";
	/** 分组行识别的环绕标记 */
	private final static char[] GROUP_SURROUND = { '[', ']' };
	/** 变量名称的正则 */
	private String reg_var = "\\$\\{(.*?)\\}";
	
	/** 本设置对象的字符集 */
	private Charset charset;
	/** 是否使用变量 */
	private boolean isUseVariable;
	/** 设定文件的URL */
	private URL settingUrl;
	
	private LinkedList<String> groups = new LinkedList<String>();
	
	/**
	 * 基本构造<br/>
	 * 需自定义初始化配置文件<br/>
	 * 
	 * @param charset 字符集
	 * @param isUseVariable 是否使用变量
	 */
	public BasicSetting(Charset charset, boolean isUseVariable) {
		this.charset = charset;
		this.isUseVariable = isUseVariable;
	}

	/**
	 * 构造，使用相对于Class文件根目录的相对路径
	 * 
	 * @param pathBaseClassLoader 相对路径（相对于当前项目的classes路径）
	 * @param charset 字符集
	 * @param isUseVariable 是否使用变量
	 */
	public BasicSetting(String pathBaseClassLoader, String charset, boolean isUseVariable) {
		final URL url = this.getClass().getClassLoader().getResource(pathBaseClassLoader);
		if(url == null) {
			throw new RuntimeException(String.format("Can not find Setting file: [%s]", pathBaseClassLoader));
		}
		this.init(url, charset, isUseVariable);
	}

	/**
	 * 构造
	 * 
	 * @param configFile 配置文件对象
	 * @param charset 字符集
	 * @param isUseVariable 是否使用变量
	 */
	public BasicSetting(File configFile, String charset, boolean isUseVariable) {
		if (configFile == null) {
			throw new RuntimeException("Null Setting file!");
		}
		URL url = null;
		try {
			url = configFile.toURI().toURL();
			this.init(url, charset, isUseVariable);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 构造，相对于classes读取文件
	 * 
	 * @param path 相对路径
	 * @param clazz 基准类
	 * @param charset 字符集
	 * @param isUseVariable 是否使用变量
	 */
	public BasicSetting(String path, Class<?> clazz, String charset, boolean isUseVariable) {
		final URL url = clazz.getResource(path);
		if(url == null) {
			throw new RuntimeException(String.format("Can not find Setting file: [{}]", path));
		}
		this.init(url, charset, isUseVariable);
	}

	/**
	 * 构造
	 * 
	 * @param url 设定文件的URL
	 * @param charset 字符集
	 * @param isUseVariable 是否使用变量
	 */
	public BasicSetting(URL url, String charset, boolean isUseVariable) {
		if(url == null) {
			throw new RuntimeException("Null url define!");
		}
		this.init(url, charset, isUseVariable);
	}
	
	/**
	 * 构造
	 * @param pathBaseClassLoader 相对路径（相对于当前项目的classes路径）
	 */
	public BasicSetting(String pathBaseClassLoader) {
		this(pathBaseClassLoader, DEFAULT_CHARSET, false);
	}
	
	/*--------------------------公有方法 start-------------------------------*/
	/**
	 * 初始化设定文件
	 * 
	 * @param settingUrl 设定文件的URL
	 * @param charset 字符集
	 * @param isUseVariable 是否使用变量
	 * @return 成功初始化与否
	 */
	public boolean init(URL settingUrl, String charset, boolean isUseVariable) {
		if (settingUrl == null) {
			throw new RuntimeException("Null setting url or charset define!");
		}
		try {
			this.charset = Charset.forName(charset);
		} catch (Exception e) {
			log.warn("User custom charset [{}] parse error, use default charset: [{}]", charset, DEFAULT_CHARSET);
			this.charset = Charset.forName(DEFAULT_CHARSET);
		}
		this.isUseVariable = isUseVariable;
		this.settingUrl = settingUrl;

		return this.load(settingUrl);
	}

	/**
	 * 加载设置文件
	 * 
	 * @param settingUrl 配置文件URL
	 * @return 加载是否成功
	 */
	synchronized public boolean load(URL settingUrl) {
		if (settingUrl == null) {
			throw new RuntimeException("Null setting url define!");
		}
		log.debug("Load setting file [{}]", settingUrl.getPath());
		InputStream settingStream = null;
		try {
			settingStream = settingUrl.openStream();
			load(settingStream, isUseVariable);
		} catch (IOException e) {
			log.error("Load setting error!", e);
			return false;
		} finally {
			if(settingStream!=null){
				try {
					settingStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	/**
	 * 重新加载配置文件
	 */
	public void reload() {
		this.load(settingUrl);
	}

	/**
	 * 加载设置文件。 此方法不会关闭流对象
	 * 
	 * @param settingStream 文件流
	 * @param isUseVariable 是否使用变量（替换配置文件值中含有的变量）
	 * @return 加载成功与否
	 * @throws IOException
	 */
	public boolean load(InputStream settingStream, boolean isUseVariable) throws IOException {
		map.clear();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(settingStream, charset));
			// 分组
			String group = null;
			
			while (true) {
				String line = reader.readLine();
				if (line == null) {
					break;
				}
				line = line.trim();
				// 跳过注释行和空行
				if (StringUtils.isBlank(line) || line.startsWith(COMMENT_FLAG_PRE)) {
					continue;
				}
				
				// 记录分组名
				if (line.charAt(0) == GROUP_SURROUND[0] && line.charAt(line.length() - 1) == GROUP_SURROUND[1]) {
					group = line.substring(1, line.length() - 1).trim();
					this.groups.add(group);
					continue;
				}
				
				String[] keyValue = line.split(ASSIGN_FLAG, 2);
				// 跳过不符合简直规范的行
				if (keyValue.length < 2) {
					continue;
				}
				
				String key = keyValue[0].trim();
				if (false == StringUtils.isBlank(group)) {
					key = group + "." + key;
				}
				String value = keyValue[1].trim();
				
				// 替换值中的所有变量变量（变量必须是此行之前定义的变量，否则无法找到）
				if (isUseVariable) {
					value = replaceVar(value);
				}
				map.put(key, value);
			}
		} finally {
			if(reader!=null){
				reader.close();
			}
		}
		return true;
	}

	/**
	 * 设置变量的正则<br/>
	 * 正则只能有一个group表示变量本身，剩余为字符 例如 \$\{(name)\}表示${name}变量名为name的一个变量表示
	 * 
	 * @param regex 正则
	 */
	public void setVarRegex(String regex) {
		this.reg_var = regex;
	}
	
	/**
	 * @return 获得设定文件的路径
	 */
	public String getSettingPath() {
		return settingUrl.getPath();
	}

	@Override
	public int size() {
		return map.size();
	}
	
	@Override
	public String getStr(String key, String defaultValue) {
		final String value = map.get(key);
		if(StringUtils.isBlank(value)) {
			return defaultValue;
		}
		return value;
	}

	/**
	 * 获得指定分组的键对应值
	 * 
	 * @param key 键
	 * @param group 分组
	 * @return 值
	 */
	public String getByGroup(String key, String group) {
		return getStr(keyWithGroup(key, group));
	}
	
	/**
	 * 获得所有键值对
	 * @return map
	 */
	public Map<String, String> getMap(){
		return this.map;
	}
	
	/**
	 * 获得指定分组的所有键值对
	 * @param group 分组
	 * @return map
	 */
	public Map<String, String> getMap(String group){
		Map<String, String> map2 = new HashMap<String, String>();
		for (String key : map.keySet()) {
			if(StringUtils.isNotBlank(key) && key.startsWith(group)){
				map2.put(key, map.get(key));
			}
		}
		return map2;
	}
	
	//--------------------------------------------------------------- Set
	/**
	 * 设置值，无给定键创建之。设置后未持久化
	 * 
	 * @param key 键
	 * @param value 值
	 */
	public void setSetting(String key, Object value) {
		map.put(key, value.toString());
	}

	//--------------------------------------------------------------------------------- Functions
	/**
	 * 持久化当前设置，会覆盖掉之前的设置<br>
	 * 持久化会不会保留之前的分组
	 * @param absolutePath 设置文件的绝对路径
	 */
	public void store(String absolutePath) {
		Writer writer = null;
		try {
			File file=new File(absolutePath);
			if (false == file.exists()) {
				file.createNewFile();
			}
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), charset));
			Set<Entry<String, String>> entrySet = map.entrySet();
			for (Entry<String, String> entry : entrySet) {
				writer.write(entry.getKey() + ASSIGN_FLAG + entry.getValue());
			}
			writer.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(String.format("Can not find file [{}]!", absolutePath), e);
		} catch (IOException e) {
			throw new RuntimeException("Store Setting error!", e);
		}finally{
			if(writer!=null){
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 存储当前设置，会覆盖掉以前的设置
	 * 
	 * @param path 相对路径
	 * @param clazz 相对的类
	 */
	public void store(String path, Class<?> clazz) {
		this.store(Props.getAbsolutePath(path, clazz));
	}

	/**
	 * 将setting中的键值关系映射到对象中，原理是调用对象对应的set方法<br/>
	 * 只支持基本类型的转换
	 * 
	 * @param bean Bean
	 * @return Bean
	 */
	public Object toBean(final String group, Object bean) {
		return fill(bean, new ValueProvider(){
			public Object value(String name) {
				final String value = getByGroup(name, group);
				if(null != value){
					log.debug("Parse setting to object field [{}={}]", name, value);
				}
				return value;
			}
		});
	}

	/**
	 * 将setting中的键值关系映射到对象中，原理是调用对象对应的set方法<br/>
	 * 只支持基本类型的转换
	 * 
	 * @param bean Bean
	 * @return Bean
	 */
	public Object toBean(Object bean) {
		return toBean(null, bean);
	}
	
	/**
	 * 转换为Properties对象，原分组变为前缀
	 * @return Properties对象
	 */
	public Properties toProperties(){
		Properties properties = new Properties();
		properties.putAll(map);
		return properties;
	}
	
	/**
	 * @return 获得所有分组名
	 */
	public LinkedList<String> getGroups() {
		return this.groups;
	}
	
	public Set<Entry<String, String>> entrySet(){
		return map.entrySet();
	}
	
	@Override
	public String toString() {
		return map.toString();
	}

	/*--------------------------Private Method start-------------------------------*/
	/**
	 * 替换给定值中的变量标识
	 * @param value 值
	 * @return 替换后的字符串
	 */
	private String replaceVar(String value) {
		// 找到所有变量标识
		Pattern pattern = Pattern.compile(reg_var, Pattern.DOTALL);
		if(null == pattern || null == value){
			return null;
		}
		Set<String> vars =new HashSet<String>();
		Matcher matcher = pattern.matcher(value);
		while(matcher.find()){
			vars.add(matcher.group(0));
		}
		for (String var : vars) {
			if(null == var || null == pattern){
				return null;
			}
			String varValue="";
			Matcher matchertemp = pattern.matcher(var);
			if (matchertemp.find()) {
				varValue=matchertemp.group(1);
			}
			if (null != varValue) {
				value = value.replace(var, varValue);
			}
		}
		return value;
	}
	
	/**
	 * 组合Key和Group，组合后为group.key
	 * @param key
	 * @param group
	 * @return 组合后的KEY
	 */
	private static  String keyWithGroup(String key, String group){
		String keyWithGroup = key;
		if (!StringUtils.isBlank(group)) {
			keyWithGroup = group + "." + keyWithGroup;
		}
		return keyWithGroup;
	}
	
	/*--------------------------Private Method end-------------------------------*/
	
	/**
	 * 填充Bean
	 * 
	 * @param <T>
	 * @param bean Bean
	 * @param valueProvider 值提供者
	 * @return Bean
	 */
	public static <T> T fill(T bean, ValueProvider valueProvider) {
		if (null == valueProvider) {
			return bean;
		}

		Class<?> beanClass = bean.getClass();
		try {
			PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(beanClass).getPropertyDescriptors();
			String propertyName;
			Object value;
			for (PropertyDescriptor property : propertyDescriptors) {
				propertyName = property.getName();
				value = valueProvider.value(propertyName);
				if (null == value) {
					// 此处取得的值为空时跳过，包括null和""
					continue;
				}

				try {
					property.getWriteMethod().invoke(bean, Conver.parse(property.getPropertyType(), value));
				} catch (Exception e) {
					throw new RuntimeException(String.format("Inject [%s] error!", property.getName()), e);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return bean;
	}

	/**
	 * 值提供者，用于提供Bean注入时参数对应值得抽象接口
	 * 
	 * @author Looly
	 *
	 */
	public static interface ValueProvider {
		/**
		 * 获取值
		 * 
		 * @param name Bean对象中参数名
		 * @return 对应参数名的值
		 */
		public Object value(String name);
	}
}
