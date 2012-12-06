/*
 * Copyright Evan Summers
 * 
 */
package vellum.util;

import java.security.SecureRandom;
import java.util.Arrays;
import org.junit.Test;
import static junit.framework.Assert.*;
import vellum.datatype.Millis;

/**
 *
 * @author evan
 */
public class PasswordsTest {

    @Test
    public void testSaltEncoding() throws Exception {
        byte[] saltBytes = Passwords.nextSalt();
        String salt = Base64.encode(saltBytes);
        System.out.println(salt);
        assertEquals(salt.length(), 24);
        assertEquals(salt.substring(22, 24), "==");
    }

    @Test
    public void testLength() throws Exception {
        System.out.printf("128bit: %d\n", Base64.encode(new byte[128/8]).length());
        System.out.printf("160bit: %d\n", Base64.encode(new byte[160/8]).length());
        System.out.printf("256bit: %d\n", Base64.encode(new byte[256/8]).length());
        System.out.printf("512bit: %d\n", Base64.encode(new byte[512/8]).length());
    }

    @Test
    public void test() throws Exception {
        char[] password = "12345678".toCharArray();
        byte[] saltBytes = Passwords.nextSalt();
        String salt = Base64.encode(saltBytes);
        String hash = Passwords.hashPassword(password, saltBytes);
        assertTrue(Passwords.matches(password, hash, salt));
        byte[] otherSaltBytes = Arrays.copyOf(saltBytes, saltBytes.length);
        otherSaltBytes[0] ^= otherSaltBytes[0];
        assertFalse(Passwords.matches(password, hash, Base64.encode(otherSaltBytes)));
        assertFalse(Passwords.matches("wrong".toCharArray(), hash, salt));
    }

    @Test
    public void testEffort() throws Exception {
        char[] password = "12345678".toCharArray();
        long startMillis = System.currentTimeMillis();
        byte[] saltBytes = Passwords.nextSalt();
        Passwords.hashPassword(password, saltBytes);
        System.out.println("time " + Millis.elapsed(startMillis));
        assertTrue(Millis.elapsed(startMillis) > 10);
        assertTrue(Millis.elapsed(startMillis) < 500);
    }
    
    @Test
    public void testPacked() throws Exception {
        char[] password = "12345678".toCharArray();
        String hash = PackedPasswords.hashPassword(password);
        System.out.println(hash);
        assertTrue(PackedPasswords.isPacked(hash));
        assertTrue(PackedPasswords.matches(password, hash));
        assertFalse(PackedPasswords.matches("wrong".toCharArray(), hash));
    }

    public boolean matchesUnsalted(char[] password, String passwordHash) throws Exception {
        return PackedPasswords.matches(password, passwordHash);
    }

    @Test
    public void testProto() throws Exception {
        char[] password = "12345678".toCharArray();
        String hash = PackedPasswords.hashPassword(password);
        assertTrue(matches("evanx", password, hash));
        assertTrue(PackedPasswords.matches(password, hash));        
    }
    
    public boolean matches(String user, char[] password, String passwordHash) throws Exception {
        if (PackedPasswords.isPacked(passwordHash)) {
            if (PackedPasswords.matches(password, passwordHash)) {
                if (!PackedPasswords.isPackedLatest(passwordHash)) {
                    passwordHash = PackedPasswords.hashPassword(password);
                    persistNewPasswordHash(user, passwordHash);                    
                }
                return true;
            }
            return false;
        }
        if (matchesUnsalted(password, passwordHash)) {
            passwordHash = PackedPasswords.hashPassword(password);
            persistNewPasswordHash(user, passwordHash);
            return true;
        }
        return false;
    }

    private void persistNewPasswordHash(String user, String passwordHash) {
    }
}
