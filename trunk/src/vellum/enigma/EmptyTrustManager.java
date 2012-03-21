/*
 * Copyright Evan Summers
 * 
 */
package vellum.enigma;

import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

/**
 *
 * @author evan
 */
public class EmptyTrustManager implements X509TrustManager {
    
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
    
    @Override
    public void checkClientTrusted(X509Certificate[] certs, String authType) {
    }
    
    @Override
    public void checkServerTrusted(X509Certificate[] certs, String authType) {
    }
}
