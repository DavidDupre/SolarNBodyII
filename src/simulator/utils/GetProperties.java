package simulator.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GetProperties {
	public static Properties getProperties(String filePath) {
		final Properties prop = new Properties();
		InputStream propIS = null;
		File propFile = new File(filePath);
		try {
			propIS = new FileInputStream(propFile);
		} catch (FileNotFoundException e1) {
			System.out.println("config not found");
			e1.printStackTrace();
		}
		try {
			prop.load(propIS);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}
}
