/*
 * Apache Software License 2.0, (c) Copyright 2013, Evan Summers
 * 
 */
package dualcontrol;

/**
 *
 * @author evans
 */
public class SystemProperties {

    public static String getString(String propertyName) {
        String propertyValue = System.getProperty("alias");
        if (propertyValue == null) {
            throw new RuntimeException("Missing -D property: " + propertyName);
        }
        return propertyValue;
    } 
    
    public static int getInt(String propertyName) {
        String propertyString = System.getProperty("alias");
        if (propertyString == null) {
            throw new RuntimeException("Missing -D property: " + propertyName);
        }
        return Integer.parseInt(propertyString);
    }
    
    public static int getInt(String propertyName, int defaultValue) {
        return Integer.getInteger(propertyName, defaultValue);
    }    
    
}
