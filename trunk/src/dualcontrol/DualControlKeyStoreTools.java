/*
 * Apache Software License 2.0, (c) Copyright 2013, Evan Summers
 * 
 */
package dualcontrol;

import org.apache.log4j.Logger;

/**
 *
 * @author evans
 */
public class DualControlKeyStoreTools {

    final static Logger logger = Logger.getLogger(DualControlKeyStoreTools.class);
    public static char[] getKeyStorePassword() {
        String storePasswordString = System.getProperty("storepass");
        if (storePasswordString != null) {
            return storePasswordString.toCharArray();
        } else {
            return System.console().readPassword("storepass: ");
        }
    }    
}
