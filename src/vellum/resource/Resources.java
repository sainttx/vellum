/*
 * Apache Software License 2.0, Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2012, Evan Summers
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