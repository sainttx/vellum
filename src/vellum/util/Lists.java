/*
 * Apache Software License 2.0   
 */
package vellum.util;

import vellum.datatype.MapEntryComparator;
import vellum.datatype.MapValueComparator;
import vellum.format.ListFormats;
import java.util.*;

/**
 * Utility methods related to classes.
 * Utility methods for specific types eg {@code String}, are found in {@code Strings}.
 *
 * @author evan
 */
public class Lists {

    public static <T> LinkedList<T> sortedLinkedList(Collection<T> collection, Comparator<T> comparator) {
        LinkedList list = new LinkedList(collection);
        Collections.sort(list, comparator);
        return list;
    }
    
    public static <T> LinkedList<T> sortedReverseLinkedList(Collection<T> collection, Comparator<T> comparator) {
        return sortedLinkedList(collection, Collections.reverseOrder(comparator));
    }

    public static Map sortByValue(Map map) {
        Map result = new TreeMap(new MapValueComparator(map));
        result.putAll(map);
        return result;
    }
    
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
        return ListFormats.formatter.formatArray(args);
    }

    /**
     * Convenience method used in {@code toString()} methods of objects
     * to format their properties.
     *
     */
    public static String format(Collection collection) {
        return ListFormats.formatter.formatArray(collection);
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

    public static List<Map.Entry> sortedEntryList(Map map) {
        List list = new ArrayList(map.entrySet());
        Collections.sort(list, new MapEntryComparator());
        return list;
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

    public static boolean contains(String[] array, String string) {
        for (String item : array) {
            if (item.equals(string)) {
                return true;
            }
        }
        return false;
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

    public static String getFirst(String[] array) {
        if (array != null && array.length > 0) {
            return array[0];
        }
        return null;
    }

    public static <T> T getFirst(List<T> list) {
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
    
    public static String getLast(String[] array) {
        if (array != null && array.length > 0) {
            return array[array.length - 1];
        }
        return null;
    }

    public static Object[] toArray(String[] array) {
        return Arrays.asList(array).toArray();
    }

    public static List toList(byte[] array) {
        List list = new ArrayList();
        for (byte element : array) {
            list.add(element);
        }
        return list;
    }
    
}
