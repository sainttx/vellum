/*
 * Apache Software License 2.0
 * Supported by iPay (Pty) Ltd, BizSwitch.net   
 */
package vellum.util;

import vellum.format.ArgFormats;

/**
 * Utility methods related to classes.
 * Utility methods for specific types eg {@code String}, are found in {@code Strings}.
 *
 * @author evan
 */
public class Args {

    /**
     * Used in toString() methods.
     * 
     */
    public static String format(Object ... args) {
        return ArgFormats.formatter.formatArray(args);
    }
    
    /**
     * Determine if any of the args equal the given object. 
     */
    public static boolean equals(Object object, Object... args) {
        for (Object arg : args) {
            if (arg != null && arg.equals(object)) {
                return true;
            }

        }
        return false;
    }

    /**
     * Determine if any of the args equal the given object. 
     */
    public static boolean equalsIdentity(Object object, Object... args) {
        for (Object arg : args) {
            if (arg != null && arg == object) {
                return true;
            }
        }
        return false;
    }

    public static Object coalesce(Object ... args) {
        for (Object arg : args) {
            if (arg != null) {
                return arg;
            }
        }
        return null;
    }

}
