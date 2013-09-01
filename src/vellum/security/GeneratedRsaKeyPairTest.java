/*
       Source https://code.google.com/p/vellum by @evanxsummers
 * 
 */
package vellum.security;

import java.util.Date;
import vellum.logr.Logr;
import vellum.logr.LogrFactory; 

/**
 *
 * @author evan.summers
 */
public class GeneratedRsaKeyPairTest {
    Logr logger = LogrFactory.getLogger(getClass());    
    GeneratedRsaKeyPair rootKeyPair = new GeneratedRsaKeyPair();
    GeneratedRsaKeyPair clientKeyPair = new GeneratedRsaKeyPair();
    String rootDname = Certificates.formatDname("localhost", "serverUnit", "serverOrg", "WP", "CT", "za");
    String clientDname = Certificates.formatDname("123456", "clientUnit", "clientOrg", "WP", "CT", "za");
    
    private void test() throws Exception {
        rootKeyPair.generate(rootDname, new Date(), 999);
        clientKeyPair.generate(clientDname, new Date(), 999);
        logger.info(Certificates.buildCertReqPem(clientKeyPair.getCertReq()));
        clientKeyPair.sign(rootKeyPair.getPrivateKey(), rootKeyPair.getCert());
        logger.info(Certificates.buildKeyPem(clientKeyPair.getPrivateKey()));
        logger.info(Certificates.buildCertPem(clientKeyPair.getCert()));
    }
    
    public static void main(String[] args) throws Exception {
        try {
            new GeneratedRsaKeyPairTest().test();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }    
}
