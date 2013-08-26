package dualcontrol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;
import java.util.TreeMap;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import org.apache.commons.codec.binary.Base32;
import org.apache.log4j.Logger;
import sun.security.x509.X500Name;
import vellum.security.Digests;
import vellum.util.Bytes;

/**
 *
 * @author evans
 */
public class DualControlReader {
    private final static Logger logger = Logger.getLogger(DualControlReader.class);
    
    private final static int PORT = 4444;
    private final static String LOCAL_ADDRESS = "127.0.0.1";
    private final static String REMOTE_ADDRESS = "127.0.0.1";
    
    String alias;
    int submissionCount;
    
    static Map.Entry<String, char[]> readDualEntry(String alias) throws Exception {
        return new DualControlReader().readDualMap(alias, 2).entrySet().iterator().next();
    }

    public Map<String, char[]> readDualMap(String alias, int submissionCount) throws Exception {
        this.alias = alias;
        this.submissionCount = submissionCount;
        Map<String, char[]> map = new TreeMap();
        Map<String, char[]> submissions = readMap();
        for (String name : submissions.keySet()) {
            for (String otherName : submissions.keySet()) {
                if (name.compareTo(otherName) < 0) {
                    String dualAlias = String.format("%s-%s", name, otherName);
                    char[] dualPassword = combineDualPassword(
                            submissions.get(name), submissions.get(otherName));
                    if (true) {
                        System.err.printf("INFO DualControlReader.readDualMap: %s, %s\n", 
                                dualAlias, new String(dualPassword));
                    }
                    map.put(dualAlias, dualPassword);
                }
            }
        }
        return map;
    }

    static char[] combineDualPassword(char[] password, char[] other) {
        StringBuilder builder = new StringBuilder();
        builder.append(password);
        builder.append(other);
        return builder.toString().toCharArray();
    }
    
    Map<String, char[]> readMap() throws Exception {
        logger.info("Waiting for submissions on SSL port " + PORT);
        SSLServerSocket serverSocket = (SSLServerSocket) 
                DualControlSSLContextFactory.createSSLContext().
                getServerSocketFactory().createServerSocket(PORT, submissionCount, 
                InetAddress.getByName(LOCAL_ADDRESS));
        serverSocket.setNeedClientAuth(true);
        return readMap(serverSocket);
    }
    
    Map<String, char[]> readMap(SSLServerSocket serverSocket) throws Exception {
        Map<String, char[]> map = new TreeMap();
        while (map.size() < submissionCount) {
            SSLSocket socket = (SSLSocket) serverSocket.accept();
            if (!socket.getInetAddress().getHostAddress().equals(REMOTE_ADDRESS)) {
                logger.warn("Ignoring remote address " + 
                        socket.getInetAddress().getHostAddress());
            } else {
                String username = new X500Name(socket.getSession().getPeerPrincipal().
                        getName()).getCommonName();
                logger.info("accepting " + username);
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF(alias);
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                char[] password = readChars(dis);
                String invalidMessage = 
                        DualControlPasswordVerifier.getInvalidMessage(password);
                if (invalidMessage != null) {
                    dos.writeUTF(invalidMessage);
                    logger.warn(invalidMessage);
                } else {
                    map.put(username, password);
                    String responseMessage = "OK " + username;
                    if (true) {
                        responseMessage += " " + new Base32().encodeAsString(
                                Digests.sha1(Bytes.getBytes(password)));
                        responseMessage += " " +  new String(password);
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
    
    public static char[] readChars(DataInputStream dis) throws IOException {
        char[] chars = new char[dis.readShort()];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = dis.readChar();
        }
        return chars;
    }
}
