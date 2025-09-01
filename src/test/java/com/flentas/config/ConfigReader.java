package com.flentas.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
	private static Properties prop;

	public static void loadConfig() {
		if (prop == null) {
			prop = new Properties();
			try {
				// Load from resources
				String path = System.getProperty("user.dir") + "/resources/config.properties";
				FileInputStream input = new FileInputStream(path);

				prop.load(input);
			} catch (IOException e) {
				throw new RuntimeException("Failed to load config.properties file!", e);
			}
		}
	}

	public static String getProperty(String key) {
		if (prop == null) {
			loadConfig();
		}
		return prop.getProperty(key);
	}
}
