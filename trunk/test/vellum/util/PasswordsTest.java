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
        String password = "12345678";
        byte[] saltBytes = Passwords.nextSalt();
        String salt = Base64.encode(saltBytes);
        String hash = Passwords.hashPassword(password, saltBytes);
        assertTrue(Passwords.matches(password, hash, salt));
        byte[] otherSaltBytes = Arrays.copyOf(saltBytes, saltBytes.length);
        otherSaltBytes[0] ^= otherSaltBytes[0];
        assertFalse(Passwords.matches(password, hash, Base64.encode(otherSaltBytes)));
        assertFalse(Passwords.matches("not" + password, hash, salt));
    }

    @Test
    public void testEffort() throws Exception {
        String password = "12345678";
        long startMillis = System.currentTimeMillis();
        byte[] saltBytes = Passwords.nextSalt();
        Passwords.hashPassword(password, saltBytes);
        System.out.println("time " + Millis.elapsed(startMillis));
        assertTrue(Millis.elapsed(startMillis) > 10);
        assertTrue(Millis.elapsed(startMillis) < 500);
    }
    
    @Test
    public void test2() throws Exception {
        String password1 = "12345678";
        String password2 = "12345789";
        byte[] saltBytes1 = Passwords.nextSalt();
        byte[] saltBytes2 = Passwords.nextSalt();
        String salt1 = Base64.encode(saltBytes1);
        String salt2 = Base64.encode(saltBytes2);
        String hash1 = Passwords.hashPassword(password1, saltBytes1);
        String hash2 = Passwords.hashPassword(password2, saltBytes2);
        assertTrue(Passwords.matches(password1, hash1, salt1));
        assertTrue(Passwords.matches(password2, hash2, salt2));
        assertTrue(!Passwords.matches(password1, hash2, salt2));
        assertTrue(!Passwords.matches(password2, hash1, salt1));
    }

    @Test
    public void testPacked() throws Exception {
        String password = "12345678";
        String hash = PackedPasswords.hashPassword(password);
        System.out.println(hash);
        assertEquals(PackedPasswords.HASH_LENGTH, hash.length());
        assertTrue(PackedPasswords.isPacked(hash));
        assertTrue(PackedPasswords.matches(password, hash));
        assertFalse(PackedPasswords.matches("wrong", hash));
    }

    public boolean matchesUnsalted(String password, String passwordHash) throws Exception {
        return PackedPasswords.matches(password, passwordHash);
    }

    @Test
    public void testProto() throws Exception {
        String password = "12345678";
        String hash = PackedPasswords.hashPassword(password);
        assertTrue(matches("evanx", password, hash));
        assertTrue(PackedPasswords.matches(password, hash));        
    }
    
    public boolean matches(String user, String password, String passwordHash) throws Exception {
        if (PackedPasswords.isPacked(passwordHash)) {
            return PackedPasswords.matches(password, passwordHash);
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
