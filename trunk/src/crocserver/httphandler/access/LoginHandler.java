/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package crocserver.httphandler.access;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import crocserver.app.CrocApp;
import crocserver.app.CrocSecurity;
import crocserver.app.GoogleUserInfo;
import crocserver.httpserver.HttpExchangeInfo;
import crocserver.storage.adminuser.AdminRole;
import crocserver.storage.adminuser.AdminUser;
import java.io.IOException;
import java.io.PrintStream;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.util.Strings;

/**
 *
 * @author evans
 */
public class LoginHandler implements HttpHandler {

    Logr logger = LogrFactory.getLogger(getClass());
    CrocApp app;
    HttpExchange httpExchange;
    HttpExchangeInfo httpExchangeInfo;
    PrintStream out;

    public LoginHandler(CrocApp app) {
        super();
        this.app = app;
    }
    
    String userId;
    String accessToken;
    
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        this.httpExchange = httpExchange;
        httpExchangeInfo = new HttpExchangeInfo(httpExchange);
        logger.info("handle", getClass().getSimpleName(), httpExchangeInfo.getPath(), httpExchangeInfo.getParameterMap());
        if (httpExchangeInfo.getPath().length() == 0) {
            httpExchange.close();
            return;
        }
        if (httpExchangeInfo.getPathLength() == 1) {
            accessToken = httpExchangeInfo.getInputString();
            logger.info("input", userId, accessToken);
            try {
                if (accessToken != null) {
                    handle();
                } else {
                    httpExchangeInfo.handleError("require access_token");
                }
            } catch (Exception e) {
                httpExchangeInfo.handleException(e);
            }
        } else {
            httpExchangeInfo.handleError();
        }
        httpExchange.close();
    }
    
    GoogleUserInfo userInfo;    
    
    private void handle() throws Exception {
        userInfo = app.getGoogleApi().getUserInfo(accessToken);
        logger.info("userInfo", userInfo);
        AdminUser user = app.getStorage().getUserStorage().findEmail(userInfo.getEmail());
        if (user == null) {
            user = new AdminUser(userInfo.getEmail());
            user.setDisplayName(userInfo.getDisplayName());
            user.setFirstName(userInfo.getGivenName());
            user.setLastName(userInfo.getFamilyName());
            user.setEmail(userInfo.getEmail());
            user.setRole(AdminRole.DEFAULT);
            user.setEnabled(true);
            user.setSecret(CrocSecurity.generateSecret());
        }
        if (user.isStored()) {
            app.getStorage().getUserStorage().update(user);
        } else {
            app.getStorage().getUserStorage().insert(user);
        }
        String qrUrl = CrocSecurity.getQRBarcodeURL(user.getFirstName().toLowerCase(), app.getServerName(), user.getSecret());
        logger.info("qrUrl", qrUrl, Strings.decodeUrl(qrUrl));
        httpExchangeInfo.setResponse("text/json", true);
        out = httpExchangeInfo.getPrintStream();
        out.println(userInfo.getJson());
        logger.info(userInfo.getJson());
    }    
}
