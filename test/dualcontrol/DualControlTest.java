/*
 * Source https://code.google.com/p/vellum by @evanxsummers

       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements. See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.  
       
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
import java.util.Properties;
import java.util.TreeMap;
import javax.crypto.SecretKey;
import javax.net.ssl.SSLContext;
import org.junit.Test;
import sun.security.x509.X500Name;
import vellum.security.GeneratedRsaKeyPair;
import vellum.util.Lists;
import vellum.util.Sockets;
import vellum.util.Threads;
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
    private final static int PORT = 4444;

    private KeyStore trustStore;
    private char[] keyStorePass = "test1234".toCharArray();
    private VellumProperties properties = new VellumProperties();
    private Map<String, char[]> dualPasswordMap = new TreeMap();
    private Map<String, KeyStore> keyStoreMap = new TreeMap();
    private Map<String, SSLContext> sslContextMap = new TreeMap();

    public DualControlTest() {
        properties.put("dualcontrol.verifyPassword", false);
        properties.put("alias", "dek2013");
        properties.put("storetype", "JCEKS");
        properties.put("keyalg", "AES");
        properties.put("keysize", "192");
    }
        
    @Test
    public void testGenKeyStore() throws Exception {
        dualPasswordMap.put("brent-evanx", "bbbb+eeee".toCharArray());
        dualPasswordMap.put("brent-henty", "bbbb+hhhh".toCharArray());
        dualPasswordMap.put("evanx-henty", "eeee+hhhh".toCharArray());
        MockConsole appConsole = new MockConsole("app", keyStorePass);
        DualControlGenSecKey instance = new DualControlGenSecKey(properties, appConsole);
        KeyStore keyStore = instance.createKeyStore(dualPasswordMap);
        assertEquals(3, Collections.list(keyStore.aliases()).size());
        assertEquals("dek2013-brent-evanx", Lists.asSortedSet(keyStore.aliases()).first());
        SecretKey key = getSecretKey(keyStore, "dek2013-brent-evanx", "bbbb+eeee".toCharArray());
        assertEquals("AES", key.getAlgorithm());
        assertTrue(Arrays.equals(key.getEncoded(), getSecretKey(keyStore, 
                "dek2013-brent-henty", "bbbb+hhhh".toCharArray()).getEncoded()));
    }

    @Test
    public void testGenSecKey() throws Exception {
        initSSL();
        assertNull(new DualControlPasswordVerifier(properties).
                getInvalidMessage("bbbb".toCharArray()));
        MockConsole appConsole = new MockConsole("app", keyStorePass);
        GenSecKeyThread genSecKeyThread = new GenSecKeyThread(
                new DualControlGenSecKey(properties, appConsole));
        System.out.print("app console: " + appConsole.getLine(0));
        SubmitterThread brentThread = createSubmitterThread("brent", "bbbb".toCharArray());
        SubmitterThread evanxThread = createSubmitterThread("evanx", "eeee".toCharArray());
        SubmitterThread hentyThread = createSubmitterThread("henty", "hhhh".toCharArray());
        Sockets.waitPort(PORT, 2000, 100);
        genSecKeyThread.start();
        brentThread.start();
        evanxThread.start();
        hentyThread.start();
        genSecKeyThread.join(2000);
        assertOk(genSecKeyThread.exception);
        assertOk(evanxThread.exception);
        assertOk(brentThread.exception);
        assertOk(hentyThread.exception);
        assertTrue(evanxThread.console.getLine(0).startsWith(
                "Enter password for new key dek2013:"));
        Threads.sleep(1000);
    }

    class GenSecKeyThread extends Thread  {
        DualControlGenSecKey genSecKey;
        KeyStore keyStore;
        Exception exception;

        public GenSecKeyThread(DualControlGenSecKey genSecKey) {
            this.genSecKey = genSecKey;
        }
        
        @Override
        public void run() {
            try {
                genSecKey.init(sslContextMap.get("app"));
                keyStore = genSecKey.createKeyStore();
            } catch (Exception e) {
                exception = e;
            }
        }
    }
    
    
    @Test
    public void testReader() throws Exception {
        initSSL();
        DualControlReader reader = new DualControlReader(properties, 2, "app");
        reader.init(sslContextMap.get("app"));
        DualReaderThread readerThread = new DualReaderThread(reader);
        SubmitterThread brentThread = createSubmitterThread("brent", "bbbb".toCharArray());
        SubmitterThread evanxThread = createSubmitterThread("evanx", "eeee".toCharArray());
        readerThread.start();
        brentThread.start();
        evanxThread.start();
        readerThread.join(2000);
        System.out.println("evanx console: " + evanxThread.console.getLine(0));
        assertOk(evanxThread.exception);
        assertOk(brentThread.exception);
        assertOk(readerThread.exception);
        assertTrue(evanxThread.console.getLine(0).startsWith("Enter password for app:"));
        assertEquals("brent-evanx", readerThread.dualEntry.getKey());
        assertEquals("bbbb+eeee", new String(readerThread.dualEntry.getValue()));
        Threads.sleep(1000);
    }

    private SubmitterThread createSubmitterThread(String alias, char[] password) {
        return new SubmitterThread(properties, new MockConsole(alias, password), 
                sslContextMap.get(alias));
        
    }
    
    private void assertOk(Exception e) throws Exception {
        if (e != null) {            
            throw e;
        }
    }
    
    private void initSSL() throws Exception {
        trustStore = KeyStore.getInstance("JKS");
        trustStore.load(null, keyStorePass);
        buildKeyStore("app");
        buildKeyStore("brent");
        buildKeyStore("evanx");
        buildKeyStore("henty");
        for (String name : keyStoreMap.keySet()) {
            sslContextMap.put(name, DualControlSSLContextFactory.createSSLContext(
                keyStoreMap.get(name), 
                keyStorePass, trustStore));
        }
    }    

    class SubmitterThread extends Thread  {
        MockConsole console;
        DualControlConsole dualControlConsole;
        Exception exception = null;
        
        public SubmitterThread(Properties properties, MockConsole console,
                SSLContext sslContext) {
            super();
            this.console = console;
            dualControlConsole = new DualControlConsole(properties, console);
            dualControlConsole.init(sslContext);
        }
        
        @Override
        public void run() {
            try {
                dualControlConsole.call();
            } catch (Exception e) {
                exception = e;
            }
        }
    }
    
    class DualReaderThread extends Thread  {
        DualControlReader reader;
        Map.Entry<String, char[]> dualEntry = null;
        Exception exception = null;

        public DualReaderThread(DualControlReader reader) {
            this.reader = reader;
        }

        
        @Override
        public void run() {
            try {
                dualEntry = reader.readDualMap().
                        entrySet().iterator().next();
            } catch (Exception e) {
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
