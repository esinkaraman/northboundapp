package tr.edu.boun.cmpe.swe599.nothbound.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PropertyReader {

	private static final String FILENAME = "nb.properties";
	private static PropertyReader instance = new PropertyReader();

	public static final String CLOUDS = "clouds";
	public static final String POSTFIX_MAC = ".mac";
	public static final String POSTFIX_IP = ".ip";
	public static final String POSTFIX_SWITCHPORT = ".switchport";

	public static PropertyReader getInstance() {
		return instance;
	}

	public String getProperty(String key) {
		Properties prop = new Properties();
		try {
			prop.load(this.getClass().getClassLoader()
					.getResourceAsStream(FILENAME));
			return prop.getProperty(key);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.println(PropertyReader.getInstance().getProperty("clouds"));
	}

	public List<String> getClouds() {
		List<String> result = new ArrayList<String>();
		String property = getProperty(CLOUDS);
		if (property != null) {
			String[] clouds = property.split(",");
			for (String cloud : clouds) {
				result.add(cloud.trim());
			}
		}
		return result;
	}

	public String getCloudMac(String cloudname) {
		String property = getProperty(cloudname + POSTFIX_MAC);
		return property;
	}

	public String getCloudIp(String cloudname) {
		String property = getProperty(cloudname + POSTFIX_IP);
		return property;
	}

	public String getCloudSwitchPort(String cloudname) {
		String property = getProperty(cloudname + POSTFIX_SWITCHPORT);
		return property;
	}
	
	public String getControllerURL() {
		String property = getProperty("controller.url");
		return property;
	}
	
	public String getNatMac() {
		String property = getProperty("nat.mac");
		return property;
	}
	
}