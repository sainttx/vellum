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
public class SaltCipherTest {
    static Logr logger = LogrFactory.getLogger(SaltCipherTest.class);
    int iterationCount = 1024;
    int keySize = 128;
    byte[] pbeSalt = Base64.decode("nD++3Wv9h9MqnS3bO3KJzA==");
    char[] pbePassword = "sshssh".toCharArray();
    char[] userPassword = "12345678".toCharArray();

    @Test
    public void testCipher() throws Exception {
        PBECipher cipher = new PBECipher(pbePassword, pbeSalt, iterationCount, keySize);
        PasswordHash passwordHash = new PasswordHash(userPassword, iterationCount, keySize);
        byte[] iv = Base64.decode("xI87HaOKY5y9JIjMiqrtLg==");
        byte[] encryptedHash = cipher.encrypt(passwordHash.pack(), iv);
        byte[] decryptedHash = cipher.decrypt(encryptedHash, iv);
        assertTrue(Arrays.equals(decryptedHash, passwordHash.pack()));
        encryptedHash = cipher.encrypt(decryptedHash, iv);
        decryptedHash = cipher.decrypt(encryptedHash, iv);
        assertTrue(Arrays.equals(decryptedHash, passwordHash.pack()));
        assertTrue(new PasswordHash(decryptedHash).matches(userPassword));
        assertFalse(new PasswordHash(decryptedHash).matches("wrong".toCharArray()));        
    }

    @Test
    public void testPasswordHashEncryption() throws Exception {
        PBECipher cipher = new PBECipher(pbePassword, pbeSalt, iterationCount, keySize);
        PasswordHash passwordHash = new PasswordHash(userPassword, iterationCount, keySize);
        passwordHash.encryptSalt(cipher);
        passwordHash.decryptSalt(cipher);
        assertTrue(passwordHash.matches(userPassword));
        passwordHash.encryptSalt(cipher);
        passwordHash = new PasswordHash(passwordHash.pack());
        passwordHash.decryptSalt(cipher);
        assertTrue(passwordHash.matches(userPassword));
        assertTrue(!passwordHash.matches("wrong".toCharArray()));        
    }

    @Test(expected = AssertionError.class)
    public void testDoubleEncryptException() throws Exception {
        PBECipher cipher = new PBECipher(pbePassword, pbeSalt, iterationCount, keySize);
        PasswordHash passwordHash = new PasswordHash(userPassword, iterationCount, keySize);
        passwordHash.encryptSalt(cipher);
        passwordHash.encryptSalt(cipher);
    }

    @Test(expected = AssertionError.class)
    public void testDoubleDecryptException() throws Exception {
        PBECipher cipher = new PBECipher(pbePassword, pbeSalt, iterationCount, keySize);
        PasswordHash passwordHash = new PasswordHash(userPassword, iterationCount, keySize);
        passwordHash.encryptSalt(cipher);
        passwordHash.decryptSalt(cipher);
        passwordHash.decryptSalt(cipher);
    }
    
    @Test
    public void testDecrypt() throws Exception {
        PasswordHash passwordHash = new PasswordHash(userPassword, iterationCount, keySize);
        assertTrue(passwordHash.getIv().length == 0);
        byte[] hash = passwordHash.getHash();
        byte[] salt = passwordHash.getSalt();
        PBECipher cipher = new PBECipher(pbePassword, pbeSalt, iterationCount, keySize);                
        passwordHash.encryptSalt(cipher);
        assertTrue(passwordHash.getIv().length != 0);
        assertTrue(!Arrays.equals(salt, passwordHash.getSalt()));
        passwordHash.decryptSalt(cipher);
        assertTrue(passwordHash.getIv().length == 0);
        assertTrue(Arrays.equals(salt, passwordHash.getSalt()));
        assertTrue(Arrays.equals(hash, passwordHash.getHash()));
        passwordHash = new PasswordHash(passwordHash.pack());
        passwordHash.encryptSalt(cipher);
        passwordHash.decryptSalt(cipher);
        assertTrue(passwordHash.getIv().length == 0);
        assertTrue(Arrays.equals(hash, passwordHash.getHash()));
    }    
}
