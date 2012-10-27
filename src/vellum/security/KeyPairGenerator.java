/*
 * Copyright Evan Summers
 * 
 */
package vellum.security;

import java.io.IOException;
import java.security.PrivateKey;
import java.util.Date;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import sun.security.x509.CertAndKeyGen;
import sun.security.x509.X500Name;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evan
 */
public class KeyPairGenerator {    
    String providerName = null;
    String keyAlgName = "RSA";
    String sigAlgName = "SHA1WithRSA";
    PrivateKey privateKey;
    X509Certificate cert;

    public KeyPairGenerator() {        
    }

    public void genKeyPair(String dname, Date startDate, int validityDays, int keySize) throws Exception {
        CertAndKeyGen keyPair = new CertAndKeyGen(keyAlgName, sigAlgName, providerName);
        keyPair.generate(keySize);
        privateKey = keyPair.getPrivateKey();
        X500Name x500Name = new X500Name(dname);
        cert = keyPair.getSelfCertificate(x500Name, startDate, TimeUnit.DAYS.toSeconds(validityDays));
    }

    public X509Certificate getCert() {
        return cert;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }    
    
}
