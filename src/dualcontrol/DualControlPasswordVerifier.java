/*
 * Apache Software License 2.0, (c) Copyright 2013, Evan Summers
 * 
 */

package dualcontrol;

/**
 *
 * @author evans
 */
public class DualControlPasswordVerifier {
    final static private VellumProperties properties = VellumProperties.systemProperties;
    final static private boolean verifyPasswordComplexity = 
            properties.getBoolean("dualcontrol.verifyPasswordComplexity", false);
    final static private int minPasswordLength = 
            properties.getInt("dualcontrol.minPasswordLength", 18);

    public static void assertValid(char[] password) throws Exception {
        String errorMessage = getInvalidMessage(password);
        if (errorMessage != null) {
            throw new Exception(errorMessage);
        }
    }

    public static boolean isValid(char[] password) throws Exception {
        return getInvalidMessage(password) == null;
    }
    
    public static String getInvalidMessage(char[] password) throws Exception {
        if (password.length < minPasswordLength) {
            return "Password too short";
        }
        if (verifyPasswordComplexity) {
            if (!isLetter(password) || !isUpperCase(password) || !isLowerCase(password)
                    || !isDigit(password) || !isPunctuation(password)) {
                return "Insufficient password complexity";
            }
        }
        return null;
    }

    public static boolean isDigit(char[] array) {
        for (char ch : array) {
            if (Character.isDigit(ch)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isPunctuation(char[] array) {
        for (char ch : array) {
            if (!Character.isWhitespace(ch) && !Character.isLetterOrDigit(ch)) {
                return true;
            }
        }
        return false;
    }
    
    
    public static boolean isLetter(char[] array) {
        for (char ch : array) {
            if (Character.isLetter(ch)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isUpperCase(char[] array) {
        for (char ch : array) {
            if (Character.isUpperCase(ch)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isLowerCase(char[] array) {
        for (char ch : array) {
            if (Character.isLowerCase(ch)) {
                return true;
            }
        }
        return false;
    }       
}
