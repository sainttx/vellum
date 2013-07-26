/*
 * Copyright Evan Summers
 * 
 */
package bizstat.enumtype;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import vellum.datatype.Milli;

/**
 *
 * @author evan
 */
public enum StatusChangeType {
    OK_WARNING,
    WARNING_CRITICAL,
    CRITICAL_WARNING,
    WARNING_OK,
    OK_CRITICAL,
    CRITICAL_OK;
    

    public static Map<StatusChangeType, Long> newIntervalMap(List<String> list) {
        Map<StatusChangeType, Long> map = new HashMap();
        map.put(OK_WARNING, Milli.parse(list.get(0)));
        map.put(WARNING_CRITICAL, Milli.parse(list.get(1)));
        map.put(OK_CRITICAL, Milli.parse(list.get(0)) + Milli.parse(list.get(1)));
        map.put(CRITICAL_WARNING, Milli.parse(list.get(2)));
        map.put(WARNING_OK, Milli.parse(list.get(3)));
        map.put(CRITICAL_OK, Milli.parse(list.get(2)) + Milli.parse(list.get(3)));
        return map;
    }
    
    public static Map<StatusChangeType, Integer> newIntegerMap(List<String> list) {
        Map<StatusChangeType, Integer> map = new HashMap();
        map.put(OK_WARNING, Integer.parseInt(list.get(0)));
        map.put(WARNING_CRITICAL, Integer.parseInt(list.get(1)));
        map.put(OK_CRITICAL, Integer.parseInt(list.get(0)) + Integer.parseInt(list.get(1)));
        map.put(CRITICAL_WARNING, Integer.parseInt(list.get(2)));
        map.put(WARNING_OK, Integer.parseInt(list.get(3)));
        map.put(CRITICAL_OK, Integer.parseInt(list.get(2)) + Integer.parseInt(list.get(3)));
        return map;
    }

    public static Map<StatusChangeType, Float> newValueMap(List<String> list) {
        Map<StatusChangeType, Float> map = new HashMap();
        map.put(OK_WARNING, Float.parseFloat(list.get(0)));
        map.put(OK_CRITICAL, Float.parseFloat(list.get(1)));
        map.put(WARNING_CRITICAL, Float.parseFloat(list.get(1)));
        map.put(CRITICAL_WARNING, Float.parseFloat(list.get(2)));
        map.put(CRITICAL_OK, Float.parseFloat(list.get(3)));
        map.put(WARNING_OK, Float.parseFloat(list.get(3)));
        return map;
    }
    
}
