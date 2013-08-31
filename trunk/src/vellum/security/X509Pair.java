/*
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
 * 
 */
package vellum.security;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

/**
 *
 * @author evan.summers
 */
public class X509Pair {
    PrivateKey privateKey;
    X509Certificate certificate;

    public X509Pair(PrivateKey privateKey, X509Certificate certificate) {
        this.privateKey = privateKey;
        this.certificate = certificate;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public X509Certificate getCertificate() {
        return certificate;
    }      
}
