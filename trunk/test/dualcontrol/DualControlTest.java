/*
 * Source https://code.google.com/p/vellum by @evanxsummers
 * 
 */
package dualcontrol;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import static junit.framework.Assert.*;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import javax.crypto.SecretKey;
import org.junit.Test;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.util.Lists;

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
    Properties properties = new Properties();
    private Map<String, char[]> dualPasswordMap = new TreeMap();

    public DualControlTest() {
    }
        
    @Test
    public void test() throws Exception {
    }
    
    @Test
    public void genKeyTest() throws Exception {
        dualPasswordMap.put("brent-evanx", "bbbb+eeee".toCharArray());
        dualPasswordMap.put("brent-henty", "bbbb+hhhh".toCharArray());
        dualPasswordMap.put("evanx-henty", "eeee+hhhh".toCharArray());
        properties.put("alias", "dek2013");
        properties.put("storetype", "JCEKS");
        properties.put("keyalg", "AES");
        properties.put("keysize", "192");
        DualControlGenSecKey instance = new DualControlGenSecKey();
        KeyStore keyStore = instance.createKeyStore(properties, dualPasswordMap);
        assertEquals(3, Collections.list(keyStore.aliases()).size());
        assertEquals("dek2013-brent-evanx", Lists.asSortedSet(keyStore.aliases()).first());
        SecretKey key = getSecretKey(keyStore, "dek2013-brent-evanx", "bbbb+eeee".toCharArray());
        assertEquals("AES", key.getAlgorithm());
        assertTrue(Arrays.equals(key.getEncoded(), getSecretKey(keyStore, 
                "dek2013-brent-henty", "bbbb+hhhh".toCharArray()).getEncoded()));
    }

    @Test
    public void genConsole() throws Exception {
    }

    @Test
    public void genReader() throws Exception {
    }
    
    
    private SecretKey getSecretKey(KeyStore keyStore, String keyAlias, char[] keyPass) 
            throws GeneralSecurityException {
        KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) keyStore.getEntry(
                keyAlias, new KeyStore.PasswordProtection(keyPass));
        return entry.getSecretKey();
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
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair keyPair = generator.generateKeyPair();
        X509Certificate[] chain = new X509Certificate[1];
        keyStore.setKeyEntry(name, keyPair.getPrivate(), keyStorePass, chain);
        return keyStore;
    }    
}
