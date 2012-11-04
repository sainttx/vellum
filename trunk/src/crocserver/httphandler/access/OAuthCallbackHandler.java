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
import vellum.html.HtmlPrinter;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.util.Streams;

/**
 *
 * @author evans
 */
public class OAuthCallbackHandler implements HttpHandler {

    Logr logger = LogrFactory.getLogger(getClass());
    CrocApp app;
    HttpExchange httpExchange;
    HttpExchangeInfo httpExchangeInfo;
    PrintStream out;

    public OAuthCallbackHandler(CrocApp app) {
        super();
        this.app = app;
    }
    String state;
    String code;
    String error;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        logger.info("handle", getClass().getSimpleName());
        this.httpExchange = httpExchange;
        httpExchangeInfo = new HttpExchangeInfo(httpExchange);
        logger.info("handle", httpExchangeInfo.getParameterMap());
        if (false) {
            String post = Streams.readString(httpExchange.getRequestBody());
            logger.info("post", post);
        }
        state = httpExchangeInfo.getParameter("state");
        code = httpExchangeInfo.getParameter("code");
        error = httpExchangeInfo.getParameter("error");
        try {
            if (error != null) {
                httpExchangeInfo.handleError(error);
            } else if (code != null) {
                handle();
            } else {
                httpExchangeInfo.handleError("internal error");                
            }
        } catch (Exception e) {
            httpExchangeInfo.handleException(e);
        }
        httpExchange.close();
    }
    
    GoogleUserInfo userInfo;    
    
    private void handle() throws Exception {
        userInfo = app.getGoogleApi().sendTokenRequest(code);
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
        httpExchangeInfo.setResponse("text/html", true);
        HtmlPrinter p = new HtmlPrinter(httpExchange.getResponseBody());
        p.div("menuBarDiv");
        p.a_("/", "Home");
        p._div();
        p.h(2, "Welcome, " + userInfo.getDisplayName());
        p.span("", String.format("Your email address: %s", userInfo.getEmail()));
        p.div("");
        if (false) {
            String qrUrl = CrocSecurity.getQRBarcodeURL(user.getFirstName().toLowerCase(), app.getServerName(), user.getSecret());
            logger.info("qrUrl", qrUrl);
            p.img(qrUrl);
            p.pre(qrUrl);
        }
        String signCertUrl = String.format("%s/sign/userCert/%s", app.getServerUrl(), user.getEmail());
        p.pre(String.format("curl %s", signCertUrl));
        p._div();
    }    
}
