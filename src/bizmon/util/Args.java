/*
 * Apache Software License 2.0
 * Supported by BizSwitch.net   
 */
package bizmon.util;

/**
 * Utility methods related to classes.
 * Utility methods for specific types eg {@code String}, are found in {@code Strings}.
 *
 * @author evan
 */
public class Args {

    /**
     * Convenience method used in {@code toString()} methods of objects
     * to format their properties.
     *
     */
    public static String format(Object... args) {
        return Lists.format(args);
    }

    /**
     * Convenience method used in {@code toString()} methods of objects
     * to format their properties.
     *
     */
    public static String formatPrint(Object... args) {
        return Lists.formatPrint(", ", args);
    }

    /**
     * Convenience method used in {@code toString()} methods of objects
     * to format their properties.
     *
     */
    public static String formatDisplay(Object... args) {
        return Lists.formatDisplay(", ", args);
    }

    /**
     * Convenience method used in {@code toString()} methods of objects
     * to format their properties.
     *
     */
    public static String formatDisplaySpaced(Object... args) {
        return Lists.formatDisplay(" ", args);
    }

    /**
     * Convenience method used in {@code toString()} methods of objects
     * to format their properties.
     *
     */
    public static String formatDisplayDashed(Object... args) {
        return Lists.formatDisplay("-", args);
    }

    /**
     * Convenience method used in {@code toString()} methods of objects
     * to format their properties.
     *
     */
    public static String formatExport(String delimiter, Object... args) {
        return Lists.formatExport(delimiter, args);
    }

    /**
     * Format the list of args to a String.
     *
     */
    public static String formatVerbose(Object... args) {
        return Lists.formatVerbose(args);
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
