/*
 */
package vellum.hype;

/**
 *
 * @author evans
 */
public class Utils {

    public static boolean contains(char[] array, char ch) {
        for (char item : array) {
            if (item == ch) return true;
        }
        return false;
    }

    public static boolean isWhitespace(char ch) {
        return ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n';
    }

    public static boolean isWord(char ch) {
        return Character.isLetterOrDigit(ch) || ch == '_';
    }

    public static boolean isWhitespace(String string) {
        if (string == null || string.length() == 0) return false;
        for (char ch : string.toCharArray()) {
            if (!isWhitespace(ch)) return false;            
        }
        return true;
    }
    
    public static boolean isWord(String string) {
        if (string == null || string.length() == 0) return false;
        for (char ch : string.toCharArray()) {
            if (!isWord(ch)) return false;            
        }
        return true;
    }    
    
}
