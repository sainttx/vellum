
package dualcontrol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.crypto.SecretKey;
import javax.net.ssl.SSLServerSocketFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author evans
 */
public abstract class DualControl {
    private static int PORT = 4444;
    private static Logger logger = Logger.getLogger(DualControl.class);
    public static String keyAlias;
    public static char[] keyPass;
    
    public static void init() throws Exception {
        Map.Entry<String, String> entry = DualControl.dualEntry();
        keyAlias = entry.getKey();
        keyPass = entry.getValue().toCharArray();
        logger.debug(String.format("init keyAlias %s, keyPass %s", keyAlias, new String(keyPass)));
    }
    
    public static SecretKey loadKey(KeyStore keyStore, String alias) throws Exception {
        alias += "-" + keyAlias;
        logger.debug(String.format("alias %s, keypass %s", alias, new String(keyPass)));
        return (SecretKey) keyStore.getKey(alias, keyPass);
    }    

    private static List<byte[]> readInputs(ServerSocket serverSocket, int n) throws Exception {
        List<byte[]> list = new ArrayList();
        for (int i = 0; i < n; i++) {
            Socket socket = serverSocket.accept();
            if (!socket.getInetAddress().getHostAddress().equals("127.0.0.1")) {
                throw new RuntimeException();
            }
            list.add(readBytes(socket.getInputStream()));
            socket.close();
        }
        serverSocket.close();
        return list;
    }

    public static List<byte[]> readInputs(int n) throws Exception {
        logger.info("waiting for info on SSL port " + PORT);
        SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        return readInputs(ssf.createServerSocket(PORT), n);
    }
    
    public static Map<String, String> mapInputs(int n) throws Exception {
        Map<String, String> map = new TreeMap();
        for (byte[] bytes : readInputs(n)) {
            String string = new String(bytes).trim();
            String[] array = string.split(":");
            map.put(array[0], array[1]);
            logger.debug("DualControl input " + array[0]);
        }
        return map;
    }
    
    public static Map<String, String> mapCombinations(int n) throws Exception {
        Map<String, String> map = new TreeMap();
        Map<String, String> inputs = mapInputs(n);
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
    
    public static Map.Entry<String, String> dualEntry() throws Exception {
        return DualControl.mapCombinations(2).entrySet().iterator().next();
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
