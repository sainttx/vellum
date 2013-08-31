/*
 * Contributed (2013) by Evan Summers via https://code.google.com/p/vellum
 * 
 */
package vellum.resource;

import java.util.Locale;
import java.util.ResourceBundle;
import vellum.exception.Exceptions;

public class Resources {

    public static ResourceBundle getBundle(Class type) {
        return new LocaleResourceBundle(Locale.getDefault()).getBundle(type);
    }
    
    public static String getString(Class type, String key) {
        try {
            ResourceBundle bundle = getBundle(type);
            return bundle.getString(key);
        } catch (Exception e) {
            Exceptions.warn(e);
            return type.getSimpleName() + "." + key;
        }
    }
}