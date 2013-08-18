package dualcontrol;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.Map;
import javax.crypto.SecretKey;
import org.apache.log4j.Logger;

/**
 *
 * @author evans
 */
public class DualControlSession {
    private final static Logger logger = Logger.getLogger(DualControlSession.class);
    
    private char[] keyPass;
    private String keyAlias;

    public void readDual() throws Exception {
        Map.Entry<String, String> entry = DualControlReader.readDualEntry();
        keyAlias = entry.getKey();
        keyPass = entry.getValue().toCharArray();
        logger.debug(String.format("init keyAlias %s, keyPass %s", keyAlias, new String(keyPass)));
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
