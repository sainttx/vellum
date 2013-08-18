package dualcontrol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private final static String REMOTE_ADDRESS = "127.0.0.1";
    
    static Map.Entry<String, String> readDualEntry() throws Exception {
        return readDualMap(2).entrySet().iterator().next();
    }

    public static Map<String, String> readDualMap(int inputCount) throws Exception {
        Map<String, String> map = new TreeMap();
        Map<String, String> inputs = readInputMap(inputCount);
        for (String name : inputs.keySet()) {
            for (String otherName : inputs.keySet()) {
                if (name.compareTo(otherName) < 0) {
                    map.put(String.format("%s-%s", name, otherName),
                            String.format("%s-%s", inputs.get(name), inputs.get(otherName)));
                }
            }
        }
        return map;
    }

    static Map<String, String> readInputMap(int inputCount) throws Exception {
        Map<String, String> map = new TreeMap();
        for (byte[] bytes : readInputs(inputCount)) {
            String string = new String(bytes).trim();
            String[] array = string.split(":");
            map.put(array[0], array[1]);
            logger.debug("input " + array[0]);
        }
        return map;
    }

    static List<byte[]> readInputs(int inputCount) throws Exception {
        logger.info("waiting for info on SSL port " + PORT);
        return readInputs(DualControlKeyStores.createSSLContext().getServerSocketFactory().
                createServerSocket(PORT), inputCount);
    }

    static List<byte[]> readInputs(ServerSocket serverSocket, int inputCount) throws Exception {
        List<byte[]> list = new ArrayList();
        for (int i = 0; i < inputCount; i++) {
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
