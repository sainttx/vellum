/*
 * Apache Software License 2.0
 * Supported by BizSwitch.net   
 */
package vellum.util;

/**
 * Utility methods related to classes.
 * Utility methods for specific types eg {@code String}, are found in {@code Strings}.
 *
 * @author evan
 */
public class Types {

    /**
     * Null-safe equals.
     *
     */
    public static boolean equals(Object object, Object other) {
        if (object == null && other == null) {
            return true;
        }
        if (object == null || other == null) {
            return false;
        }
        if (object.getClass() != other.getClass()) {
            return false;
        }
        return object.equals(other);
    }

    /**
     * Null-safe equals any of given values.
     *
     */
    public static boolean equalsAny(Object value, Object... args) {
        for (Object arg : args) {
            if (Types.equals(value, arg)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Displayable {@code toString()} returning empty string for {@literal null}.
     *
     */
    public static String formatDisplay(Object object) {
        return ArgFormatter.displayFormatter.format(object);
    }

    /**
     * Printable {@code toString()} indicating empty and null values.
     *
     */
    public static String formatPrint(Object object) {
        return ArgFormatter.formatter.format(object);
    }

    public static <T> T newInstance(Class<T> type) {
        try {
            return (T) type.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException(type.getName(), e);
        }
    }

    public static Object parse(Class propertyType, String string) {
        return Convertors.parse(propertyType, string);
    }

    public static Object convert(Class propertyType, Object value) {
        return Convertors.convert(propertyType, value);
    }

    public static int compareTo(Object object, Object other) {
        if (object == other) return 0;
        if (object == null) return -1;
        if (other == null) return 1;
        if (other != null && object instanceof Comparable) {
            Comparable comparable = (Comparable) object;
            return comparable.compareTo(other);
        }
        return object.toString().compareTo(other.toString());
    }

    public static String getStyleClass(Class type) {
        if (isNumber(type)) {
            return "Numeric";
        }
        return type.getSimpleName();
    }

    public static boolean isNumber(Class type) {
        if (type == boolean.class) {
            return false;
        }
        if (type.isPrimitive()) {
            return true;
        }
        if (type.getSuperclass() == Number.class) {
            return true;
        }
        return false;
    }

    public static int hashCode(Object[] values) {
        int hash = 0;
        for (Object value : values) {
            if (value != null) hash ^= value.hashCode();
        }
        return hash;
    }
}
