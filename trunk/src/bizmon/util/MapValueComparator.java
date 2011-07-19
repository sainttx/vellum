/*
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 * (c) Copyright 2011, iPay (Pty) Ltd, Evan Summers
 */

package bizmon.util;

import java.util.Comparator;
import java.util.Map;

/**
 *
 * @author evanx
 */
public class MapValueComparator implements Comparator {

    Map map;

    public MapValueComparator(Map map) {
        this.map = map;
    }

    public int compare(Object e1, Object e2) {
        Comparable v1 = (Comparable) map.get(e1);
        Comparable v2 = (Comparable) map.get(e2);
        return v2.compareTo(v1);
    }
}
