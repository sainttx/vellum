/*
 * Copyright Evan Summers
 * 
 */
package vellum.crypto;

import vellum.crypto.PackedPasswords;
import vellum.crypto.PasswordHash;
import vellum.crypto.PasswordSalts;
import vellum.crypto.Passwords;
import vellum.crypto.PBECipher;
import vellum.crypto.Base64;
import java.util.Arrays;
import javax.crypto.Cipher;
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
    public void testEncodedLength() throws Exception {
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
        System.out.printf("iterationCountExponent %d: %d\n", Passwords.ITERATION_COUNT_EXPONENT, 2<<Passwords.ITERATION_COUNT_EXPONENT);
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
        PasswordHash passwordHash = new PasswordHash(password, 
                Passwords.ITERATION_COUNT_EXPONENT, Passwords.KEY_SIZE);
        byte[] hashBytes = passwordHash.pack();
        String hashString = Base64.encode(hashBytes);
        System.out.printf("%s\n", hashString);
        System.out.printf("byte array length %d, encoded length %d\n", 
                hashBytes.length, hashString.length());
        assertTrue(PasswordHash.isPacked(hashBytes));
        assertTrue(passwordHash.matches(password));
        assertFalse(passwordHash.matches("wrong".toCharArray()));
        assertTrue(new PasswordHash(passwordHash.pack()).matches(password));
        assertFalse(new PasswordHash(passwordHash.pack()).matches("wrong".toCharArray()));
        passwordHash = new PasswordHash(password, 16, 256);
        assertTrue(new PasswordHash(passwordHash.pack()).matches(password));
    }
    
    @Test
    public void testPacked() throws Exception {
        char[] password = "12345678".toCharArray();
        byte[] hashBytes = PackedPasswords.hashPassword(password);
        String hashString = Base64.encode(hashBytes);
        System.out.printf("testPacked: %s\n", hashString);
        System.out.printf("testPacked: byte array length %d, encoded length %d\n", hashBytes.length, hashString.length());
        assertTrue(PasswordHash.isPacked(hashBytes));
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
        if (PasswordHash.isPacked(packedBytes)) {
            PasswordHash passwordHash = new PasswordHash(packedBytes);
            if (passwordHash.matches(password)) {
                if (passwordHash.getIterationCountExponent() != Passwords.ITERATION_COUNT_EXPONENT ||
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
    
    static final byte[] PBE_SALT = Base64.decode("nD++3Wv9h9MqnS3bO3KJzA==");
    
    @Test
    public void testCipher() throws Exception {
        char[] pbePassword = "sshssh".toCharArray();
        PBECipher cipher = new PBECipher(pbePassword, PBE_SALT);
        char[] userPassword = "12345678".toCharArray();
        PasswordHash passwordHash0 = new PasswordHash(userPassword, 
                Passwords.ITERATION_COUNT_EXPONENT, Passwords.KEY_SIZE);
        byte[] iv = Base64.decode("xI87HaOKY5y9JIjMiqrtLg==");
        byte[] encryptedHash = cipher.encrypt(passwordHash0.pack(), iv);
        byte[] decryptedHash = cipher.decrypt(encryptedHash, iv);
        assertTrue(Arrays.equals(decryptedHash, passwordHash0.pack()));
        encryptedHash = cipher.encrypt(decryptedHash, iv);
        decryptedHash = cipher.decrypt(encryptedHash, iv);
        assertTrue(Arrays.equals(decryptedHash, passwordHash0.pack()));
        assertTrue(new PasswordHash(decryptedHash).matches(userPassword));
        assertFalse(new PasswordHash(decryptedHash).matches("wrong".toCharArray()));        
    }
    
}
