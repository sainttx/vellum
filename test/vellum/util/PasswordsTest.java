/*
 * Copyright Evan Summers
 * 
 */
package vellum.util;

/**
 *
 * @author evan
 */
public class PasswordsTest {
    
    private void test() throws Exception {
        String password1 = "123456";
        String password2 = "123457";
        byte[] salt1 = Passwords.nextSalt();
        byte[] salt2 = Passwords.nextSalt();
        String saltHash1 = Passwords.encode(salt1);
        String saltHash2 = Passwords.encode(salt2);
        String hash1 = Passwords.hashPassword(password1, salt1);
        String hash2 = Passwords.hashPassword(password2, salt2);
        assertTrue(Passwords.matches(password1, hash1, saltHash1));
        assertTrue(Passwords.matches(password2, hash2, saltHash2));
        assertTrue(!Passwords.matches(password1, hash2, saltHash2));
        assertTrue(!Passwords.matches(password2, hash1, saltHash1));
    }
    
    public static void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError();
        }
    }
    public static void main(String[] args) throws Exception {
        new PasswordsTest().test();
    }
}
