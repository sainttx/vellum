/*
 * Copyright Evan Summers
 * 
 */
package vellum.crypto;

import org.junit.Test;
import static org.junit.Assert.*;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evan
 */
public class PBECipherTest {
    private static Logr logger = LogrFactory.getLogger(PBECipherTest.class);
    
    static final byte[] PBE_SALT = Base64.decode("nD++3Wv9h9MqnS3bO3KJzA==");
    
    @Test
    public void testCipher() throws Exception {
        char[] pbePassword = "sshssh".toCharArray();
        PBECipher cipher = new PBECipher(pbePassword, PBE_SALT);
        byte[] iv = Base64.decode("xI87HaOKY5y9JIjMiqrtLg==");
        byte[] encryptedSalt = cipher.encrypt(PBE_SALT, iv);
        assertEquals("w0T678gw6Ze3GWr09ebb/uDlT0oUkPkieh4I7w0smBU=", Base64.encode(encryptedSalt));
    }    
}
