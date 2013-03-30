/*
 */
package vellum.util;

/**
 *
 * @author evans
 */
public class SystemProperties {

    public static String getString(String name) {
        String string = System.getProperty(name);
        if (string == null) {
            throw new RuntimeException(name);
        }
        return string;
    }

    public static String getString(String name, String defaultValue) {
        String string = System.getProperty(name, defaultValue);
        return string;
    }
    
    public static char[] getChars(String name, char[] defaultValue) {
        String string = System.getProperty(name);
        if (string == null) {
            return defaultValue;
        }
        return string.toCharArray();
    }
    

}
