/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
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
