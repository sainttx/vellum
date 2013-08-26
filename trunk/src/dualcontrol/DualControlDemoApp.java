/*
 * Apache Software License 2.0, (c) Copyright 2013, Evan Summers
 * 
 */
package dualcontrol;

import java.util.Arrays;
import javax.crypto.SecretKey;
import org.apache.log4j.Logger;

/**
 *
 * @author evans
 */
public class DualControlDemoApp {
    private static final Logger logger = Logger.getLogger(DualControlDemoApp.class);
    private SecretKey dek; 
    
    public static void main(String[] args) throws Exception {
        logger.debug("main invoked with args: " + Arrays.toString(args));
        if (args.length != 3) {
            System.err.println("usage: keyStoreLocation storePass alias");
        } else {
            new DualControlDemoApp().loadKey(args[0], args[1].toCharArray(), args[2]);
        }
    }    
    
    private void loadKey(String keyStoreLocation, char[] keyStorePass, String alias) 
            throws Exception {
        dek = DualControlSessions.loadKey(keyStoreLocation, keyStorePass, alias,
                "DualControlDemoApp");
        logger.debug("loaded key " + dek.getAlgorithm());
    }
}

