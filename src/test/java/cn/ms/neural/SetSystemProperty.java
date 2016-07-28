package cn.ms.neural;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author
 * @version
 */
public class SetSystemProperty {
	
	// 属性文件的路径
	static String profilepath = "druid.properties";
	private static Properties props = new Properties();
	static {
		try {
			profilepath=Thread.currentThread().getContextClassLoader().getResource("").getPath()+profilepath;
			props.load(new FileInputStream(profilepath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			System.exit(-1);
		}
	}

	/**
	 * 更新（或插入）一对properties信息(主键及其键值) 如果该主键已经存在，更新该主键的值； 如果该主键不存在，则插件一对键值。
	 * 
	 * @param keyname 键名
	 * @param keyvalue 键值
	 */
	public static void writeProperties(String keyname, String keyvalue) {
		try {
			OutputStream fos = new FileOutputStream(profilepath);
			props.setProperty(keyname, keyvalue);
			props.store(fos, "Update '" + keyname + "' value '"+keyvalue+"'");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新properties文件的键值对 如果该主键已经存在，更新该主键的值； 如果该主键不存在，则插件一对键值。
	 * 
	 * @param keyname
	 *            键名
	 * @param keyvalue
	 *            键值
	 */
	public void updateProperties(String keyname, String keyvalue) {
		try {
			props.load(new FileInputStream(profilepath));
			OutputStream fos = new FileOutputStream(profilepath);
			props.setProperty(keyname, keyvalue);
			props.store(fos, "Update '" + keyname + "' value '"+keyvalue+"'");
		} catch (IOException e) {
			System.err.println("属性文件更新错误");
		}
	}

	// 测试代码
	public static void main(String[] args) throws Exception{
		Map<String, Object> data=new HashMap<String, Object>();
		for (Map.Entry<Object, Object> entry:props.entrySet()) {
			data.put(String.valueOf(entry.getKey()), entry.getValue());
		}
		String password=String.valueOf(data.get("password"));
		String[] passwordArray=password.split(",");
		if(passwordArray==null||passwordArray.length!=2){
			throw new RuntimeException("密码配置错误");
		}else{
			String finalPassword=passwordArray[1];
			if("EXP".equals(passwordArray[0])){//明文
				DesUtils desUtils=new DesUtils();
				String cipPassword=desUtils.encrypt(finalPassword);
				writeProperties("password", "CIP,"+cipPassword);
			}else if("CIP".equals(passwordArray[0])){//密文
				DesUtils desUtils=new DesUtils();
				System.out.println(passwordArray[1]);
				finalPassword=desUtils.decrypt(passwordArray[1]);
				System.out.println(finalPassword);
			}else{
				throw new RuntimeException("密码配置错误");
			}
			System.out.println(finalPassword);
		}
//        writeProperties("MAIL_SERVER_INCOMING", "327@qq.com");
		
	}
}