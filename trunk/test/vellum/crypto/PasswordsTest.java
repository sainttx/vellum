/*
 * Copyright Evan Summers
 * 
 */
package vellum.crypto;

import java.util.Arrays;
import org.junit.Test;
import static junit.framework.Assert.*;
import vellum.datatype.Millis;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.util.Bytes;

/**
 *
 * @author evan
 */
public class PasswordsTest {
    private static Logr logger = LogrFactory.getLogger(PasswordsTest.class);
    int iterationCount = 1024;
    int keySize = 128;

    @Test
    public void testSaltEncoding() throws Exception {
        byte[] saltBytes = PasswordSalts.nextSalt();
        String encodedSalt = Base64.encode(saltBytes);
        System.out.println(Bytes.formatHex(saltBytes));
        System.out.println(encodedSalt);
        assertEquals(encodedSalt.length(), 24);
        assertEquals(encodedSalt.substring(22, 24), "==");
    }

    @Test
    public void printEncodedLength() throws Exception {
        System.out.printf("testEncodedLength 128bit: %d\n", Base64.encode(new byte[128/8]).length());
        System.out.printf("testEncodedLength 160bit: %d\n", Base64.encode(new byte[160/8]).length());
        System.out.printf("testEncodedLength 256bit: %d\n", Base64.encode(new byte[256/8]).length());
        System.out.printf("testEncodedLength 512bit: %d\n", Base64.encode(new byte[512/8]).length());
    }

    @Test
    public void test() throws Exception {
        char[] password = "12345678".toCharArray();
        byte[] salt = PasswordSalts.nextSalt();
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
        byte[] saltBytes = PasswordSalts.nextSalt();
        Passwords.hashPassword(password, saltBytes);
        System.out.println("time " + Millis.elapsed(startMillis));
        if (Millis.elapsed(startMillis) < 10) {
            System.out.println("Ooooooo.... i'm not sure");
        } else if (Millis.elapsed(startMillis) > 500) {
            System.out.println("Ooooooo.... i don't know");
        }
    }

    @Test
    public void testPasswordHash() throws Exception {
        char[] password = "12345678".toCharArray();
        PasswordHash passwordHash = new PasswordHash(password, iterationCount, keySize);
        byte[] hashBytes = passwordHash.getBytes();
        String hashString = Base64.encode(hashBytes);
        System.out.printf("%s\n", hashString);
        System.out.printf("byte array length %d, encoded length %d\n", 
                hashBytes.length, hashString.length());
        assertTrue(PasswordHash.verifyBytes(hashBytes));
        assertTrue(passwordHash.matches(password));
        assertFalse(passwordHash.matches("wrong".toCharArray()));
        assertTrue(new PasswordHash(passwordHash.getBytes()).matches(password));
        assertFalse(new PasswordHash(passwordHash.getBytes()).matches("wrong".toCharArray()));
        passwordHash = new PasswordHash(password, 16, 256);
        assertTrue(new PasswordHash(passwordHash.getBytes()).matches(password));
    }
    
    @Test
    public void testPacked() throws Exception {
        char[] password = "12345678".toCharArray();
        byte[] hashBytes = PackedPasswords.hashPassword(password);
        String hashString = Base64.encode(hashBytes);
        System.out.printf("testPacked: %s\n", hashString);
        System.out.printf("testPacked: byte array length %d, encoded length %d\n", hashBytes.length, hashString.length());
        assertTrue(PasswordHash.verifyBytes(hashBytes));
        assertTrue(PackedPasswords.matches(password, hashBytes));
        assertFalse(PackedPasswords.matches("wrong".toCharArray(), hashBytes));
    }

    @Test
    public void testRevision() throws Exception {
        char[] password = "12345678".toCharArray();
        byte[] hash0 = PackedPasswords.hashPassword(password);
        byte[] hash1 = PackedPasswords.hashPassword(password, 16, 256);
        System.out.println(Base64.encode(hash0));
        System.out.println(Base64.encode(hash1));
        assertFalse(Arrays.equals(hash0, hash1));
        assertTrue(PackedPasswords.matches(password, hash0));
        assertTrue(PackedPasswords.matches(password, hash1));
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
        byte[] hash0 = PackedPasswords.hashPassword(password);
        byte[] hash1 = PackedPasswords.hashPassword(password, 16, 256);
        assertTrue(matches("evanx", password, hash0));
        assertTrue(matches("evanx", password, hash1));
        assertTrue(PackedPasswords.matches(password, hash0));        
        assertTrue(PackedPasswords.matches(password, hash1));
    }
    
    public boolean matches(String user, char[] password, byte[] packedBytes) throws Exception {
        if (PasswordHash.verifyBytes(packedBytes)) {
            PasswordHash passwordHash = new PasswordHash(packedBytes);
            if (passwordHash.matches(password)) {
                if (passwordHash.getIterationCount() != Passwords.ITERATION_COUNT ||
                        passwordHash.getKeySize() != Passwords.KEY_SIZE) {
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
