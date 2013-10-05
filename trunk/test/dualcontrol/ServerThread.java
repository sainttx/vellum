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
import java.net.Socket;
import java.security.GeneralSecurityException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import junit.framework.Assert;
import vellum.util.Streams;

/**
 *
 * @author evan
 */
public class ServerThread extends Thread {

    private final SSLContext sslContext;
    private final int port;
    private Exception exception;

    public ServerThread(SSLContext sslContext, int port) {
        this.sslContext = sslContext;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            accept(sslContext, port);
        } catch (Exception e) {
            exception = e;
        }
    }

    static void accept(SSLContext sslContext, int port) throws Exception {
        SSLServerSocket serverSocket = (SSLServerSocket) sslContext.
                getServerSocketFactory().createServerSocket(port);
        try {
            serverSocket.setNeedClientAuth(true);
            handle(serverSocket.accept());
        } finally {
            serverSocket.close();
            Thread.sleep(100);
        }
    }

    static void handle(Socket socket) throws IOException {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            Assert.assertEquals("clienthello", dis.readUTF());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF("serverhello");
        } finally {
            socket.close();
        }
    }

    public Exception getException() {
        return exception;
    }
}
