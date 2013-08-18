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
        Map.Entry<String, String> entry = DualControlReader.readDualEntry();
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
}
