/*
 * Apache Software License 2.0
 * Supported by BizSwitch.net   
 */
package vellum.util;

import java.math.BigDecimal;
import java.util.Date;

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
        if (object == null) {
            return "";
        }
        if (object instanceof Date) {
            return Calendars.timestampFormat.format((Date) object);
        }
        if (object instanceof BigDecimal) {
            return Numbers.formatMoney((BigDecimal) object);
        }
        if (object instanceof Double) {
            return Numbers.formatMoney((Double) object);
        }
        return object.toString();
    }

    /**
     * Printable {@code toString()} indicating empty and null values.
     *
     */
    public static String formatPrint(Object arg) {
        if (arg == null) {
            return "null";
        } else if (arg instanceof Class) {
            return ((Class) arg).getSimpleName();
        } else if (arg instanceof Date) {
            return Calendars.timestampFormat.format((Date) arg);
        } else if (Strings.isEmpty(arg.toString())) {
            return "empty";
        } else if (arg instanceof Object[]) {
            return String.format("[%s]", Lists.formatPrint(", ", (Object[]) arg));
        } else if (arg instanceof String[]) {
            return String.format("[%s]", Lists.formatPrint(", ", (String[]) arg));
        } else {
            return arg.toString();
        }
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
