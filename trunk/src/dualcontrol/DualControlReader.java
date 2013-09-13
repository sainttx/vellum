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
import java.net.InetAddress;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import org.apache.commons.codec.binary.Base32;
import org.apache.log4j.Logger;
import sun.security.x509.X500Name;
import vellum.security.Digests;
import vellum.util.Chars;

/**
 *
 * @author evan.summers
 */
public class DualControlReader {
    private final static Logger logger = Logger.getLogger(DualControlReader.class);
    private final static int PORT = 4444;
    private final static String HOST = "127.0.0.1";
    private final static String REMOTE_ADDRESS = "127.0.0.1";
    private Properties properties;
    private String purpose;
    private int submissionCount;
    private SSLContext sslContext;
    private Set<String> names = new TreeSet();
    private Map<String, char[]> submissions = new TreeMap();

    public DualControlReader(Properties properties, int submissionCount, String purpose) {
        this.properties = properties;
        this.submissionCount = submissionCount;
        this.purpose = purpose;
    }

    public void init(SSLContext sslContent) {
        this.sslContext = sslContent;
    }

    public Map<String, char[]> readDualMap(SSLContext sslContext) throws Exception {
        this.sslContext = sslContext;
        return readDualMap();
    }
    
    public Map<String, char[]> readDualMap() throws Exception {
        logger.info("readDualMap submissionCount: " + submissionCount);
        logger.info("readDualMap purpose: " + purpose);
        readSubmissions();
        Map<String, char[]> dualMap = new TreeMap();
        for (String name : submissions.keySet()) {
            for (String otherName : submissions.keySet()) {
                if (name.compareTo(otherName) < 0) {
                    String dualAlias = String.format("%s-%s", name, otherName);
                    char[] dualPassword = combineSplitPassword(
                            submissions.get(name), submissions.get(otherName));
                    dualMap.put(dualAlias, dualPassword);
                    logger.info("readDualMap dualAlias: " + dualAlias);
                }
            }
        }
        return dualMap;
    }

    private static char[] combineSplitPassword(char[] password, char[] other) {
        StringBuilder builder = new StringBuilder();
        builder.append(password);
        builder.append('+');
        builder.append(other);
        return builder.toString().toCharArray();
    }

    private void readSubmissions() throws Exception {
        logger.info("readSubmissions SSL port " + PORT);
        SSLServerSocket serverSocket = (SSLServerSocket) sslContext.
                getServerSocketFactory().createServerSocket(PORT, submissionCount,
                InetAddress.getByName(HOST));
        try {
            serverSocket.setNeedClientAuth(true);
            read(serverSocket);
        } finally {
            serverSocket.close();
        }
    }

    private void read(SSLServerSocket serverSocket) throws Exception {
        while (submissions.size() < submissionCount) {
            SSLSocket socket = (SSLSocket) serverSocket.accept();
            try {
                if (!socket.getInetAddress().getHostAddress().equals(REMOTE_ADDRESS)) {
                    throw new Exception("Invalid remote address "
                            + socket.getInetAddress().getHostAddress());
                }
                read(socket);
            } finally {
                socket.close();
            }
        }
    }
    
    private void read(SSLSocket socket) throws Exception {
        String name = new X500Name(socket.getSession().getPeerPrincipal().
                getName()).getCommonName();
        if (names.contains(name)) {
            throw new Exception("Duplicate submission from " + name);
        }
        names.add(name);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF(purpose);
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        char[] password = readChars(dis);
        String responseMessage = "Received " + name;
        String invalidMessage = new DualControlPasswordVerifier(properties).
                getInvalidMessage(password);
        if (invalidMessage != null) {
            throw new Exception(responseMessage + ": " + invalidMessage);
        }
        submissions.put(name, password);
        if (true) {
            responseMessage += " " + new Base32().encodeAsString(
                    Digests.sha1(Chars.getAsciiBytes(password)));
        }
        dos.writeUTF(responseMessage);
        logger.info(responseMessage);
    }

    public Set<String> getNames() {
        return names;
    }

    public static char[] readChars(DataInputStream dis) throws IOException {
        char[] chars = new char[dis.readShort()];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = dis.readChar();
        }
        return chars;
    }
}
