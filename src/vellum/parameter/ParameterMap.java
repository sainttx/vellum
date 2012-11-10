/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2011, iPay (Pty) Ltd, Evan Summers
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 */

package vellum.parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author evanx
 */
public class ParameterMap extends HashMap<String, String> {
    List<Entry<String, String>> entryList = new ArrayList();

    public String getString(String key, String defaultValue) {
        String value = super.get(key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
}

