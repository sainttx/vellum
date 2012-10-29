/*
 * Copyright Evan Summers
 * 
 */
package vellum.security;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

/**
 *
 * @author evan
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
