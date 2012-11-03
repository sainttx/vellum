/*
 * Copyright Evan Summers
 * 
 */
package vellumdemo.totp;

import java.util.Random;
import org.apache.commons.codec.binary.Base32;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evan
 */
public class Auth {
    Logr logger = LogrFactory.getLogger(getClass());
    static Auth tester = new Auth();
    
    void test() {
        byte[] buffer = new byte[10];
        new Random().nextBytes(buffer);
        String secret = new String(new Base32().encode(buffer));
        logger.info(secret);
        
    }
    
    public static void main(String[] args) {
        tester.test();
    }
    
    
}
