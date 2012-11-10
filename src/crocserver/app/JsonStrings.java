/*
 * Copyright Evan Summers
 * 
 */
package crocserver.app;

import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.parameter.StringMap;

/**
 *
 * @author evan
 */
public class JsonStrings {
    static Logr logger = LogrFactory.getLogger(JsonStrings.class);
    
    public static String get(String json, String key) {
        String pattern = "\"" + key + "\"";
        int index = json.indexOf(pattern);
        if (index > 0) {
            int beginIndex = json.indexOf('\"', index + pattern.length());
            beginIndex += 1;
            index = json.indexOf('\"', beginIndex);
            if (index > beginIndex) {
                return json.substring(beginIndex, index);
            }
        }
        return null;
    }
    
    public static String buildJson(StringMap map) {
        StringBuilder builder = new StringBuilder();
        for (String key : map.keySet()) {
            if (builder.length() > 0) {
                builder.append(",");
            }
            builder.append(String.format("\n\"%s\" : \"%s\"", key, map.get(key)));
        }
        return "{" + builder.toString() + "\n}";
    }

}
