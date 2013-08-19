package dualcontrol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.Logger;

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
                    map.put(String.format("%s-%s", name, otherName),
                            String.format("%s-%s", submissions.get(name), 
                            submissions.get(otherName)).toCharArray());
                }
            }
        }
        return map;
    }

    Map<String, char[]> readMap() throws Exception {
        Map<String, char[]> map = new TreeMap();
        for (byte[] bytes : readList()) {
            String string = new String(bytes).trim();
            String[] array = string.split(":");
            map.put(array[0], array[1].toCharArray());
            logger.debug("readMap " + array[0]);
        }
        return map;
    }

    List<byte[]> readList() throws Exception {
        logger.info("waiting for submissions on SSL port " + PORT);
        ServerSocket serverSocket = DualControlKeyStores.createSSLContext().
                getServerSocketFactory().createServerSocket(PORT, submissionCount, 
                InetAddress.getByName(LOCAL_ADDRESS));
        List<byte[]> list = new ArrayList();
        for (int i = 0; i < submissionCount; i++) {
            Socket socket = serverSocket.accept();
            if (!socket.getInetAddress().getHostAddress().equals(REMOTE_ADDRESS)) {
                throw new RuntimeException();
            }
            list.add(readBytes(socket.getInputStream()));
            socket.close();
        }
        serverSocket.close();
        return list;
    }

    public static byte[] readBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (true) {
            int b = inputStream.read();
            if (b < 0) {
                return baos.toByteArray();
            }
            baos.write(b);
        }
    }
}
