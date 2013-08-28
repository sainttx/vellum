/*
 * Apache Software License 2.0, (c) Copyright 2013, Evan Summers
 * 
 */
package dualcontrol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;
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
 * @author evans
 */
public class DualControlReader {

    private final static Logger logger = Logger.getLogger(DualControlReader.class);
    private final static int PORT = 4444;
    private final static String HOST = "127.0.0.1";
    private final static String REMOTE_ADDRESS = "127.0.0.1";
    
    String purpose;
    int submissionCount;
    SSLContext sslContext;
    Set<String> names = new TreeSet();

    public static Map.Entry<String, char[]> readDualEntry(String purpose) throws Exception {
        return new DualControlReader().readDualMap(purpose, 2,
                DualControlSSLContextFactory.createSSLContext(VellumProperties.systemProperties)).
                entrySet().iterator().next();
    }

    public Map<String, char[]> readDualMap(String purpose, int submissionCount,
            SSLContext sslContext) throws Exception {
        this.purpose = purpose;
        this.submissionCount = submissionCount;
        this.sslContext = sslContext;
        logger.info("readDualMap submissionCount: " + submissionCount);
        logger.info("readDualMap purpose: " + purpose);
        Map<String, char[]> map = new TreeMap();
        Map<String, char[]> submissions = readMap();
        for (String name : submissions.keySet()) {
            for (String otherName : submissions.keySet()) {
                if (name.compareTo(otherName) < 0) {
                    String dualAlias = String.format("%s-%s", name, otherName);
                    char[] dualPassword = combineDualPassword(
                            submissions.get(name), submissions.get(otherName));
                    map.put(dualAlias, dualPassword);
                    logger.info("readDualMap dualAlias: " + dualAlias);
                }
            }
        }
        return map;
    }

    private static char[] combineDualPassword(char[] password, char[] other) {
        StringBuilder builder = new StringBuilder();
        builder.append(password);
        builder.append(other);
        return builder.toString().toCharArray();
    }

    private Map<String, char[]> readMap() throws Exception {
        logger.info("readMap SSL port " + PORT);
        SSLServerSocket serverSocket = (SSLServerSocket) sslContext.
                getServerSocketFactory().createServerSocket(PORT, submissionCount,
                InetAddress.getByName(HOST));
        serverSocket.setNeedClientAuth(true);
        return readMap(serverSocket);
    }

    private Map<String, char[]> readMap(SSLServerSocket serverSocket) throws Exception {
        Map<String, char[]> map = new TreeMap();
        while (map.size() < submissionCount) {
            SSLSocket socket = (SSLSocket) serverSocket.accept();
            if (!socket.getInetAddress().getHostAddress().equals(REMOTE_ADDRESS)) {
                logger.warn("Ignoring remote address "
                        + socket.getInetAddress().getHostAddress());
            } else {
                String username = new X500Name(socket.getSession().getPeerPrincipal().
                        getName()).getCommonName();
                if (names.contains(username)) {
                    logger.warn("Ignore duplicate " + username);
                } else {
                    names.add(username);
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                    dos.writeUTF(purpose);
                    DataInputStream dis = new DataInputStream(socket.getInputStream());
                    char[] password = readChars(dis);
                    String responseMessage = "Received " + username;
                    map.put(username, password);
                    if (true) {
                        responseMessage += " " + new Base32().encodeAsString(
                                Digests.sha1(Chars.getAsciiBytes(password)));
                    }
                    dos.writeUTF(responseMessage);
                    logger.info(responseMessage);
                }
            }
            socket.close();
        }
        serverSocket.close();
        return map;
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
