
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
public class TestApp {
    public static SecretKey dek; 
    static Logger logger = Logger.getLogger(TestApp.class);
    
    public static void main(String[] args) throws Exception {
        DualControl.init();
        logger.info("main invoked with args: " + Arrays.toString(args));
        if (args.length >= 3) {
            dek = loadKey(args[0], args[1].toCharArray(), 
                    args[2] + "-" + DualControl.keyAlias, DualControl.keyPass);
            logger.info("loaded key " + dek.getAlgorithm());
        }
    }    
    
    public static SecretKey loadKey(String keystore, char[] storepass, String alias, char[] keypass)
            throws Exception {
        logger.info(String.format("loading key %s %s", keystore, alias));
        KeyStore ks = KeyStore.getInstance("JCEKS");
        ks.load(new FileInputStream(keystore), storepass);
        return (SecretKey) ks.getKey(alias, keypass);
    }
}
