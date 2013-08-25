package dualcontrol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.util.Map;
import java.util.TreeMap;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
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
    
    String prompt;
    int submissionCount;
    
    static Map.Entry<String, char[]> readDualEntry(String prompt) throws Exception {
        return new DualControlReader().readDualMap(prompt, 2).entrySet().iterator().next();
    }

    public Map<String, char[]> readDualMap(String prompt, int submissionCount) throws Exception {
        this.prompt = prompt;
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
                DualControlKeyStores.createSSLContext().
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
                throw new RuntimeException("Remote host address excluded");
            }
            String username = new X500Name(socket.getSession().getPeerPrincipal().
                    getName()).getCommonName();
            logger.info("accepting " + username);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(prompt);
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            char[] password = dis.readUTF().toCharArray();
            String errorMessage = DualControlPasswords.getErrorMessage(password);
            if (errorMessage == null) {                
                map.put(username, password);
                dos.writeUTF("OK " + new String(Digests.sha1(Bytes.getBytes(password))));
                logger.info("OK " + new String(password));
            } else {
                dos.writeUTF(errorMessage);
                logger.warn(errorMessage);
            }
            socket.close();
        }
        serverSocket.close();
        return map;
    }
}
