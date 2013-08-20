
package dualcontrol;

import java.util.Arrays;
import javax.crypto.SecretKey;
import org.apache.log4j.Logger;

/**
 *
 * @author evans
 */
public class DemoApp {
    private static final Logger logger = Logger.getLogger(DemoApp.class);
    private SecretKey dek; 
    
    public static void main(String[] args) throws Exception {
        logger.debug("main invoked with args: " + Arrays.toString(args));
        if (args.length != 3) {
            System.err.println("usage: keyStorePath storePass alias");
        } else {
            new DemoApp().loadKey(args[0], args[1].toCharArray(), args[2]);
        }
    }    
    
    private void loadKey(String keyStorePath, char[] storePass, String alias) throws Exception {
        dek = DualControlSessions.loadKey(keyStorePath, storePass, alias);
        logger.debug("loaded key " + dek.getAlgorithm());
    }
}

