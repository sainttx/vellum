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
public class VellumKeyPairToolTest {
    Logr logger = LogrFactory.getLogger(getClass());    
    KeyPairGenerator tool = new KeyPairGenerator();
    
    private void test() throws Exception {
        tool.genKeyPair(KeyStores.LOCAL_DNAME, new Date(), 999, 1024);
        logger.info(KeyStores.buildPrivateKeyPem(tool.getPrivateKey()));
        logger.info(KeyStores.buildCertPem(tool.getCert()));
    }
    
    public static void main(String[] args) throws Exception {
        try {
            new VellumKeyPairToolTest().test();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
    
}
