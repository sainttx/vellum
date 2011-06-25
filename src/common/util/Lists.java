/*
 * Apache Software License 2.0
 * Supported by BizSwitch.net   
 */
package common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Utility methods related to classes.
 * Utility methods for specific types eg {@code String}, are found in {@code Strings}.
 *
 * @author evan
 */
public class Lists {

    public static final String DEFAULT_DELIMITER = ", ";
    public static final String COMMA_DELIMITER = ", ";
    public static final String SINGLE_QUOTE = "'";
    public static final String DOUBLE_QUOTE = "\"";

    /**
     * Compare items in two lists for equality.
     * 
     */
    public static boolean equals(List list, List other) {
        if (list == other) return true;
        if (list == null || other == null) return false;
        if (list.size() != other.size()) return false;
        for (int i = 0; i < list.size(); i++) {
            if (!Types.equals(list.get(i), other.get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compare items in two arrays for equality.
     *
     */
    public static boolean equals(Object[] array, Object[] other) {
        if (array == other) return true;
        if (array == null || other == null) return false;
        if (array.length != other.length) return false;
        for (int i = 0; i < array.length; i++) {
            if (!Types.equals(array[i], other[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Convenience method used in {@code toString()} methods of objects
     * to format their properties.
     *
     */
    public static String format(Object[] args) {
        if (args == null) {
            return "";
        }
        if (args.length > 1) {
            if (args[0] instanceof String) {
                String string = (String) args[0];
                if (string.contains("%")) {
                    return String.format(string, Arrays.copyOfRange(args, 1, args.length));
                }
            }
        }
        return formatPrint(", ", args);
    }

    /**
     * Convenience method used in {@code toString()} methods of objects
     * to format their properties.
     *
     */
    public static String formatPrint(String delimiter, Object[] args) {
        if (args == null) {
            return "{}";
        }
        StringBuilder builder = new StringBuilder();
        for (Object arg : args) {
            if (builder.length() > 0) {
                builder.append(delimiter);
            }
            builder.append(Types.formatPrint(arg));
        }
        return builder.toString();
    }

    /**
     * Convenience method used in {@code toString()} methods of objects
     * to format their properties.
     *
     */
    public static String formatDisplay(String delimiter, Object[] args) {
        if (args == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (Object arg : args) {
            if (arg != null) {
                if (builder.length() > 0) {
                    builder.append(delimiter);
                }
                builder.append(Types.formatPrint(arg));
            }
        }
        return builder.toString();
    }

    /**
     * Convenience method used in {@code toString()} methods of objects
     * to format their properties.
     *
     */
    public static String formatDisplay(String delimiter, String quote, Object[] args) {
        if (args == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (Object arg : args) {
            if (arg != null) {
                if (builder.length() > 0) {
                    builder.append(delimiter);
                }
                builder.append(quote);
                builder.append(Types.formatPrint(arg));
                builder.append(quote);
            }
        }
        return builder.toString();
    }

    public static String formatDisplayComma(Object[] args) {
        return formatDisplay(COMMA_DELIMITER, args);
    }

    public static String formatDisplayCommaSingleQuote(Object[] args) {
        return formatDisplay(COMMA_DELIMITER, SINGLE_QUOTE, args);
    }

    public static String formatDisplayCommaSingleQuote(Collection collection) {
        return formatDisplay(COMMA_DELIMITER, SINGLE_QUOTE, collection.toArray());
    }

    /**
     * Convenience method used in {@code toString()} methods of objects
     * to format their properties.
     *
     */
    public static String formatDisplay(Collection collection) {
        return formatDisplay(DEFAULT_DELIMITER, collection.toArray());
    }

    /**
     * Convenience method used in {@code toString()} methods of objects
     * to format their properties.
     *
     */
    public static String formatExport(String delimiter, Object[] args) {
        if (args == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        boolean delimit = false;
        for (Object arg : args) {
            if (!delimit) {
                delimit = true;
            } else {
                builder.append(delimiter);
            }
            if (arg != null) {
                builder.append(Types.formatDisplay(arg));
            }
        }
        return builder.toString();
    }

    /**
     * Format the list of values to a String.
     *
     */
    public static String formatVerbose(Object[] args) {
        StringBuilder builder = new StringBuilder();
        for (Object arg : args) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            if (arg == null) {
                builder.append("null");
            } else {
                String string = Types.formatPrint(arg);
                if (Strings.isEmpty(string)) {
                    string = "empty";
                }
                if (arg.getClass() != String.class && !arg.getClass().isPrimitive()) {
                    builder.append("(");
                    builder.append(arg.getClass().getSimpleName());
                    builder.append(") ");
                }
                builder.append(string);
            }
        }
        return builder.toString();
    }

    /**
     * Create and populate a new fixed length list.
     *
     */
    public static <T> List<T> newList(Class<T> type, int length) {
        List list = new ArrayList(length);
        for (int i = 0; i < length; i++) {
            list.add(Types.newInstance(type));
        }
        return list;
    }

    public static Map sortByValue(Map map) {
        Map result = new TreeMap(new MapValueComparator(map));
        result.putAll(map);
        return result;
    }

    public static List asList(String[] array) {
        List list = new ArrayList();
        for (String string : array) {
            list.add(string);
        }
        return list;
    }

    public static <T> T get(List<T> list, int index) {
        if (list != null && index < list.size()) {
            return (T) list.get(index);
        }
        return null;
    }

    public static boolean contains(Map map, Object... keys) {
        for (Object key : keys) {
            if (map.containsKey(key)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsAll(Map map, Object... keys) {
        for (Object key : keys) {
            if (!map.containsKey(key)) {
                return false;
            }
        }
        return true;
    }

    public static int compareTo(Object[] array, Object[] other) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null && other[i] == null) {
                continue;
            }
            if (array[i] == null) {
                return -1;
            }
            if (other[i] == null) {
                return 1;
            }
            int result = Types.compareTo(array[i], other[i]);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }

    public static boolean isEmpty(String[] array) {
        return array != null && array.length > 0 && array[0] != null && array[0].length() > 0;
    }

    public static String getLast(String[] pathParts) {
        if (pathParts.length > 0) {
            return pathParts[pathParts.length - 1];
        }
        throw new IllegalArgumentException();
    }

    public static Object[] toArray(String[] array) {
        return Arrays.asList(array).toArray();
    }
}
