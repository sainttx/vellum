/*
 * Copyright Evan Summers
 * 
 */
package vellum.datatype;

/**
 *
 * @author evan
 */
public class Emails {

    public static boolean matchesEmail(String string) {
        return Patterns.EMAIL_PATTERN.matcher(string).matches();
    }

    public static String getUsername(String string) {
        int index = string.indexOf("@");
        if (index > 0) {
            return string.substring(0, index);
        }
        return string;
    }
    
}
