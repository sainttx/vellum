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
package localca;

import dualcontrol.DualControlManager;
import vellum.crypto.rsa.GenRsaPair;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.junit.Test;
import sun.security.pkcs.PKCS10;
import sun.security.validator.Validator;

/**
 *
 * @author evan
 */
public class RevocableClientTrustManagerTest {
    static Logger logger = Logger.getLogger(RevocableClientTrustManagerTest.class);
    
    private int port = 4446;
    private char[] pass = "test1234".toCharArray();
    GenRsaPair serverPair;
    X509Certificate serverCert;
    KeyStore serverKeyStore;
    SSLContext serverContext;
    GenRsaPair clientPair;
    SSLContext clientContext;
    KeyStore clientKeyStore;
    X509Certificate clientCert;
    PKCS10 certRequest;
    KeyStore signedKeyStore;
    X509Certificate signedCert;
    SSLContext signedContext;
    
    public RevocableClientTrustManagerTest() {
    }

    @Test
    public void test() throws Exception {
        serverPair = new GenRsaPair();
        serverPair.generate("CN=server", new Date(), 365);
        serverCert = serverPair.getCertificate();
        serverKeyStore = createKeyStore("server", serverPair);
        clientPair = new GenRsaPair();
        clientPair.generate("CN=client", new Date(), 365);
        clientKeyStore = createKeyStore("client", clientPair);
        clientCert = (X509Certificate) clientKeyStore.getCertificate("client");
        serverContext = SSLContexts.create(serverKeyStore, pass, clientKeyStore);
        clientContext = SSLContexts.create(clientKeyStore, pass, serverKeyStore);
        testConnection(serverContext, clientContext);
    }
    
    @Test
    public void testAll() throws Exception {
        initServer();
        initClient();
        testSigned();
        testRevoked();
        testInvalidServerCertOrder();
        testInvalidServerCertClient();
        testInvalidServerCertSigned();
        testInvalidServerCertOther();
    }

    private void initServer() throws Exception {
        serverPair = new GenRsaPair();
        serverPair.generate("CN=server", new Date(), 1);
        serverCert = serverPair.getCertificate();
        serverKeyStore = createKeyStore("server", serverPair);
        Assert.assertEquals("CN=server", serverCert.getIssuerDN().getName());
        Assert.assertEquals("CN=server", serverCert.getSubjectDN().getName());
        Assert.assertEquals(1, Collections.list(serverKeyStore.aliases()).size());
        serverContext = createContext(serverKeyStore, "revokedName"); 
        testConnectionException(serverContext, serverContext, 
                "Invalid cert chain length");
    }
    
    private void initClient() throws Exception {
        clientPair = new GenRsaPair();
        clientPair.generate("CN=client", new Date(), 1);
        clientKeyStore = createKeyStore("client", clientPair);
        clientCert = (X509Certificate) clientKeyStore.getCertificate("client");
        Assert.assertEquals("CN=client", clientCert.getIssuerDN().getName());
        Assert.assertEquals("CN=client", clientCert.getSubjectDN().getName());
        Assert.assertEquals(1, Collections.list(clientKeyStore.aliases()).size());
        SSLContext clientContext = SSLContexts.create(clientKeyStore, pass, clientKeyStore);
        testConnectionOk(clientContext, clientContext);
        testConnectionException(serverContext, clientContext, 
                "Received fatal alert: certificate_unknown");
    }
    
    private void testSigned() throws Exception {
        certRequest = clientPair.getCertRequest("CN=client");
        signedCert = Certificates.sign(serverPair.getPrivateKey(),
                serverPair.getCertificate(), certRequest, new Date(), 365, 1234);
        Assert.assertEquals("CN=server", signedCert.getIssuerDN().getName());
        Assert.assertEquals("CN=client", signedCert.getSubjectDN().getName());
        signedKeyStore = createSSLKeyStore("client", clientPair.getPrivateKey(), signedCert,
                serverPair.getCertificate());
        Assert.assertEquals(2, Collections.list(signedKeyStore.aliases()).size());
        signedContext = SSLContexts.create(signedKeyStore, pass,
                signedKeyStore);
        testConnectionOk(serverContext, signedContext);
        testConnectionOk(signedContext, signedContext);
    }
    
    private void testRevoked() throws Exception {        
        SSLContext revokedContext = createContext(serverKeyStore, 
                DualControlManager.getCN(signedCert.getSubjectDN()));
        testConnectionException(revokedContext, signedContext, 
                "Certificate CN revoked");
    }

    private void testInvalidServerCertClient() throws Exception {
        KeyStore invalidKeyStore = createSSLKeyStore("client", clientPair.getPrivateKey(), 
                signedCert, clientCert
                );
        SSLContext invalidContext = createContext(invalidKeyStore, null);
        testConnectionException(serverContext, invalidContext, 
                "Received fatal alert: certificate_unknown");
    }

    private void testInvalidServerCertOrder() throws Exception {
        KeyStore invalidKeyStore = createSSLKeyStore("client", clientPair.getPrivateKey(), 
                serverCert, signedCert
                );
        SSLContext invalidContext = createContext(invalidKeyStore, null);
        testConnectionException(serverContext, invalidContext, 
                "Invalid server certificate");
    }
    
