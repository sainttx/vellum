/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package crocserver.app;

import crocserver.storage.CrocStorage;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evan
 */
public class CrocTrustManager implements X509TrustManager {
    Logr logger = LogrFactory.getLogger(CrocTrustManager.class);
    CrocStorage storage;

    public CrocTrustManager(CrocStorage storage) {
        this.storage = storage;
    }
        
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
    
    @Override
    public void checkClientTrusted(X509Certificate[] certs, String authType) {
        logger.info("checkClientTrusted");
    }
    
    @Override
    public void checkServerTrusted(X509Certificate[] certs, String authType) {
        logger.info("checkServerTrusted");
    }
}
