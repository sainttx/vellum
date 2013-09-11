/*
 * Source https://code.google.com/p/vellum by @evanxsummers
 * 
 */
package dualcontrol;

import java.security.GeneralSecurityException;
import static junit.framework.Assert.*;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import javax.crypto.SecretKey;
import javax.net.ssl.SSLContext;
import org.junit.Test;
import sun.security.x509.X500Name;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.security.GeneratedRsaKeyPair;
import vellum.util.Lists;
import vellum.util.VellumProperties;

/**
 *
 * @see MockConsole
 * @see DualControlConsole
 * @see DualControlReader
 * @see DualControlGenSecKey
 * 
 * @author evan
 */
public class DualControlTest {
    static Logr logger = LogrFactory.getLogger(DualControlTest.class);

    private KeyStore trustStore;
    private char[] keyStorePass = "test1234".toCharArray();
    private VellumProperties properties = new VellumProperties();
    private Map<String, char[]> dualPasswordMap = new TreeMap();
    private Map<String, KeyStore> keyStoreMap = new TreeMap();
    private Map<String, SSLContext> sslContextMap = new TreeMap();

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
    public void testReader() throws Exception {
        initSSL();
        DualReaderThread readerThread = new DualReaderThread();
        SubmitterThread brentThread = new SubmitterThread("brent", "bbbb".toCharArray());
        SubmitterThread evanxThread = new SubmitterThread("evanx", "eeee".toCharArray());
        readerThread.start();
        brentThread.start();
        evanxThread.start();
        readerThread.join(2000);
        assertTrue(evanxThread.console.getOutput().startsWith("Enter password for app:"));
        assertNull(evanxThread.exception);
        assertNull(brentThread.exception);
        assertNull(readerThread.exception);
        assertEquals("brent-evanx", readerThread.dualEntry.getKey());
        assertEquals("bbbb+eeee", new String(readerThread.dualEntry.getValue()));
    }

    private void initSSL() throws Exception {
        trustStore = KeyStore.getInstance("JKS");
        trustStore.load(null, keyStorePass);
        buildKeyStore("app");
        buildKeyStore("brent");
        buildKeyStore("evanx");
        for (String name : keyStoreMap.keySet()) {
            sslContextMap.put(name, DualControlSSLContextFactory.createSSLContext(
                keyStoreMap.get(name), 
                keyStorePass, trustStore));
        }
    }    

    class SubmitterThread extends Thread  {
        String alias;
        MockConsole console;
        Exception exception = null;
        
        public SubmitterThread(String alias, char[] password) {
            super(alias);
            this.alias = alias;
            this.console = new MockConsole(password);
        }
        
        @Override
        public void run() {
            try {
                DualControlConsole.call(properties, console, sslContextMap.get(alias));
            } catch (Exception e) {
                System.err.println(e.getMessage());
                exception = e;
            }
        }
    }
    
    class DualReaderThread extends Thread  {
        Map.Entry<String, char[]> dualEntry = null;
        Exception exception = null;
        
        @Override
        public void run() {
            try {
                dualEntry = DualControlReader.readDualEntry("app",
                        sslContextMap.get("app"));
            } catch (Exception e) {
                System.err.println(e.getMessage());
                exception = e;
            }
        }
    }
    
    private KeyStore buildKeyStore(String alias) throws Exception {
        KeyStore keyStore = createSSLKeyStore(alias, 1);
        X509Certificate cert = (X509Certificate) keyStore.getCertificate(alias);
        String dname = cert.getSubjectDN().getName();
        assertEquals(alias, new X500Name(dname).getCommonName());
        keyStoreMap.put(alias, keyStore);
        trustStore.setCertificateEntry(alias, cert);
        return keyStore;
    }
    
    private KeyStore createSSLKeyStore(String name, int validityDays) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, keyStorePass);
        GeneratedRsaKeyPair keyPair = new GeneratedRsaKeyPair();
        keyPair.generate("CN=" + name, new Date(), validityDays);
        X509Certificate[] chain = new X509Certificate[] {keyPair.getCert()};
        keyStore.setKeyEntry(name, keyPair.getPrivateKey(), keyStorePass, chain);
        return keyStore;
    }    
    
    private SecretKey getSecretKey(KeyStore keyStore, String keyAlias, char[] keyPass) 
            throws GeneralSecurityException {
        KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) keyStore.getEntry(
                keyAlias, new KeyStore.PasswordProtection(keyPass));
        return entry.getSecretKey();
    }
        
}
