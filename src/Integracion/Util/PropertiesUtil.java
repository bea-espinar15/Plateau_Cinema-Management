
package Integracion.Util;

import java.io.FileInputStream;
import java.util.Properties;


public class PropertiesUtil {
	
	public static Properties loadProperty(String propertiesURL){
		try{
			Properties properties = new Properties();
			properties.load(new FileInputStream(propertiesURL));
			return properties;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
}
