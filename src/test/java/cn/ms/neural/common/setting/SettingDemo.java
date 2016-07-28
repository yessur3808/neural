package cn.ms.neural.common.setting;

import java.io.IOException;

/**
 * Setting演示样例类
 */
public class SettingDemo {
	
	public static void main(String[] args) throws IOException {
		Setting setting = new Setting("config/db.setting");
		System.err.println(setting.getMap());
		System.err.println(setting.get("user", "test"));
	}
}
