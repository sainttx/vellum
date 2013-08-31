/*
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
 * 
 */
package mobi.exception;

import java.util.Locale;
import java.util.ResourceBundle;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

public class Resources {
    static Logr logger = LogrFactory.getLogger(Resources.class);
    
    public static ResourceBundle getBundle(Class type) {
        return new LocaleResourceBundle(Locale.getDefault()).getBundle(type);
    }
    
    public static String getString(Class type, String key) {
        try {
            ResourceBundle bundle = getBundle(type);
            return bundle.getString(key);
        } catch (Exception e) {
            logger.warn(e);
            return type.getSimpleName() + "." + key;
        }
    }
}