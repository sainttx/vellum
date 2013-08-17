/*
 * Apache Software License 2.0
 */

package vellum.parameter;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import vellum.exception.EnumRuntimeException;

/**
 *
 * @author evanx
 */
public class StringMap extends HashMap<String, String> {
    List<Entry<String, String>> entryList = new ArrayList();

    public StringMap() {
    }

    public StringMap(Map m) {
        super(m);
    }

    public String put(String key, String value) {
        return super.put(key, value);
    }

    
    public String put(String key, Object object) {
        if (object == null) {
            return super.put(key, null);
        } else {
            return super.put(key, object.toString());    
        }
    }

    public long getLong(String key) {
        return Long.parseLong(getString(key));
    }
    
    public String getString(String key) {
        String value = super.get(key);
        if (value == null) {
            throw new EnumRuntimeException(StringMapExceptionType.NOT_FOUND);
        }
        return value;
    }
    
    public String getString(String key, String defaultValue) {
        String value = super.get(key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
    
    public long getLong(String key, long defaultValue) {
        String string = super.get(key);
        if (string == null) {
            return defaultValue;
        }
        return Long.parseLong(string);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }    
}

