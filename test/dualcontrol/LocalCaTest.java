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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import javax.net.ssl.SSLContext;
import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.junit.Test;
import sun.security.pkcs.PKCS10;

/**
 *
 * @author evan
 */
public class LocalCaTest {

    private final static Logger logger = Logger.getLogger(LocalCaTest.class);
    private final int port = 4446;
    private char[] pass = "test1234".toCharArray();
    private SSLParams ca = new SSLParams("ca");
    private SSLParams server = new SSLParams("server");
    private SSLParams client = new SSLParams("client");
            
    class SSLParams {
        String alias;
        GenRsaPair pair;
        KeyStore keyStore;
        KeyStore trustStore;
        SSLContext sslContext;
        X509Certificate cert;
        PKCS10 certRequest;
        KeyStore signedKeyStore;
        X509Certificate signedCert;
        SSLContext signedContext;
        
        SSLParams(String alias) {
            this.alias = alias;
        }
        
        void init() throws Exception {
            pair = new GenRsaPair();
            pair.generate("CN=" + alias, new Date(), 365);
            cert = pair.getCertificate();
            keyStore = createKeyStore(alias, pair);
            certRequest = pair.getCertRequest("CN=" + alias);
        }

        void sign(SSLParams signer) throws Exception {
            signedCert = RsaSigner.signCert(signer.pair.getPrivateKey(),
                    signer.pair.getCertificate(), certRequest, new Date(), 365, 1234);
            signedKeyStore = createKeyStore(alias, pair.getPrivateKey(),
                    signedCert, signer.cert);
            signedKeyStore.store(createOutputStream(alias), pass);
        }
        
        void trust(X509Certificate trustedCert) throws Exception {
            trustStore = createTrustStore(alias, trustedCert);
            trustStore.store(createOutputStream(alias + ".trust"), pass);
            sslContext = SSLContexts.create(keyStore, pass, trustStore);
            signedContext = SSLContexts.create(signedKeyStore, pass,
                    trustStore);
        }        
    }
    
    public LocalCaTest() {
    }

    @Test
    public void test() throws Exception {
        init();
        server.trust(server.cert);
        testRevocation(server.keyStore, server.trustStore, client.signedKeyStore, 
                client.trustStore, client.signedCert);
    }

    private void init() throws Exception {
        ca.init();
        server.init();
        server.sign(ca);
        server.trust(ca.cert);
        client.init();
        client.sign(server);
        client.trust(server.cert);
    }
    
    private FileOutputStream createOutputStream(String alias) throws IOException {
        return new FileOutputStream(File.createTempFile(alias, "jks"));
    }

    private void testRevocation(KeyStore serverKeyStore, KeyStore serverTrustStore, 
            KeyStore clientKeyStore, KeyStore clientTrustStore, 
            X509Certificate revokedCert) throws Exception {
        Set<BigInteger> revokedSerialNumbers = new ConcurrentSkipListSet();
        SSLContext serverSSLContext = RevocableSSLContexts.createRevokedSerialNumbers(
                serverKeyStore, pass, serverTrustStore, revokedSerialNumbers);
        SSLContext clientSSLContext = SSLContexts.create(clientKeyStore, pass, clientTrustStore);
        ServerThread serverThread = new ServerThread();
        serverThread.start(serverSSLContext, port, 2);
        Assert.assertEquals("", ClientThread.connect(clientSSLContext, port));
        Assert.assertEquals("", serverThread.getErrorMessage());
        revokedSerialNumbers.add(revokedCert.getSerialNumber());
        Assert.assertEquals("", ClientThread.connect(clientSSLContext, port));
        Assert.assertEquals("", serverThread.getErrorMessage());
    }
    
    private KeyStore createKeyStore(String keyAlias, GenRsaPair keyPair) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, null);
        X509Certificate[] chain = new X509Certificate[]{keyPair.getCertificate()};
        keyStore.setKeyEntry(keyAlias, keyPair.getPrivateKey(), pass, chain);
        return keyStore;
    }

    private KeyStore createTrustStore(String alias, X509Certificate cert) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, null);
        keyStore.setCertificateEntry(alias, cert);
        return keyStore;
    }

    private KeyStore createKeyStore(String alias, PrivateKey privateKey,
            X509Certificate signed, X509Certificate ca) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);
        X509Certificate[] chain = new X509Certificate[] {signed, ca};
        keyStore.setKeyEntry(alias, privateKey, pass, chain);
        return keyStore;
    }
}