    private void testInvalidServerCertSigned() throws Exception {
        KeyStore invalidKeyStore = createSSLKeyStore("client", clientPair.getPrivateKey(), 
                signedCert, signedCert
                );
        SSLContext invalidContext = createContext(invalidKeyStore, null);
        testConnectionException(serverContext, invalidContext, 
                "Received fatal alert: certificate_unknown");
    }
    
    private void testInvalidServerCertOther() throws Exception {
        GenRsaPair otherPair = new GenRsaPair();
        otherPair.generate("CN=server", new Date(), 1);
        KeyStore invalidKeyStore = createSSLKeyStore("client", clientPair.getPrivateKey(), 
                signedCert, otherPair.getCertificate()
                );
        SSLContext invalidContext = createContext(invalidKeyStore, null);
        testConnectionException(serverContext, invalidContext, 
                "Received fatal alert: certificate_unknown");
    }
    
    private SSLContext createContext(KeyStore keyStore, String revokedName) 
            throws Exception {
        Set<String> revocationList = new TreeSet();
        if (revokedName != null) {
            revocationList.add(revokedName);
        }
        KeyStore trustStore = keyStore;
        return RevocableSSLContexts.createRevokedNames(keyStore, pass, trustStore, 
                revocationList);
    }
    
    private void testConnectionOk(SSLContext serverContext, SSLContext clientContext)
            throws Exception {
        Exception exception = testConnection(serverContext, clientContext);
        if (exception != null) {
            throw exception;
        }
    }

    private void testConnectionException(SSLContext serverContext, SSLContext clientContext,
            String expectedExceptionMessage) throws Exception {
        Exception exception = testConnection(serverContext, clientContext);
        if (exception != null) {
            if (!exception.getMessage().contains(expectedExceptionMessage)) {
                logger.info("expected: " + expectedExceptionMessage);
                logger.error("got: %s" + exception.getMessage());
                throw new Exception("testConnectionException invalid");
            }
        } else {
            throw new Exception("testConnectionException expected exception");
        }
    }

    private Exception testConnection(SSLContext serverContext, SSLContext clientContext)
            throws Exception {
        ServerThread serverThread = new ServerThread(serverContext);
        ClientThread clientThread = new ClientThread(clientContext);
        serverThread.start();
        clientThread.start();
        clientThread.join(1000);
        serverThread.join(1000);
        if (serverThread.exception != null) {
            return serverThread.exception;
        }
        if (clientThread.exception != null) {
            return clientThread.exception;
        }
        return null;
    }

    private KeyStore createKeyStore(String keyAlias, GenRsaPair keyPair) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, null);
        X509Certificate[] chain = new X509Certificate[]{keyPair.getCertificate()};
        keyStore.setKeyEntry(keyAlias, keyPair.getPrivateKey(), pass, chain);
        return keyStore;
    }

    private KeyStore createSSLKeyStore(String alias, PrivateKey privateKey,
            X509Certificate signedCert, X509Certificate issuer) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, null);
        X509Certificate[] chain = new X509Certificate[]{signedCert, issuer};
        keyStore.setCertificateEntry("issuer", issuer);
        keyStore.setKeyEntry(alias, privateKey, pass, chain);
        return keyStore;
    }

    private KeyStore createTrustStore(String alias, X509Certificate cert) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, null);
        X509Certificate[] chain = new X509Certificate[]{cert};
        keyStore.setCertificateEntry(alias, cert);
        return keyStore;
    }
    
    private void testValidator() {
        Validator validator = Validator.getInstance(Validator.TYPE_SIMPLE,
                Validator.VAR_GENERIC, serverKeyStore);
    }
    
    class ServerThread extends Thread {

        SSLContext sslContext;
        Exception exception;

        public ServerThread(SSLContext sslContext) {
            this.sslContext = sslContext;
        }

        @Override
        public void run() {
            SSLServerSocket serverSocket = null;
            SSLSocket clientSocket = null;
            try {
                serverSocket = (SSLServerSocket) sslContext.getServerSocketFactory().
                        createServerSocket(port);
                serverSocket.setNeedClientAuth(true);
                clientSocket = (SSLSocket) serverSocket.accept();
                DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                Assert.assertEquals("clienthello", dis.readUTF());
                DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
                dos.writeUTF("serverhello");
                clientSocket.close();
                serverSocket.close();
                Thread.sleep(500);
            } catch (Exception e) {
                exception = e;
                close(clientSocket);
                close(serverSocket);
            }
        }
    }

    class ClientThread extends Thread {

        SSLContext sslContext;
        Exception exception;

        public ClientThread(SSLContext sslContext) {
            this.sslContext = sslContext;
        }

        @Override
        public void run() {
            SSLSocket clientSocket = null;
            try {
                Thread.sleep(500);
                clientSocket = (SSLSocket) sslContext.getSocketFactory().
                        createSocket("localhost", port);
                DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
                dos.writeUTF("clienthello");
                DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                Assert.assertEquals("serverhello", dis.readUTF());
                clientSocket.close();
            } catch (Exception e) {
                exception = e;
                close(clientSocket);
            }
        }
    }

    private void close(Socket socket) {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException ioe) {
                logger.warn(ioe.getMessage());
            }
        }
    }

    private void close(ServerSocket serverSocket) {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException ioe) {
                logger.warn(ioe.getMessage());
            }
        }
    }
}
