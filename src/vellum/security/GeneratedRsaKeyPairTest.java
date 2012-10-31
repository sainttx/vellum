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
    GeneratedRsaKeyPair rootKeyPair = new GeneratedRsaKeyPair();
    GeneratedRsaKeyPair clientKeyPair = new GeneratedRsaKeyPair();
    String rootDname = KeyStores.formatDname("localhost", "serverUnit", "serverOrg", "WP", "CT", "za");
    String clientDname = KeyStores.formatDname("123456", "clientUnit", "clientOrg", "WP", "CT", "za");
    
    private void test() throws Exception {
        rootKeyPair.generate(rootDname, new Date(), 999);
        clientKeyPair.generate(clientDname, new Date(), 999);
        logger.info(KeyStores.buildCertReqPem(clientKeyPair.getCertReq()));
        clientKeyPair.sign(rootKeyPair.getPrivateKey(), rootKeyPair.getCert());
        logger.info(KeyStores.buildKeyPem(clientKeyPair.getPrivateKey()));
        logger.info(KeyStores.buildCertPem(clientKeyPair.getCert()));
    }
    
    public static void main(String[] args) throws Exception {
        try {
            new GeneratedRsaKeyPairTest().test();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }    
}
