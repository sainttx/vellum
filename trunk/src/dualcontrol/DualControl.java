
package dualcontrol;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.crypto.SecretKey;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author evans
 */
public class DualControl {
    static final String keyStorePath = System.getProperty("dualcontrol.ssl.keyStore");
    static final char[] keyStorePassword = System.getProperty("dualcontrol.ssl.keyStorePassword").toCharArray();
    static final char[] keyPassword = System.getProperty("dualcontrol.ssl.keyStorePassword").toCharArray();
    static final String trustStorePath = System.getProperty("dualcontrol.ssl.trustStore");
    static final char[] trustStorePassword = System.getProperty("dualcontrol.ssl.trustStorePassword").toCharArray();    
    private final static int PORT = 4444;
    private final static Logger logger = Logger.getLogger(DualControl.class);
    private static char[] keyPass;
    private static String keyAlias;
    
    public static void init() throws Exception {
        Map.Entry<String, String> entry = DualControl.dualEntry();
        keyAlias = entry.getKey();
        keyPass = entry.getValue().toCharArray();
        logger.debug(String.format("init keyAlias %s, keyPass %s", keyAlias, new String(keyPass)));
    }

    public static String getKeyAlias() {
        return keyAlias;
    }
        
    public static void clear() {
        Arrays.fill(keyPass, (char) 0);
    }
    
    public static SecretKey loadKey(String keystore, char[] storepass, String alias) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        keyStore.load(new FileInputStream(keystore), storepass);
        logger.info(String.format("loading key %s %s", keystore, alias));
        return loadKey(keyStore, alias);
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
        return readInputs(createSSLContext().getServerSocketFactory().
                createServerSocket(PORT), n);
    }
    
    public static Map<String, String> inputMap(int n) throws Exception {
        Map<String, String> map = new TreeMap();
        for (byte[] bytes : readInputs(n)) {
            String string = new String(bytes).trim();
            String[] array = string.split(":");
            map.put(array[0], array[1]);
            logger.debug("input " + array[0]);
        }
        return map;
    }
    
    public static Map<String, String> dualMap(int n) throws Exception {
        Map<String, String> map = new TreeMap();
        Map<String, String> inputs = inputMap(n);
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
        return DualControl.dualMap(2).entrySet().iterator().next();
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

    public static SSLContext createSSLContext() throws Exception {
        return createSSLContext(keyStorePath, keyStorePassword, keyPassword,
                trustStorePath, trustStorePassword);
    }

    public static SSLContext createSSLContext(String keyStorePath, 
            char[] keyStorePassword, char[] keyPassword,
            String trustStorePath, char[] trustStorePassword) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(keyStorePath), keyStorePassword);
        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(new FileInputStream(trustStorePath), trustStorePassword);
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, keyPassword);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        trustManagerFactory.init(trustStore);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), 
                trustManagerFactory.getTrustManagers(), new SecureRandom());
        return sslContext;
    }
    
}
