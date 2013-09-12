/*
 Source https://code.google.com/p/vellum by @evanxsummers
 * 
 */
package vellum.cryptostore;

import static junit.framework.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.junit.Test;
import vellum.pbestore.RsaKeyStore;
import vellum.asymmetricstore.RsaStore;
import vellum.datatype.Millis;

/**
 *
 * @author evan
 */
public class RsaStoreTest {

    static Logger logger = Logger.getLogger(RsaStoreTest.class);
    
    char[] password = "test1234".toCharArray();
    String type = "adhoc";
    String alias = "test2013";
    int keySize = 2048;
    String text = "all your base all belong to us";
    
    @Test 
    public void testGenerate() throws Exception {
        testGenerate(1000);
        testGenerate(10000);
        testGenerate(100000);
    }
    
    public void testGenerate(int iterationCount) throws Exception {
        long millis = System.currentTimeMillis();
        RsaKeyStore ks = new RsaKeyStore();
        ks.generate(alias, keySize);
        ByteArrayOutputStream kos = new ByteArrayOutputStream();
        ks.storePublic(kos);
        ByteArrayInputStream kis = new ByteArrayInputStream(kos.toByteArray());
        PublicKey loadedPublicKey = ks.loadPublic(kis);
        System.out.printf("loaded public key %s %s: %s\n", alias, 
                loadedPublicKey.getAlgorithm(), 
                Base64.encodeBase64String(loadedPublicKey.getEncoded()));
        assertTrue("loaded public key", Arrays.equals(ks.getKeyPair().getPublic().getEncoded(),
                loadedPublicKey.getEncoded()));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new RsaStore().store(baos, type, alias, text.getBytes(), ks.getKeyPair().getPublic());
        millis = Millis.elapsed(millis);
        System.out.printf("store %s %d %dms: %s\n", alias, iterationCount, millis, text);
        millis = System.currentTimeMillis();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        kos = new ByteArrayOutputStream();
        ks.storePrivate(kos, password);
        kis = new ByteArrayInputStream(kos.toByteArray());        
        PrivateKey loadedPrivateKey = ks.loadPrivate(kis, alias, password);
        assertTrue("loaded private key", Arrays.equals(ks.getKeyPair().getPrivate().getEncoded(),
                loadedPrivateKey.getEncoded()));
        millis = Millis.elapsed(millis);
        System.out.printf("loaded private key %s %d %dms: %s\n", alias, iterationCount, 
                millis, loadedPrivateKey.getAlgorithm());
        millis = System.currentTimeMillis();
        byte[] loadBytes = new RsaStore().load(bais, type, alias, loadedPrivateKey);
        millis = Millis.elapsed(millis);
        System.out.printf("load %s %d %dms: %s\n", alias, iterationCount, millis, 
                new String(loadBytes));
        assertTrue("loaded bytes", Arrays.equals(loadBytes, text.getBytes()));
    }
}
