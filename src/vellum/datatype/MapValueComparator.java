/*
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2011, iPay (Pty) Ltd, Evan Summers
 */

package vellum.datatype;

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
