package simulator.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesManager {
	private static File file;
	private static Properties prop;
	private static FileInputStream in;
	private static FileOutputStream out;
	private static final String PATH = "/resources/config.properties";

	public static void load() {
		File jarPath = new File(PropertiesManager.class.getProtectionDomain()
				.getCodeSource().getLocation().getPath());
		File rootDirectory = jarPath.getParentFile();
		file = new File(rootDirectory + PATH);
		prop = new Properties();
	}

	public static String getProperty(String key) {
		try {
			in = new FileInputStream(file);
			prop.load(in);
			String value = prop.getProperty(key);
			in.close();
			return value;
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("no property found by the name of " + key);
		return "ayy lmao (null)";
	}

	public static void setProperty(String key, String value) {
		prop.setProperty(key, value);
		try {
			out = new FileOutputStream(file);
			prop.store(out, null);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
