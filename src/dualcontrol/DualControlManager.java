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

import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import org.apache.log4j.Logger;
import sun.security.x509.X500Name;

/**
 *
 * @author evan.summers
 */
public class DualControlManager {

    private final static Logger logger = Logger.getLogger(DualControlManager.class);
    private final static int PORT = 4444;
    private final static String HOST = "127.0.0.1";
    private final static String REMOTE_ADDRESS = "127.0.0.1";
    private Properties properties;
    private String purpose;
    private int submissionCount;
    private SSLContext sslContext;
    private Set<String> names = new TreeSet();
    private Map<String, char[]> submissions = new TreeMap();
    private Map<String, char[]> dualMap = new TreeMap();

    public DualControlManager(Properties properties, int submissionCount, String purpose) {
        this.properties = properties;
        this.submissionCount = submissionCount;
        this.purpose = purpose;
    }

    public void init(SSLContext sslContent) {
        this.sslContext = sslContent;
    }

    public void init(MockableConsole console) throws Exception {
        init(DualControlSSLContextFactory.createSSLContext(properties, console));
    }

    public void init(Console console) throws Exception {
        init(new ConsoleAdapter(console));
    }
    
    public void call() throws Exception {
        logger.info("purpose: "  + purpose);
        SSLServerSocket serverSocket = (SSLServerSocket) sslContext.
                getServerSocketFactory().createServerSocket(PORT, submissionCount,
                InetAddress.getByName(HOST));
        try {
            serverSocket.setNeedClientAuth(true);
            read(serverSocket);
        } finally {
            serverSocket.close();
        }
        buildDualMap();
    }

    private void buildDualMap() {
        for (String name : submissions.keySet()) {
            for (String otherName : submissions.keySet()) {
                if (name.compareTo(otherName) < 0) {
                    String dualAlias = String.format("%s-%s", name, otherName);
                    char[] dualPassword = combineSplitPassword(
                            submissions.get(name), submissions.get(otherName));
                    dualMap.put(dualAlias, dualPassword);
                    logger.info("dualAlias: " + dualAlias);
                }
            }
        }
    }

    private static char[] combineSplitPassword(char[] password, char[] other) {
        StringBuilder builder = new StringBuilder();
        builder.append(password);
        builder.append('+');
        builder.append(other);
        return builder.toString().toCharArray();
    }

    private void read(SSLServerSocket serverSocket) throws Exception {
        while (submissions.size() < submissionCount) {
            SSLSocket socket = (SSLSocket) serverSocket.accept();
            try {
                if (!socket.getInetAddress().getHostAddress().equals(REMOTE_ADDRESS)) {
                    throw new Exception("Invalid remote address: "
                            + socket.getInetAddress().getHostAddress());
                }
                read(socket);
            } finally {
                socket.close();
            }
        }
    }

    private void read(SSLSocket socket) throws Exception {
        String name = getCN(socket.getSession().getPeerPrincipal().getName());
        if (names.contains(name)) {
            throw new Exception("Duplicate submission from " + name);
        }
        names.add(name);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF(purpose);
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        char[] password = readChars(dis);
        if (password.length == 0) {
            logger.warn("Empty password submission from " + name);
            return;
        }
        String responseMessage = "Received " + name;
        String invalidMessage = new DualControlPassphraseVerifier(properties).
                getInvalidMessage(password);
        if (invalidMessage != null) {
            throw new Exception(responseMessage + ": " + invalidMessage);
        }
        submissions.put(name, password);
        logger.info(responseMessage);
        responseMessage += " " + DualControlDigest.digestBase32(password).substring(1, 16);
        dos.writeUTF(responseMessage);
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

    public Map<String, char[]> getDualMap() {
        return dualMap;
    }

    public static Map.Entry<String, char[]> readDualEntry(String purpose) throws Exception {
        DualControlManager manager = new DualControlManager(System.getProperties(), 2, purpose);
        SSLContext sslContext = DualControlSSLContextFactory.createSSLContext(
                System.getProperties(), new ConsoleAdapter(System.console()));
        manager.init(sslContext);
        manager.call();
        return manager.getDualMap().entrySet().iterator().next();
    }
    
    public static String getCN(String dname) throws InvalidNameException {
        LdapName ln = new LdapName(dname);
        for (Rdn rdn : ln.getRdns()) {
            if (rdn.getType().equalsIgnoreCase("CN")) {
                return rdn.getValue().toString();
            }
        }
        throw new InvalidNameException(dname);
    }
    
}
