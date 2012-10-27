/*
 * Copyright Evan Summers
 * 
 */
package vellum.security;

import java.util.Date;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evan
 */
public class GeneratedRsaKeyPairTest {
    Logr logger = LogrFactory.getLogger(getClass());    
    GeneratedRsaKeyPair keyPair = new GeneratedRsaKeyPair();
    
    private void test() throws Exception {
        keyPair.generate(KeyStores.LOCAL_DNAME, new Date(), 999, 1024);
        logger.info(KeyStores.buildPrivateKeyPem(keyPair.getPrivateKey()));
        logger.info(KeyStores.buildCertPem(keyPair.getCert()));
    }
    
    public static void main(String[] args) throws Exception {
        try {
            new GeneratedRsaKeyPairTest().test();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }    
}
