/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package crocserver.app;

import crocserver.storage.clientcert.Cert;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import javax.net.ssl.X509TrustManager;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.security.Certificates;
import vellum.security.DefaultKeyStores;

/**
 *
 * @author evan
 */
public class CrocTrustManager implements X509TrustManager {

    Logr logger = LogrFactory.getLogger(CrocTrustManager.class);
    CrocApp app;
    X509TrustManager trustManager;

    public CrocTrustManager(CrocApp app) {
        this.app = app;
    }

    public void init() throws Exception {
        trustManager = DefaultKeyStores.loadTrustManager();
        for (X509Certificate acceptedIssuer : trustManager.getAcceptedIssuers()) {
            logger.trace("acceptedIssuer", acceptedIssuer.getSubjectDN().getName());
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        logger.info("getAcceptedIssuers", app.getServerCert().getSubjectDN().getName());
        return new X509Certificate[] {app.getServerCert()};
    }

    @Override
    public void checkClientTrusted(X509Certificate[] certs, String authType) {
        String dname = certs[0].getSubjectDN().getName();
        logger.info("checkClientTrusted " + dname);
        logger.info("server", app.getServerCert().getSubjectDN().getName());
        if (dname.equals(app.getServerCert().getSubjectDN().getName())) {
            return;
        }
        try {
            String cname = Certificates.getCommonName(dname);
            Cert cert = app.getStorage().getCertStorage().findName(cname);
            if (cert == null) {
                logger.info("cert not found", cname);
                throw new RuntimeException(dname);
            }
            logger.info("cert", cert.getSubject());
            if (!cert.isEnabled()) {
                throw new RuntimeException(dname);
            }
        } catch (SQLException e) {
            logger.warn(e, dname);
        }
    }

    @Override
    public void checkServerTrusted(X509Certificate[] certs, String authType) {
        logger.info("checkServerTrusted");
    }
}
