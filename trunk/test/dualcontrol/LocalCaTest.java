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

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Date;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import org.apache.log4j.Logger;
import org.junit.Test;
import sun.security.pkcs.PKCS10;

/**
 *
 * @author evan
 */
public class LocalCaTest {

    static Logger logger = Logger.getLogger(LocalCaTest.class);
    private int port = 4446;
    private char[] pass = "test1234".toCharArray();
    GenRsaPair caPair;
    X509Certificate caCert;
    KeyStore caTrustStore;
    GenRsaPair serverPair;
    X509Certificate serverCert;
    KeyStore serverKeyStore;
    SSLContext serverContext;
    PKCS10 serverCertRequest;
    GenRsaPair clientPair;
    SSLContext clientContext;
    KeyStore clientKeyStore;
    X509Certificate clientCert;
    KeyStore signedServerKeyStore;
    KeyStore signedServerTrustStore;
    X509Certificate signedServerCert;
    SSLContext signedServerContext;

    public LocalCaTest() {
    }

    @Test
    public void test() throws Exception {
        caPair = new GenRsaPair();
        caPair.generate("CN=ca, OU=test", new Date(), 365);
        caCert = caPair.getCertificate();
        caTrustStore = createTrustStore("ca", caCert);
        serverPair = new GenRsaPair();
        serverPair.generate("CN=server, OU=test", new Date(), 365);
        serverCert = serverPair.getCertificate();
        serverKeyStore = createKeyStore("server", serverPair);
        serverContext = SSLContexts.create(serverKeyStore, pass, clientKeyStore);
        serverCertRequest = serverPair.getCertRequest("CN=server, OU=test");
        signedServerCert = RsaSigner.signCert(caPair.getPrivateKey(),
                caPair.getCertificate(), serverCertRequest, new Date(), 365, 1234);
        signedServerKeyStore = createKeyStore("server", serverPair.getPrivateKey(),
                signedServerCert, caCert);
        signedServerContext = SSLContexts.create(signedServerKeyStore, pass, caTrustStore);
        signedServerTrustStore = createTrustStore("server", signedServerCert);
        signedServerKeyStore.store(new FileOutputStream("/home/evans/tmp/server.jks"), pass);
        signedServerTrustStore.store(new FileOutputStream("/home/evans/tmp/server.trust.jks"), pass);
        SSLServerSocket serverSocket = (SSLServerSocket) signedServerContext.
                getServerSocketFactory().createServerSocket(port);
        serverSocket.setNeedClientAuth(false);
        if (false) {
            logger.info("accept");
            Socket socket = serverSocket.accept();
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            dis.read();
            close(socket);
            close(serverSocket);
        }
    }

    private void initClient() throws Exception {
        clientPair = new GenRsaPair();
        clientPair.generate("CN=evanx, OU=test", new Date(), 365);
        clientKeyStore = createKeyStore("evanx", clientPair);
        clientCert = (X509Certificate) clientKeyStore.getCertificate("evanx");
        clientContext = SSLContexts.create(clientKeyStore, pass, serverKeyStore);
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

    public static void close(Socket socket) {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException ioe) {
                logger.warn(ioe.getMessage());
            }
        }
    }

    public static void close(ServerSocket serverSocket) {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException ioe) {
                logger.warn(ioe.getMessage());
            }
        }
    }
}
