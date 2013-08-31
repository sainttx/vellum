/*
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
 * 
 */
package mobi.exception;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author evan.summers
 */
public class MobiResource {
    Locale locale;
    ResourceBundle bundle;

    public MobiResource(Class type, Locale locale) {
        this.locale = locale;
        bundle = ResourceBundle.getBundle(type.getSimpleName(), locale);
    }
        
    public String get(String key) {
        return bundle.getString(key);
    }
    
}
