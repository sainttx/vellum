
package dualcontrol;

/**
 *
 * @author evans
 */
public class DualControlPasswords {
    final static int minPasswordLength = 
            Integer.getInteger("dualcontrol.minPasswordLength", 9);

    public static void verifyPassword(char[] password) throws Exception {
        if (password.length < minPasswordLength) {
            throw new Exception("Password too short");
        }
        if (!isLetter(password) || !isUpperCase(password) || !isLowerCase(password) || 
                !isDigit(password) || !isPunctuation(password)) {
            throw new Exception("Insufficient password complexity");            
        } 
    }

    public static String getPasswordInvalidMessage(char[] password) throws Exception {
        if (password.length < minPasswordLength) {
            return "Password too short";
        }
        if (!isLetter(password) || !isUpperCase(password) || !isLowerCase(password) || 
                !isDigit(password) || !isPunctuation(password)) {
            return "Insufficient password complexity";
        } 
        return null;
    }

    public static boolean isValidPassword(char[] password) throws Exception {
        if (password.length < minPasswordLength) {
            return false;
        }
        if (!isLetter(password) || !isUpperCase(password) || !isLowerCase(password) || 
                !isDigit(password) || !isPunctuation(password)) {
            return false;
        } 
        return true;
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
