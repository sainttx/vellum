/*
 * Copyright Evan Summers
 * 
 */
package vellum.security;

import java.io.PrintStream;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.util.Date;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import sun.misc.BASE64Encoder;
import sun.security.provider.X509Factory;
import sun.security.x509.CertAndKeyGen;
import sun.security.x509.X500Name;

/**
 *
 * @author evan
 */
public class VellumKeyPairTool {
    String providerName = null;
    String keyAlgName = "RSA";
    String sigAlgName = "SHA1WithRSA";
    String dname = "CN=localhost, OU=local, O=local, L=local, S=local, C=local";
    int validity = 999;
    int keySize = 1024;
    Date startDate = new Date();
    PrivateKey privateKey;
    X509Certificate cert;

    public VellumKeyPairTool() {        
    }

    public VellumKeyPairTool(String dname) {
        this.dname = dname;
    }
    
    public void genKeyPair() throws Exception {
        CertAndKeyGen keyPair = new CertAndKeyGen(keyAlgName, sigAlgName, providerName);
        keyPair.generate(keySize);
        privateKey = keyPair.getPrivateKey();
        X500Name x500Name = new X500Name(dname);
        cert = keyPair.getSelfCertificate(x500Name, startDate, TimeUnit.DAYS.toSeconds(validity));
    }

    public void printKeyPair(PrintStream out) throws Exception {
        printPrivateKey(privateKey, out);
        printPem(cert, out);        
    }
    
    
    public void printPrivateKey(PrivateKey key, PrintStream out) throws Exception, CertificateException {
        BASE64Encoder encoder = new BASE64Encoder();
        out.println("-----BEGIN PRIVATE KEY-----");
        encoder.encodeBuffer(privateKey.getEncoded(), out);
        out.println("-----END PRIVATE KEY-----");
    }
    
    public void printPem(X509Certificate cert, PrintStream out) throws Exception, CertificateException {
        BASE64Encoder encoder = new BASE64Encoder();
        out.println(X509Factory.BEGIN_CERT);
        encoder.encodeBuffer(cert.getEncoded(), out);
        out.println(X509Factory.END_CERT);
    }
    
    public static void main(String[] args) throws Exception {
        try {
            new VellumKeyPairTool().genKeyPair();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
    
}
