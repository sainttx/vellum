/*
 * Copyright Evan Summers
 * 
 */
package vellum.datatype;

import java.util.regex.Pattern;

/**
 *
 * @author evan
 */
public class Patterns {
    private static final String USERNAME_PATTERN_CONTENT = "[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*";
    private static final String URL_PATTERN_CONTENT = "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})";
    
    public static Pattern USERNAME_PATTERN = Pattern.compile("^" + USERNAME_PATTERN_CONTENT + "$");
    public static Pattern URL_PATTERN = Pattern.compile("^" + URL_PATTERN_CONTENT + "$");
    public static Pattern EMAIL_PATTERN = Pattern.compile("^" + USERNAME_PATTERN_CONTENT + "@" + URL_PATTERN_CONTENT + "$");

    public static boolean matchesUserName(String string) {
        return USERNAME_PATTERN.matcher(string).matches();
    }
    
    public static boolean matchesUrl(String string) {
        return URL_PATTERN.matcher(string).matches();
    }
    
    public static boolean matchesEmail(String string) {
        return EMAIL_PATTERN.matcher(string).matches();
    }
}
