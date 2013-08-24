
package dualcontrol;

/**
 *
 * @author evans
 */
public class DualControlPasswords {
    final static boolean verifyPassword = 
            getBoolean("dualcontrol.verifyPassword", true);
    final static boolean verifyPasswordComplexity = 
            getBoolean("dualcontrol.verifyPasswordComplexity", true);
    final static int minPassLength = 
            Integer.getInteger("dualcontrol.minPassLength", 13);

    public static void assertValid(char[] password) throws Exception {
        String errorMessage = getErrorMessage(password);
        if (errorMessage != null) {
            throw new Exception(errorMessage);
        }
    }

    public static boolean isValid(char[] password) throws Exception {
        return getErrorMessage(password) == null;
    }
    
    public static String getErrorMessage(char[] password) throws Exception {
        if (verifyPassword) {
            if (password.length < minPassLength) {
                return "Password too short";
            }
            if (verifyPasswordComplexity) {
                if (!isLetter(password) || !isUpperCase(password) || !isLowerCase(password)
                        || !isDigit(password) || !isPunctuation(password)) {
                    return "Insufficient password complexity";
                }
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
    
    public static boolean getBoolean(String name, boolean defaultValue) {
        String string = System.getProperty("dualcontrol.verifyPassword");
        if (string == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(string);
    }    
}
