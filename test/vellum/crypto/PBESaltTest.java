/*
 * Copyright Evan Summers
 * 
 */
package vellum.crypto;

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
    private static final byte[] PBE_SALT = Base64.decode("AEgQECAEAAgqloJg9O5rq8+aauBInFWq3tpygjyEt3IXBE+tIW+3owhnwKA5khiUDsQqPYSaUrL7TVViWji7rYaoV8VjrHDQ");
    int iterationCount = 1024;
    int keySize = 128;

    @Test
    public void testEncryptedSalt() throws Exception {
        char[] pbePassword = "sshssh".toCharArray();
        byte[] salt = PasswordSalts.nextSalt();
        PBECipher cipher = new PBECipher(pbePassword, salt, iterationCount, keySize);
        Encrypted encryptedSalt = cipher.encrypt(salt);
        PBESalt packedSalt = new PBESalt(salt, iterationCount, keySize,
                encryptedSalt.getIv(), encryptedSalt.getEncryptedBytes());
        assertTrue(PBESalt.verifyBytes(packedSalt.getBytes()));
        System.out.println("More salt: " + Base64.encode(packedSalt.getBytes()));
    }

    @Test
    public void testCipher() throws Exception {
        char[] pbePassword = "sshssh".toCharArray();
        PBESalt salt = new PBESalt(PBE_SALT);
        assertEquals(iterationCount, salt.getIterationCount());
        assertEquals(keySize, salt.getKeySize());
        assertEquals(16, salt.getSalt().length);
        assertEquals(16, salt.getIv().length);
        assertEquals(32, salt.getEncryptedSalt().length);
        assertTrue(verify(pbePassword, salt));
        assertTrue(verify(pbePassword, salt));
        assertTrue(!verify("sshsshh".toCharArray(), salt));
    }

    public boolean verify(char[] pbePassword, PBESalt salt) throws GeneralSecurityException {
        PBECipher cipher = new PBECipher(pbePassword, salt.getSalt(), salt.getIterationCount(), salt.getKeySize());
        if (Arrays.equals(salt.getEncryptedSalt(), cipher.encrypt(salt.getSalt(), salt.getIv()))) {
            assert Arrays.equals(salt.getSalt(), cipher.decrypt(salt.getEncryptedSalt(), salt.getIv()));
            return true;
        }
        return false;
    }
}
