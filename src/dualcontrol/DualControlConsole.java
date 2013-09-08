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

import vellum.util.VellumProperties;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import vellum.security.Digests;
import vellum.util.Chars;

/**
 *
 * @author evan.summers
 */
public class DualControlConsole {

    private final static int PORT = 4444;
    private final static String HOST = "127.0.0.1";

    public static void main(String[] args) throws Exception {
        Socket socket = DualControlSSLContextFactory.createSSLContext(
                System.getProperties()).getSocketFactory().
                createSocket(HOST, PORT);
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        String purpose = dis.readUTF();
        char[] password = System.console().readPassword(
                "Enter password for " + purpose + ": ");
        String invalidMessage = new DualControlPasswordVerifier(
                System.getProperties()).getInvalidMessage(password);
        if (invalidMessage != null) {
            System.err.println(invalidMessage);
        } else {
            String hash = Digests.sha1String(Chars.getBytes(password));
            Arrays.fill(password, (char) 0);
            password = System.console().readPassword(
                    "Re-enter password for " + purpose + ": ");
            if (!Digests.sha1String(Chars.getBytes(password)).equals(hash)) {
                System.err.println("Passwords don't match.");
            } else {
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                writeChars(dos, password);
                String message = dis.readUTF();
                System.console().writer().println(message);
            }
            Arrays.fill(password, (char) 0);
        }
        socket.close();
    }

    public static char[] writeChars(DataOutputStream dos, char[] chars) throws IOException {
        dos.writeShort(chars.length);
        for (int i = 0; i < chars.length; i++) {
            dos.writeChar(chars[i]);
        }
        return chars;
    }
}
