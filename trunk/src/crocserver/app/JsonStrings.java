/*
 * Copyright Evan Summers
 * 
 */
package crocserver.app;

import vellum.logr.Logr;
import vellum.logr.LogrFactory;

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
    
}
