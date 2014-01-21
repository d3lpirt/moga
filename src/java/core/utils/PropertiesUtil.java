package core.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * User: Dmitry Beshkarev
 * Date: 28/11/13 Time: 20:30
 */
public class PropertiesUtil {

    private Properties properties;

    public PropertiesUtil(String mogaName) throws IOException {
        properties = loadProperties(mogaName);
    }

    private Properties loadProperties(String mogaName) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream("config/" + mogaName + "/" + mogaName + ".properties"));
        return properties;
    }

    public int getIntProperty(String propertyName) {
        return Integer.valueOf(getProperties().getProperty(propertyName));
    }

    public double getDoubleProperty(String propertyName) {
        return Double.valueOf(getProperties().getProperty(propertyName));
    }
    
    public String getProperty(String propertyName) {
        return getProperties().getProperty(propertyName);
    }

    public Properties getProperties() {
        return properties;
    }
}
