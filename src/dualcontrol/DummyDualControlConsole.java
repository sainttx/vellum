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
import java.net.Socket;
import javax.net.ssl.SSLContext;
import org.apache.log4j.Logger;

/**
 *
 * @author evan.summers
 */
public abstract class DummyDualControlConsole {

    final static Logger logger = Logger.getLogger(DummyDualControlConsole.class);
    final static int PORT = 4444;
    final static String HOST = "127.0.0.1";

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("usage: username passwd");
        } else {
            try {
                submit(DualControlSSLContextFactory.createSSLContext(
                        System.getProperties()), 
                        args[0], args[1].toCharArray());
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }
    
    public static void submit(SSLContext sslContext, 
            String username, char[] password) throws Exception {
        Socket socket = sslContext.getSocketFactory().
                createSocket(HOST, PORT);
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        String purpose = dis.readUTF();
        Log.infof(logger, "submit password for %s from %s", purpose, username);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        DualControlConsole.writeChars(dos, password);
        socket.close();
    }    
}
