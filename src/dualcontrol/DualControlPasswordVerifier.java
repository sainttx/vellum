/*
 * Apache Software License 2.0, (c) Copyright 2013, Evan Summers
 * 
 */

package dualcontrol;

import vellum.util.VellumProperties;

/**
 *
 * @author evans
 */
public class DualControlPasswordVerifier {
    final boolean verifyPasswordComplexity;
    final int minPasswordLength;

    public DualControlPasswordVerifier(VellumProperties properties) {
        verifyPasswordComplexity = properties.getBoolean(
                "dualcontrol.verifyPasswordComplexity", false);
        minPasswordLength = properties.getInt(
                "dualcontrol.minPasswordLength", 18);
    }
    
    public String getInvalidMessage(char[] password) throws Exception {
        if (password.length < minPasswordLength) {
            return "Password too short";
        }
        if (verifyPasswordComplexity) {
            if (!containsLetter(password) || !containsUpperCase(password) || 
                    !containsLowerCase(password) || !containsDigit(password) || 
                    !containsPunctuation(password)) {
                return "Insufficient password complexity";
            }
        }
        return null;
    }

    public void assertValid(char[] password) throws Exception {
        String errorMessage = getInvalidMessage(password);
        if (errorMessage != null) {
            throw new Exception(errorMessage);
        }
    }

    public boolean isValid(char[] password) throws Exception {
        return getInvalidMessage(password) == null;
    }
    
    public static boolean containsDigit(char[] array) {
        for (char ch : array) {
            if (Character.isDigit(ch)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean containsPunctuation(char[] array) {
        for (char ch : array) {
            if (!Character.isWhitespace(ch) && !Character.isLetterOrDigit(ch)) {
                return true;
            }
        }
        return false;
    }
    
    
    public static boolean containsLetter(char[] array) {
        for (char ch : array) {
            if (Character.isLetter(ch)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean containsUpperCase(char[] array) {
        for (char ch : array) {
            if (Character.isUpperCase(ch)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsLowerCase(char[] array) {
        for (char ch : array) {
            if (Character.isLowerCase(ch)) {
                return true;
            }
        }
        return false;
    }       
}
