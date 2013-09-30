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
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.junit.Test;
import sun.security.pkcs.PKCS10;

/**
 *
 * @author evan
 */
public class RevocableClientTrustManagerTest {

    static Logger logger = Logger.getLogger(RevocableClientTrustManagerTest.class);
    private int port = 4446;
    private char[] pass = "test1234".toCharArray();
    GenRsaKeyPair serverKeyPair;
    X509Certificate serverCert;
    KeyStore serverKeyStore;
    SSLContext serverContext;
    GenRsaKeyPair clientKeyPair;
    X509Certificate clientCert;
    PKCS10 certRequest;
    KeyStore signedKeyStore;
    X509Certificate signedCert;
    SSLContext signedContext;
    
    public RevocableClientTrustManagerTest() {
    }

    private void testServer() throws Exception {
        serverKeyPair = new GenRsaKeyPair("server");
        serverKeyPair.call("CN=server", new Date(), 1);
        serverCert = serverKeyPair.getCert();
        serverKeyStore = createSSLKeyStore(serverKeyPair);        
        Assert.assertEquals("CN=server", serverCert.getIssuerDN().getName());        
        Assert.assertEquals("CN=server", serverCert.getSubjectDN().getName());        
        Assert.assertEquals(1, Collections.list(serverKeyStore.aliases()).size());
        serverContext = createContext(serverKeyStore, "server", 1); 
        testConnectionException(serverContext, serverContext, 
                "Invalid cert chain length");
    }
    
    private void testClient() throws Exception {
        clientKeyPair = new GenRsaKeyPair("client");
        clientKeyPair.call("CN=client", new Date(), 1);
        KeyStore clientKeyStore = createSSLKeyStore(clientKeyPair);
        clientCert = (X509Certificate) clientKeyStore.getCertificate("client");
        Assert.assertEquals("CN=client", clientCert.getIssuerDN().getName());        
        Assert.assertEquals("CN=client", clientCert.getSubjectDN().getName());        
        Assert.assertEquals(1, Collections.list(clientKeyStore.aliases()).size());
        SSLContext clientContext = SSLContexts.create(clientKeyStore, pass, clientKeyStore);
        testConnectionOk(clientContext, clientContext);
    }
    
    private void testSigned() throws Exception {
        certRequest = RsaSigner.getCertRequest(clientKeyPair.getKeyPair(), "CN=client");
        signedCert = RsaSigner.signCert(serverKeyPair.getPrivateKey(),
                serverKeyPair.getCert(), certRequest, new Date(), 365, 1234);
        Assert.assertEquals("CN=server", signedCert.getIssuerDN().getName());
        Assert.assertEquals("CN=client", signedCert.getSubjectDN().getName());        
        signedKeyStore = createSSLKeyStore("client", clientKeyPair.getPrivateKey(), signedCert,
                serverKeyPair.getCert());
        Assert.assertEquals(2, Collections.list(signedKeyStore.aliases()).size());        
        signedContext = SSLContexts.create(signedKeyStore, pass,
                signedKeyStore);
        testConnectionOk(signedContext, signedContext);
    }
    
    private void testRevoked() throws Exception {        
        SSLContext revokedContext = createContext(serverKeyStore, "server", 
                signedCert.getSerialNumber());
        testConnectionException(revokedContext, signedContext, 
                "Certificate in revocation list");
    }

    private void testInvalidServerCertClient() throws Exception {
        KeyStore invalidKeyStore = createSSLKeyStore("client", clientKeyPair.getPrivateKey(), 
                signedCert, clientCert
                );
        SSLContext invalidContext = createContext(invalidKeyStore, "client", 1);
        testConnectionException(serverContext, invalidContext, 
                "Received fatal alert: certificate_unknown");
    }

    private void testInvalidServerCertOther() throws Exception {
        GenRsaKeyPair otherKeyPair = new GenRsaKeyPair("server");
        otherKeyPair.call("CN=server", new Date(), 1);
        KeyStore invalidKeyStore = createSSLKeyStore("client", clientKeyPair.getPrivateKey(), 
                signedCert, otherKeyPair.getCert()
                );
        SSLContext invalidContext = createContext(invalidKeyStore, "client", 1);
        testConnectionException(serverContext, invalidContext, 
                "Received fatal alert: certificate_unknown");
    }
    
    private SSLContext createContext(KeyStore keyStore, String keyAlias, Number revokedSerialNumber) 
            throws Exception {
        List<BigInteger> revocationList = new ArrayList();
        revocationList.add(new BigInteger(revokedSerialNumber.toString()));
        return SSLContexts.create(keyStore, keyAlias, pass, revocationList);
    }
    
    @Test
    public void test() throws Exception {
        testServer();
        testClient();
        testSigned();
        testRevoked();
        testInvalidServerCertClient();
        testInvalidServerCertOther();
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
                throw new Exception("testConnection exception: " + exception.getMessage());
            }
        } else {
            throw new Exception("testConnection accepted");
        }
    }

    private Exception testConnection(SSLContext serverContext, SSLContext clientContext)
            throws Exception {
        ServerThread serverThread = new ServerThread(serverContext);
        ClientThread clientThread = new ClientThread(clientContext);
        serverThread.start();
        clientThread.start();
        clientThread.join(2000);
        serverThread.join(2000);
        if (serverThread.exception != null) {
            return serverThread.exception;
        }
        if (clientThread.exception != null) {
            return clientThread.exception;
        }
        return null;
    }

    private KeyStore createSSLKeyStore(GenRsaKeyPair keyPair) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, null);
        X509Certificate[] chain = new X509Certificate[]{keyPair.getCert()};
        keyStore.setKeyEntry(keyPair.getAlias(), keyPair.getPrivateKey(), pass, chain);
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
