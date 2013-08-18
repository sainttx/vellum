package dualcontrol;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.crypto.SecretKey;
import org.apache.log4j.Logger;

/**
 *
 * @author evans
 */
public class DualControl {

    private final static Logger logger = Logger.getLogger(DualControl.class);
    private final static int PORT = 4444;
    private final static String REMOTE_ADDRESS = "127.0.0.1";
    private char[] keyPass;
    private String keyAlias;

    public void init() throws Exception {
        Map.Entry<String, String> entry = dualEntry();
        keyAlias = entry.getKey();
        keyPass = entry.getValue().toCharArray();
        logger.debug(String.format("init keyAlias %s, keyPass %s", keyAlias, new String(keyPass)));
    }

    public String getKeyAlias() {
        return keyAlias;
    }

    public void clear() {
        Arrays.fill(keyPass, (char) 0);
    }

    public SecretKey loadKey(String keystore, char[] storepass, String aliasPrefix) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        keyStore.load(new FileInputStream(keystore), storepass);
        logger.info(String.format("loadKey keystore %s, alias %s", keystore, aliasPrefix));
        return loadKey(keyStore, aliasPrefix);
    }

    public SecretKey loadKey(KeyStore keyStore, String alias) throws Exception {
        alias += "-" + keyAlias;
        logger.debug(String.format("alias %s, keypass %s", alias, new String(keyPass))); // TODO
        return (SecretKey) keyStore.getKey(alias, keyPass);
    }

    private Map.Entry<String, String> dualEntry() throws Exception {
        return dualMap(2).entrySet().iterator().next();
    }

    public Map<String, String> dualMap(int inputCount) throws Exception {
        Map<String, String> map = new TreeMap();
        Map<String, String> inputs = inputMap(inputCount);
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

    private static Map<String, String> inputMap(int inputCount) throws Exception {
        Map<String, String> map = new TreeMap();
        for (byte[] bytes : readInputs(inputCount)) {
            String string = new String(bytes).trim();
            String[] array = string.split(":");
            map.put(array[0], array[1]);
            logger.debug("input " + array[0]);
        }
        return map;
    }

    private static List<byte[]> readInputs(int inputCount) throws Exception {
        logger.info("waiting for info on SSL port " + PORT);
        return readInputs(DualControlContext.createSSLContext().getServerSocketFactory().
                createServerSocket(PORT), inputCount);
    }

    private static List<byte[]> readInputs(ServerSocket serverSocket, int inputCount) throws Exception {
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
