/*
 * Copyright Evan Summers
 * 
 */
package vellum.util;

import java.util.Arrays;
import org.junit.Test;
import static junit.framework.Assert.*;
import vellum.datatype.Millis;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evan
 */
public class PasswordsTest {
    private static Logr logger = LogrFactory.getLogger(PasswordsTest.class);

    @Test
    public void testSaltEncoding() throws Exception {
        byte[] saltBytes = Passwords.getSpec().nextSalt();
        String encodedSalt = Base64.encode(saltBytes);
        System.out.println(Bytes.formatHex(saltBytes));
        System.out.println(encodedSalt);
        assertEquals(encodedSalt.length(), 24);
        assertEquals(encodedSalt.substring(22, 24), "==");
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
        byte[] salt = Passwords.getSpec().nextSalt();
        byte[] hash = Passwords.hashPassword(password, salt);
        assertTrue(Passwords.matches(password, hash, salt));
        byte[] otherSaltBytes = Arrays.copyOf(salt, salt.length);
        otherSaltBytes[0] ^= otherSaltBytes[0];
        assertFalse(Passwords.matches(password, hash, otherSaltBytes));
        assertFalse(Passwords.matches("wrong".toCharArray(), hash, salt));
    }

    @Test
    public void testEffort() throws Exception {
        char[] password = "12345678".toCharArray();
        long startMillis = System.currentTimeMillis();
        byte[] saltBytes = Passwords.getSpec().nextSalt();
        Passwords.hashPassword(password, saltBytes);
        System.out.println("time " + Millis.elapsed(startMillis));
        if (Millis.elapsed(startMillis) < 10) {
            System.out.println("Ooooooo.... i don't know");
        } else if (Millis.elapsed(startMillis) > 500) {
            System.out.println("Ooooooo....");
        }
    }
    
    @Test
    public void testPacked() throws Exception {
        char[] password = "12345678".toCharArray();
        byte[] hash = PackedPasswords.hashPassword(password);
        System.out.printf("testPacked: length = %d\n", hash.length);
        assertTrue(PackedPasswords.matches(password, hash));
        assertFalse(PackedPasswords.matches("wrong".toCharArray(), hash));
    }

    @Test
    public void testRevision() throws Exception {
        char[] password = "12345678".toCharArray();
        byte[] hashd = PackedPasswords.hashPassword(password);
        byte[] hash0 = PackedPasswords.hashPassword(password, 0);
        byte[] hash1 = PackedPasswords.hashPassword(password, 1);
        byte[] hashn = PackedPasswords.hashPassword(password, Passwords.LATEST_REVISION_INDEX);
        System.out.println(Base64.encode(hashd));
        System.out.println(Base64.encode(hash0));
        System.out.println(Base64.encode(hash1));
        System.out.println(Base64.encode(hashn));
        assertFalse(Arrays.equals(hashd, hash0));
        assertFalse(Arrays.equals(hash0, hash1));
        assertFalse(Arrays.equals(hash1, hashn));
        assertFalse(Arrays.equals(hashn, hashd));
        assertTrue(PackedPasswords.matches(password, hashd));
        assertTrue(PackedPasswords.matches(password, hash0));
        assertTrue(PackedPasswords.matches(password, hash1));
        assertTrue(PackedPasswords.matches(password, hashn));
        assertTrue(matches("evanx", password, hash0));
        assertTrue(matches("evanx", password, hash1));
        assertFalse(PackedPasswords.matches("wrong".toCharArray(), hash0));
    }

    public boolean matchesUnsalted(char[] password, byte[] passwordHash) throws Exception {
        return PackedPasswords.matches(password, passwordHash);
    }

    @Test
    public void testProto() throws Exception {
        char[] password = "12345678".toCharArray();
        byte[] hash = PackedPasswords.hashPassword(password);
        assertTrue(matches("evanx", password, hash));
        assertTrue(PackedPasswords.matches(password, hash));        
    }
    
    public boolean matches(String user, char[] password, byte[] packedBytes) throws Exception {
        if (PasswordHash.isPacked(packedBytes)) {
            PasswordHash passwordHash = new PasswordHash(packedBytes);
            byte[] hash = Passwords.hashPassword(password, passwordHash.getSalt(), passwordHash.getRevisionIndex());
            if (Arrays.equals(hash, passwordHash.getHash())) {
                if (passwordHash.getRevisionIndex() != Passwords.LATEST_REVISION_INDEX) {
                    packedBytes = PackedPasswords.hashPassword(password);
                    persistRevisedPasswordHash(user, packedBytes);
                }
                return true;
            }
            return false;
        }
        if (matchesUnsalted(password, packedBytes)) {
            packedBytes = PackedPasswords.hashPassword(password);
            persistRevisedPasswordHash(user, packedBytes);
            return true;
        }
        return false;
    }

    private void persistRevisedPasswordHash(String user, byte[] passwordHash) {
        logger.info("persistNewPasswordHash", user, Base64.encode(passwordHash));
    }
}
