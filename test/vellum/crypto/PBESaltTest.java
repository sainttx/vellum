/*
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
 * 
 */
package vellum.crypto;

import static junit.framework.Assert.*;
import org.junit.Test;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evan
 */
public class PBESaltTest {

    static Logr logger = LogrFactory.getLogger(PBESaltTest.class);
    private static final char[] PBE_PASSWORD = "ssh ssh".toCharArray();
    private static final int ITERATION_COUNT = 1024;
    private static final int KEY_SIZE = 128;
    private static final byte[] PBE_SALT = Base64.decode("ACgQEAAEAAj1CnAYfWSowhtQawWAR8A4zHVUZdcwzpcfQf9fTDFesA==");

    
 @Test
    public void testGenerate() throws Exception {
        PasswordHash pbeSalt = new PasswordHash(PBE_PASSWORD, ITERATION_COUNT, KEY_SIZE);
        assertTrue(PasswordHash.verifyBytes(pbeSalt.getBytes()));
        pbeSalt = new PasswordHash(pbeSalt.getBytes());
        assertTrue(pbeSalt.matches(PBE_PASSWORD));
        assertEquals(ITERATION_COUNT, pbeSalt.getIterationCount());
        assertEquals(KEY_SIZE, pbeSalt.getKeySize());
        System.out.println("packed PBE salt et al: " + Base64.encode(pbeSalt.getBytes()));
        verify(PBE_PASSWORD, pbeSalt.getBytes());
    }

    @Test
    public void testVerify() throws Exception {        
        verify(PBE_PASSWORD, PBE_SALT);
    }

    @Test(expected = AssertionError.class)
    public void testInvalidPasswordAssertion() throws Exception {        
        verify("wrong password".toCharArray(), PBE_SALT);
    }
    
    public void verify(char[] pbePassword, byte[] pbeSaltBytes) throws Exception {
        PasswordHash pbeSalt = new PasswordHash(pbeSaltBytes);
        PBECipher cipher = new PBECipher(pbePassword, pbeSalt);
        assertTrue(pbeSalt.matches(pbePassword));
        pbeSalt.encryptSalt(cipher);
        pbeSalt.decryptSalt(cipher);
        assertTrue(pbeSalt.matches(pbePassword));
    }          
}
