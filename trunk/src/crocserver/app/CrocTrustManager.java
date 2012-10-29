/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package crocserver.app;

import crocserver.storage.common.CrocStorage;
import crocserver.storage.servicecert.ClientCert;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.X509TrustManager;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.security.DefaultKeyStores;

/**
 *
 * @author evan
 */
public class CrocTrustManager implements X509TrustManager {

    Logr logger = LogrFactory.getLogger(CrocTrustManager.class);
    CrocStorage storage;
    X509TrustManager trustManager;

    public CrocTrustManager(CrocStorage storage) {
        this.storage = storage;
    }

    public void init() throws Exception {
        trustManager = DefaultKeyStores.loadTrustManager();
        for (X509Certificate acceptedIssuer : trustManager.getAcceptedIssuers()) {
            logger.info("acceptedIssuer", acceptedIssuer.getSubjectDN().getName());
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        logger.info("getAcceptedIssuers");
        return trustManager.getAcceptedIssuers();
    }

    @Override
    public void checkClientTrusted(X509Certificate[] certs, String authType) {
        String dname = certs[0].getSubjectDN().getName();
        logger.info("checkClientTrusted " + dname);
        try {
            ClientCert serviceCert = storage.getServiceCertStorage().findDname(dname);
            logger.info("serviceCert", serviceCert.getDname(), serviceCert.getClientName());
        } catch (SQLException e) {
            logger.warn(e, dname);
        }
    }

    @Override
    public void checkServerTrusted(X509Certificate[] certs, String authType) {
        logger.info("checkServerTrusted");
    }
}
