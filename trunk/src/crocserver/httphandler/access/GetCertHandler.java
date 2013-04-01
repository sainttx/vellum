
/*
 * Apache Software License 2.0, (c) Copyright 2012 Evan Summers, 2010 iPay (Pty) Ltd
 */
package crocserver.httphandler.access;

import crocserver.exception.CrocExceptionType;
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
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evans
 */
public class GetCertHandler implements HttpHandler {
    Logr logger = LogrFactory.getLogger(getClass());
    CrocApp app;
    HttpExchange httpExchange;
    HttpExchangeInfo httpExchangeInfo;
    PrintStream out;

    String userName;
    String orgName;
    String certName;
    
    public GetCertHandler(CrocApp app) {
        super();
        this.app = app;
    }
    
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        this.httpExchange = httpExchange;
        httpExchangeInfo = new HttpExchangeInfo(httpExchange);
        logger.info("handle", httpExchangeInfo.getPath());
        if (httpExchangeInfo.getPathArgs().length < 4) {
            httpExchangeInfo.handleError(CrocExceptionType.INVALID_ARGS);
        } else {
            userName = httpExchangeInfo.getPathString(1);
            orgName = httpExchangeInfo.getPathString(2);
            certName = httpExchangeInfo.getPathString(3);
            logger.info("handle", userName, orgName, certName);
            try {
                handle();
            } catch (Exception e) {
                httpExchangeInfo.handleError(e);
            }
        }
        httpExchange.close();
    }
    
    private void handle() throws Exception {
        AdminUser user = app.getStorage().getUserStorage().get(userName);
        Org org = app.getStorage().getOrgStorage().get(orgName);
        app.getStorage().getOrgRoleStorage().verifyRole(user, org, AdminUserRole.SUPER);
        Cert cert = app.getStorage().getCertStorage().findName(certName);
        if (cert == null) {
            httpExchangeInfo.handleError(CrocExceptionType.NOT_FOUND);
        } else {
            httpExchangeInfo.sendResponse("application/x-pem-file",
                    cert.getCert().getBytes());
        }
    }    
}
