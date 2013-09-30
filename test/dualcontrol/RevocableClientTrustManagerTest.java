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
    
    public RevocableClientTrustManagerTest() {
    }

    @Test
    public void test() throws Exception {
        GenRsaKeyPair serverKeyPair = new GenRsaKeyPair("server");
        serverKeyPair.call("CN=server", new Date(), 1);
        GenRsaKeyPair clientKeyPair = new GenRsaKeyPair("client");
        clientKeyPair.call("CN=client", new Date(), 1);
        KeyStore serverKeyStore = createSSLKeyStore(serverKeyPair);
        KeyStore clientKeyStore = createSSLKeyStore(clientKeyPair);
        X509Certificate clientCert = (X509Certificate) clientKeyStore.getCertificate("client");
        Assert.assertEquals("CN=client", clientCert.getIssuerDN().getName());
        PKCS10 certRequest = RsaSigner.getCertRequest(clientKeyPair.getKeyPair(), "CN=client"); 
        X509Certificate signedCert = RsaSigner.signCert(serverKeyPair.getPrivateKey(), 
                serverKeyPair.getCert(), certRequest, new Date(), 365, 1);
        clientKeyStore = createSSLKeyStore("client", clientKeyPair.getPrivateKey(), signedCert,
                serverKeyPair.getCert());
        clientCert = (X509Certificate) clientKeyStore.getCertificate("client");
        Assert.assertEquals("CN=server", clientCert.getIssuerDN().getName());
        List<BigInteger> revocationList = new ArrayList();
        logger.info("clientKeyStore: " + Collections.list(clientKeyStore.aliases()));
        SSLContext serverContext = SSLContexts.create(serverKeyStore, pass, 
                serverKeyStore, revocationList);
        SSLContext clientContext = SSLContexts.create(clientKeyStore, pass, 
                clientKeyStore);
        testConnection(serverContext, clientContext);
    }

    private void testConnection(SSLContext serverContext, SSLContext clientContext) 
            throws Exception {
        ServerThread serverThread = new ServerThread(serverContext);
        ClientThread clientThread = new ClientThread(clientContext);
        serverThread.start();
        clientThread.start();
        clientThread.join(2000);
        serverThread.join(2000);
        if (serverThread.exception != null) {
            throw new Exception("serverThread", serverThread.exception);
        }
        if (clientThread.exception != null) {
            throw new Exception("clientThread", clientThread.exception);
        }
    }
    
    private KeyStore createSSLKeyStore(GenRsaKeyPair keyPair) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, null);
        X509Certificate[] chain = new X509Certificate[] {keyPair.getCert()};
        keyStore.setKeyEntry(keyPair.getAlias(), keyPair.getPrivateKey(), pass, chain);
        return keyStore;
    }
    
    private KeyStore createSSLKeyStore(String alias, PrivateKey privateKey, 
            X509Certificate signedCert, X509Certificate issuer) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, null);
        X509Certificate[] chain = new X509Certificate[] {signedCert, issuer};
        keyStore.setCertificateEntry("issuer", issuer);
        keyStore.setKeyEntry(alias, privateKey, pass, chain);
        return keyStore;
    }
    
    
    class ServerThread extends Thread  {
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
    
    class ClientThread extends Thread  {
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
