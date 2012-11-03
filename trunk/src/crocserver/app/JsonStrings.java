/*
 * Copyright Evan Summers
 * 
 */
package crocserver.app;

/**
 *
 * @author evan
 */
public class JsonStrings {
    
    public static String get(String json, String key) {
        String pattern = "\"" + key + "\" : \"";
        int index = json.indexOf(pattern);
        if (index > 0) {
            int beginIndex = index + pattern.length();
            index = json.indexOf('\"', beginIndex);
            if (index > beginIndex) {
                return json.substring(beginIndex, index);
            }
        }
        return null;
    }
    
}
