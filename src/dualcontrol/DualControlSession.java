package dualcontrol;

import static dualcontrol.DualControlKeyStores.logger;
import java.io.FileInputStream;
import java.net.Socket;
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

    private KeyStore dualKeyStore;
    private char[] dualPass;
    private String dualAlias;

    public void configure(String keyStorePath, char[] storePass) throws Exception {
        logger.debug("configure keyStore " + keyStorePath);
        this.dualKeyStore = DualControlKeyStores.loadKeyStore(keyStorePath, storePass);
        Map.Entry<String, String> entry = DualControlReader.readDualEntry();
        this.dualAlias = entry.getKey();
        this.dualPass = entry.getValue().toCharArray();
        logger.debug("configure alias " + dualAlias);
    }

    public void clear() {
        Arrays.fill(dualPass, (char) 0);
    }

    public SecretKey loadKey(String alias) throws Exception {
        alias += "-" + dualAlias;
        logger.debug("loadKey " + alias);
        return (SecretKey) dualKeyStore.getKey(alias, dualPass);
    }
}
