/*
 */
package jx;

import java.util.HashMap;

/**
 *
 * @author evans
 */
public class JxMap extends HashMap<String, Object> {
    
    public JxMap() {        
    }

    public int getInt(String key, int defaultValue) {
        return Convertors.coerceInt(super.get(key), defaultValue);
    }

    public Integer getInteger(String key) {
        return Convertors.coerceInteger(super.get(key), null);
    }

    
}
