/*
 * Apache Software License 2.0, (c) Copyright 2012 Evan Summers, 2010 iPay (Pty) Ltd
 */
package crocserver.httphandler.access;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import crocserver.app.CrocApp;
import crocserver.storage.adminuser.AdminUser;
import crocserver.storage.adminuser.AdminUserRole;
import crocserver.storage.clientcert.Cert;
import crocserver.storage.org.Org;
import vellum.httpserver.HttpExchangeInfo;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import java.util.Date;
import vellum.security.Certificates;
import vellum.security.DefaultKeyStores;
import vellum.security.GeneratedRsaKeyPair;

/**
 *
 * @author evans
 */
public class EnrollCertHandler implements HttpHandler {
    Logr logger = LogrFactory.getLogger(getClass());
    CrocApp app;
    HttpExchange httpExchange;
    HttpExchangeInfo httpExchangeInfo;

    String userName;
    String orgName;
    String certName;
 
    public EnrollCertHandler(CrocApp app) {
        super();
        this.app = app;
    }
    
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        this.httpExchange = httpExchange;
        httpExchangeInfo = new HttpExchangeInfo(httpExchange);
        logger.info("handle", getClass().getName(), httpExchangeInfo.getPathArgs());
        if (httpExchangeInfo.getPathArgs().length < 4) {
            httpExchangeInfo.handleError(CrocError.INVALID_ARGS);
        } else {
            userName = httpExchangeInfo.getPathString(1);
            orgName = httpExchangeInfo.getPathString(2);
            certName = httpExchangeInfo.getPathString(3);
            try {
                generate();
            } catch (Exception e) {
                httpExchangeInfo.handleError(e);
            }
        }
        httpExchange.close();
    }
    
    private void generate() throws Exception {
        AdminUser user = app.getStorage().getUserStorage().find(userName);
        Org org = app.getStorage().getOrgStorage().get(orgName);
        app.getStorage().getOrgRoleStorage().verifyRole(user, org, AdminUserRole.SUPER);
        logger.info("handle", user.getUserName(), org.getOrgName());
        GeneratedRsaKeyPair keyPair = new GeneratedRsaKeyPair();
        String dname = org.formatDname(certName, userName);
        keyPair.generate(dname, new Date(), 999);
        String alias = app.getServerKeyAlias();
        keyPair.sign(DefaultKeyStores.getPrivateKey(alias), DefaultKeyStores.getCert(alias));
        Cert cert = new Cert();
        cert.setName(certName);
        cert.setCert(keyPair.getCert());
        httpExchangeInfo.sendResponse("application/x-pem-file",
                Certificates.buildKeyPem(keyPair.getPrivateKey()).getBytes());

    }    
}
