/*
 * Copyright Evan Summers
 * 
 */
package vellum.crypto;

import java.util.Arrays;
import org.junit.Test;
import static junit.framework.Assert.*;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evan
 */
public class PasswordCipherTest {
    private final Logr logger = LogrFactory.getLogger(PasswordCipherTest.class);
    private final int ITERATION_COUNT = 1024;
    private final int KEY_SIZE = 128;
    private final byte[] IV = Base64.decode("xI87HaOKY5y9JIjMiqrtLg==");
    private final byte[] SALT = Base64.decode("nD++3Wv9h9MqnS3bO3KJzA==");
    private final char[] PBE_PASSWORD = "ssh ssh".toCharArray();
    private final char[] USER_PASSWORD = "12345678".toCharArray();

    @Test
    public void testCipherIV() throws Exception {
        PBECipher cipher = new PBECipher(PBE_PASSWORD, SALT, ITERATION_COUNT, KEY_SIZE);
        PasswordHash passwordHash = new PasswordHash(USER_PASSWORD, ITERATION_COUNT, KEY_SIZE);
        byte[] encryptedHash = cipher.encrypt(passwordHash.getBytes(), IV);
        byte[] decryptedHash = cipher.decrypt(encryptedHash, IV);
        assertTrue(Arrays.equals(decryptedHash, passwordHash.getBytes()));
        encryptedHash = cipher.encrypt(decryptedHash, IV);
        decryptedHash = cipher.decrypt(encryptedHash, IV);
        assertTrue(Arrays.equals(decryptedHash, passwordHash.getBytes()));
    }
    
    @Test
    public void testCipher() throws Exception {
        PBECipher cipher = new PBECipher(PBE_PASSWORD, SALT, ITERATION_COUNT, KEY_SIZE);
        PasswordHash passwordHash = new PasswordHash(USER_PASSWORD, ITERATION_COUNT, KEY_SIZE);
        Encrypted encrypted = cipher.encrypt(passwordHash.getBytes());
        byte[] decryptedHash = cipher.decrypt(encrypted.getEncryptedBytes(), encrypted.getIv());
        assertTrue(Arrays.equals(decryptedHash, passwordHash.getBytes()));
        assertTrue(new PasswordHash(decryptedHash).matches(USER_PASSWORD));
        assertFalse(new PasswordHash(decryptedHash).matches("wrong".toCharArray()));        
        passwordHash = new PasswordHash(passwordHash.getBytes());
        assertTrue(passwordHash.matches(USER_PASSWORD));
    }

    //@Test
    public void testPasswordHashEncryption() throws Exception {
        PBECipher cipher = new PBECipher(PBE_PASSWORD, SALT, ITERATION_COUNT, KEY_SIZE);
        PasswordHash passwordHash = new PasswordHash(USER_PASSWORD, ITERATION_COUNT, KEY_SIZE);
        passwordHash.encryptSalt(cipher);
        passwordHash.decryptSalt(cipher);
        assertTrue(passwordHash.matches(USER_PASSWORD));
        passwordHash.encryptSalt(cipher);
        passwordHash = new PasswordHash(passwordHash.getBytes());
        passwordHash.decryptSalt(cipher);
        assertTrue(passwordHash.matches(USER_PASSWORD));
        assertTrue(!passwordHash.matches("wrong".toCharArray()));        
    }

    @Test(expected = AssertionError.class)
    public void testDoubleEncryptException() throws Exception {
        PBECipher cipher = new PBECipher(PBE_PASSWORD, SALT, ITERATION_COUNT, KEY_SIZE);
        PasswordHash passwordHash = new PasswordHash(USER_PASSWORD, ITERATION_COUNT, KEY_SIZE);
        passwordHash.encryptSalt(cipher);
        passwordHash.encryptSalt(cipher);
    }

    @Test(expected = AssertionError.class)
    public void testDoubleDecryptException() throws Exception {
        PBECipher cipher = new PBECipher(PBE_PASSWORD, SALT, ITERATION_COUNT, KEY_SIZE);
        PasswordHash passwordHash = new PasswordHash(USER_PASSWORD, ITERATION_COUNT, KEY_SIZE);
        passwordHash.encryptSalt(cipher);
        passwordHash.decryptSalt(cipher);
        passwordHash.decryptSalt(cipher);
    }
    
    @Test
    public void testDecrypt() throws Exception {
        PasswordHash passwordHash = new PasswordHash(USER_PASSWORD, ITERATION_COUNT, KEY_SIZE);
        assertTrue(passwordHash.getIv().length == 0);
        byte[] hash = passwordHash.getHash();
        byte[] salt = passwordHash.getSalt();
        PBECipher cipher = new PBECipher(PBE_PASSWORD, SALT, ITERATION_COUNT, KEY_SIZE);
        passwordHash.encryptSalt(cipher);
        assertTrue(passwordHash.getIv().length != 0);
        assertTrue(!Arrays.equals(salt, passwordHash.getSalt()));
        passwordHash.decryptSalt(cipher);
        assertTrue(passwordHash.getIv().length == 0);
        assertTrue(Arrays.equals(salt, passwordHash.getSalt()));
        assertTrue(Arrays.equals(hash, passwordHash.getHash()));
        passwordHash = new PasswordHash(passwordHash.getBytes());
        passwordHash.encryptSalt(cipher);
        passwordHash.decryptSalt(cipher);
        assertTrue(passwordHash.getIv().length == 0);
        assertTrue(Arrays.equals(hash, passwordHash.getHash()));
    }    
}
