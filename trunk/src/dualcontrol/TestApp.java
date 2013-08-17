
package dualcontrol;

import java.util.Arrays;
import javax.crypto.SecretKey;
import org.apache.log4j.Logger;

/**
 *
 * @author evans
 */
public class TestApp {
    private static final Logger logger = Logger.getLogger(TestApp.class);
    private static SecretKey dek; 
    
    public static void main(String[] args) throws Exception {
        DualControl.init();
        logger.info("main invoked with args: " + Arrays.toString(args));
        if (args.length >= 3) {
            dek = DualControl.loadKey(args[0], args[1].toCharArray(), args[2]);
            logger.info("loaded key " + dek.getAlgorithm());
        }
    }    
    
}
