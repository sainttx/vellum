package dualcontrol;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.String;
import java.net.InetAddress;
import java.util.Map;
import java.util.TreeMap;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import org.apache.log4j.Logger;
import sun.security.x509.X500Name;

/**
 *
 * @author evans
 */
public class DualControlReader {
    private final static Logger logger = Logger.getLogger(DualControlReader.class);
    
    private final static int PORT = 4444;
    private final static String LOCAL_ADDRESS = "127.0.0.1";
    private final static String REMOTE_ADDRESS = "127.0.0.1";
    
    int submissionCount;
    
    static Map.Entry<String, char[]> readDualEntry() throws Exception {
        return new DualControlReader().readDualMap(2).entrySet().iterator().next();
    }

    public Map<String, char[]> readDualMap(int submissionCount) throws Exception {
        this.submissionCount = submissionCount;
        Map<String, char[]> map = new TreeMap();
        Map<String, char[]> submissions = readMap();
        for (String name : submissions.keySet()) {
            for (String otherName : submissions.keySet()) {
                if (name.compareTo(otherName) < 0) {
                    String dualAlias = String.format("%s-%s", name, otherName);
                    char[] dualPassword = combineDualPassword(submissions.get(name), submissions.get(otherName));
System.err.printf("readDualMap %s [%s]\n", dualAlias, new String(dualPassword));
                    map.put(dualAlias, dualPassword);
                }
            }
        }
        return map;
    }

    static char[] combineDualPassword(char[] password, char[] other) {
        char[] buffer = new char[password.length + other.length];
        int i = 0;
        for (char ch : password) {
            buffer[i++] = ch;
        }
        for (char ch : other) {
            buffer[i++] = ch;
        }
        for (i = 0; i < buffer.length; i++) {
            System.err.println("combined " + (int) buffer[i]);
        }
        return buffer;
    }
    
    Map<String, char[]> readMap() throws Exception {
        logger.info("Waiting for submissions on SSL port " + PORT);
        Map<String, char[]> map = new TreeMap();
        SSLServerSocket serverSocket = (SSLServerSocket) DualControlKeyStores.createSSLContext().
                getServerSocketFactory().createServerSocket(PORT, submissionCount, 
                InetAddress.getByName(LOCAL_ADDRESS));
        serverSocket.setNeedClientAuth(true);
        for (int i = 0; i < submissionCount; i++) {
            SSLSocket socket = (SSLSocket) serverSocket.accept();
            if (!socket.getInetAddress().getHostAddress().equals(REMOTE_ADDRESS)) {
                throw new RuntimeException();
            }
            String username = new X500Name(socket.getSession().getPeerPrincipal().
                    getName()).getCommonName();
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            char[] chars = dis.readUTF().toCharArray();
            if (true) { // TODO remove
                String[] fields = new String(chars).split(":");
                if (fields.length > 1) {
                    username = fields[0];
                    chars = fields[0].toCharArray();
                }
            }
            map.put(username, chars);
            socket.close();
        }
        serverSocket.close();
        return map;
    }
}
