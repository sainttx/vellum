/*
 * Apache Software License 2.0, (c) Copyright 2012 Evan Summers, 2010 iPay (Pty) Ltd
 * 
 */
package crocserver.httphandler.persona;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import crocserver.app.CrocApp;
import crocserver.app.CrocCookie;
import crocserver.app.CrocSecurity;
import crocserver.app.JsonStrings;
import crocserver.httpserver.HttpExchangeInfo;
import crocserver.storage.adminuser.AdminRole;
import crocserver.storage.adminuser.AdminUser;
import java.io.IOException;
import java.util.Date;
import vellum.datatype.Emails;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.parameter.StringMap;
import vellum.util.Strings;

/**
 *
 * @author evans
 */
public class LoginPersonaHandler implements HttpHandler {

    Logr logger = LogrFactory.getLogger(getClass());
    CrocApp app;
    HttpExchange httpExchange;
    HttpExchangeInfo httpExchangeInfo;

    public LoginPersonaHandler(CrocApp app) {
        super();
        this.app = app;
    }
    
    String userId;
    String assertion;
    
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        this.httpExchange = httpExchange;
        httpExchangeInfo = new HttpExchangeInfo(httpExchange);
        logger.info("handle", getClass().getSimpleName(), httpExchangeInfo.getPath(), httpExchangeInfo.getParameterMap());
            assertion = httpExchangeInfo.getParameter("assertion");
            logger.info("input", userId, assertion);
            try {
                if (assertion != null) {
                    handle();
                } else {
                    httpExchangeInfo.handleError("require assertion");
                }
            } catch (Exception e) {
                httpExchangeInfo.handleException(e);
            }
        httpExchange.close();
    }
    
    PersonaUserInfo userInfo;    
    
    private void handle() throws Exception {
        userInfo = new PersonaApi(app.getServerUrl()).getUserInfo(assertion);
        logger.info("userInfo", userInfo);
        AdminUser user = app.getStorage().getUserStorage().findEmail(userInfo.getEmail());
        if (user == null) {
            user = new AdminUser(userInfo.getEmail());
            user.setEmail(userInfo.getEmail());
            user.setFirstName(Emails.getUsername(userInfo.getEmail()));
            user.setDisplayName(Emails.getUsername(userInfo.getEmail()));
            user.setRole(AdminRole.DEFAULT);
            user.setEnabled(true);
            user.setSecret(CrocSecurity.createSecret());
        }
        user.setLoginTime(new Date());
        if (user.isStored()) {
            app.getStorage().getUserStorage().update(user);
        } else {
            app.getStorage().getUserStorage().insert(user);
        }
        String totpUrl = CrocSecurity.getTotpUrl(user.getFirstName().toLowerCase(), app.getServerName(), user.getSecret());
        String qrUrl = CrocSecurity.getQrCodeUrl(user.getFirstName().toLowerCase(), app.getServerName(), user.getSecret());
        logger.info("qrUrl", qrUrl, Strings.decodeUrl(qrUrl));
        CrocCookie cookie = new CrocCookie(user.getEmail(), user.getDisplayName(), user.getLoginTime().getTime(), assertion);
        cookie.createAuthCode(user.getSecret().getBytes());
        httpExchangeInfo.setCookie(cookie.toMap(), CrocCookie.MAX_AGE_MILLIS);
        httpExchangeInfo.sendResponse("text/json", true);
        StringMap responseMap = new StringMap();
        responseMap.put("email", user.getEmail());
        responseMap.put("displayName", user.getDisplayName());
        responseMap.put("qr", qrUrl);
        responseMap.put("totpSecret", user.getSecret());
        responseMap.put("totpUrl", totpUrl);
        responseMap.put("authCode", cookie.getAuthCode());
        String json = JsonStrings.buildJson(responseMap);
        logger.info(json);
        httpExchangeInfo.getPrintStream().println(json);
    }    
}
