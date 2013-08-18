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
    
    private char[] dualPass;
    private String dualAlias;

    public void readDual() throws Exception {
        Map.Entry<String, String> entry = DualControlReader.readDualEntry();
        dualAlias = entry.getKey();
        dualPass = entry.getValue().toCharArray();
        logger.debug(String.format("init keyAlias %s, keyPass %s", dualAlias, new String(dualPass)));
    }

    public void clear() {
        Arrays.fill(dualPass, (char) 0);
    }

    public SecretKey loadKey(String keystore, char[] storepass, String alias) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        keyStore.load(new FileInputStream(keystore), storepass);
        logger.info(String.format("loadKey keystore %s, alias %s", keystore, alias));
        return loadKey(keyStore, alias);
    }

    public SecretKey loadKey(KeyStore keyStore, String alias) throws Exception {
        alias += "-" + dualAlias;
        logger.debug(String.format("alias %s, keypass %s", alias, new String(dualPass))); // TODO
        return (SecretKey) keyStore.getKey(alias, dualPass);
    }
}
