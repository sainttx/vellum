/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
 * 
 */
package vellum.security;

import java.security.PrivateKey;
import java.util.Date;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import sun.security.pkcs.PKCS10;
import sun.security.x509.CertAndKeyGen;
import sun.security.x509.X500Name;

/**
 *
 * @author evan.summers
 */
public class GeneratedRsaKeyPair {    
    String providerName = null;
    String keyAlgName = "RSA";
    String sigAlgName = "SHA1WithRSA";
    int keySize = 1024;
    Date startDate;
    int validityDays;
    CertAndKeyGen keyPair;
    PrivateKey privateKey;
    X509Certificate cert;
    PKCS10 certReq;
    
    public GeneratedRsaKeyPair() {        
    }

    public void generate(String dname, Date startDate, int validityDays) throws Exception {
        this.startDate = startDate;
        this.validityDays = validityDays;
        keyPair = new CertAndKeyGen(keyAlgName, sigAlgName, providerName);
        keyPair.generate(keySize);
        privateKey = keyPair.getPrivateKey();
        X500Name x500Name = new X500Name(dname);
        certReq = keyPair.getCertRequest(x500Name);
        cert = keyPair.getSelfCertificate(x500Name, startDate, TimeUnit.DAYS.toSeconds(validityDays));
    }

    public void sign(PrivateKey signerKey, X509Certificate signerCert) throws Exception {
        cert = Certificates.signCert(signerKey, signerCert, certReq, startDate, validityDays);
    }
    
    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PKCS10 getCertReq() {
        return certReq;
    }
    
    public X509Certificate getCert() {
        return cert;
    }    
}
