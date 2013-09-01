/*
       Source https://code.google.com/p/vellum by @evanxsummers
 * 
 */
package dualcontrol;

import java.security.KeyStore;
import javax.crypto.SecretKey;
import org.junit.Test;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evan
 */
public class DualControlTest {

    static Logr logger = LogrFactory.getLogger(DualControlTest.class);

    char[] keyStorePass = "test1234".toCharArray();
    String dekAlias = "dek2013";
    String dekKeyStoreLocation = dekAlias;
    KeyStore dekKeyStore;
    SecretKey dek;
    KeyStore appKeyStore;
    KeyStore brentKeyStore;
    KeyStore evanxKeyStore;
    KeyStore hentyKeyStore;
    KeyStore travsKeyStore;

    public DualControlTest() {
    }
        
    @Test
    public void test() throws Exception {
    }
    
    public void loadKey() throws Exception {
        initSSLKeyStores();
        dek = DualControlSessions.loadKey(dekKeyStoreLocation, keyStorePass, dekAlias, 
                "DualControlTest");
        logger.info("loadKey " + dek.getAlgorithm());
    }

    private void initSSLKeyStores() throws Exception {
        appKeyStore = createSSLKeyStore("app");
        brentKeyStore = createSSLKeyStore("brent");
        evanxKeyStore = createSSLKeyStore("evanx");
        hentyKeyStore = createSSLKeyStore("henty");
        travsKeyStore = createSSLKeyStore("travs");
    }    
    
    private KeyStore createSSLKeyStore(String name) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, keyStorePass);
        return keyStore;
    }    
}
