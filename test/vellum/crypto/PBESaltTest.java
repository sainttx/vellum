/*
 * Copyright Evan Summers
 * 
 */
package vellum.crypto;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import static junit.framework.Assert.*;
import org.junit.Test;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evan
 */
public class PBESaltTest {

    private static Logr logger = LogrFactory.getLogger(PBESaltTest.class);
    private static final byte[] PBE_SALT = Base64.decode("ADgQEBAEAAj1GIDl10HHLVS36sp5J6PUHzF9xs/P1DgCABUBWw+OrJbyDEjrOrtWt7A1v0oE0T4=");
    int iterationCount = 1024;
    int keySize = 128;

    @Test
    public void testEncryptedSalt() throws Exception {
        char[] pbePassword = "sshssh".toCharArray();
        byte[] salt = PasswordSalts.nextSalt();
        PBECipher cipher = new PBECipher(pbePassword, salt, iterationCount, keySize);
        Encrypted encryptedSalt = cipher.encrypt(salt);
        PasswordHash packedSalt = new PasswordHash(
                Base64.encode(encryptedSalt.getEncryptedBytes()).toCharArray(), 
                salt, encryptedSalt.getIv(), iterationCount, keySize);
        assertTrue(PasswordHash.isPacked(packedSalt.pack()));
        System.out.println("More salt: " + Base64.encode(packedSalt.pack()));
    }

    @Test
    public void testCipher() throws Exception {
        char[] pbePassword = "sshssh".toCharArray();
        PasswordHash salt = new PasswordHash(PBE_SALT);
        assertEquals(iterationCount, salt.getIterationCount());
        assertEquals(keySize, salt.getKeySize());
        assertEquals(16, salt.getSalt().length);
        assertEquals(16, salt.getIv().length);
        assertTrue(verify(pbePassword, salt));
        assertTrue(verify(pbePassword, salt));
        assertTrue(!verify("sshsshh".toCharArray(), salt));
    }

    public boolean verify(char[] pbePassword, PasswordHash salt) 
            throws GeneralSecurityException, IOException {
        PBECipher cipher = new PBECipher(pbePassword, salt.getSalt(), 
                salt.getIterationCount(), salt.getKeySize());
        salt.encryptSalt(cipher);
        assertTrue(PasswordHash.isPacked(salt.pack()));
        salt = new PasswordHash(salt.pack());
        assertTrue(PasswordHash.isPacked(salt.pack()));
        salt.decryptSalt(cipher);        
        if (salt.matches(Base64.encode(cipher.encrypt(salt.getSalt(), salt.getIv())).toCharArray())) {
            return true;
        }
        return false;
    }
}
