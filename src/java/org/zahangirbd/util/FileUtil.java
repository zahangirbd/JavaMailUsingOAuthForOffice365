package org.zahangirbd.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FileUtil {

	/**
	 * This method is used to read the given resource file and return the data as String
	 * @param cls The class whose class loader will be used 
	 * @param resource The resource file name, e.g., 
	 * @return the data read from the resource as String
	 */
	public static String readDataFromResFile(Class<?> cls, String resource) {
		if (cls == null || resource == null) {
			String errMsg = "readDataFromResFile(...): Either given class or resource are NULL, thus, failed to read data from resource"; 
            System.err.println(errMsg);
            throw new RuntimeException(errMsg);
		}
		
        try {
            return new String(cls.getClassLoader().getResourceAsStream(resource).readAllBytes());
        } catch (Exception e) {
        	e.printStackTrace();
            System.err.println("readDataFromResFile(...): Error reading data from resource file ('" + resource + "'):" + e.getMessage());
            throw new RuntimeException(e);
        }
    }
	
	public static Properties readPropertiesFromResFile(Class<?> cls, String propResource) throws IOException {
        Properties prop = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = cls.getClassLoader().getResourceAsStream(propResource);
            if (inputStream != null) {
                prop.load(inputStream);
            } else {
            	String errMsg = "readPropertiesFromResFile(...): property file '" + propResource + "' not found in the classpath";
                throw new FileNotFoundException(errMsg);
            }
        } catch (Exception e) {
        	e.printStackTrace();
            System.err.println("readPropertiesFromResFile(...): Exception = " + e.getMessage());
        } finally {
            inputStream.close();
        }
        return prop;
    }
}
