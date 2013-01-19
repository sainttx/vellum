/*
 * Apache Software License 2.0, (c) Copyright 2012 Evan Summers, 2010 iPay (Pty) Ltd
 */
package crocserver.httphandler.access;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import crocserver.app.CrocApp;
import vellum.httpserver.HttpExchangeInfo;
import crocserver.storage.adminuser.AdminUser;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.storage.StorageException;
import crocserver.storage.org.Org;
import vellum.datatype.Patterns;
import vellum.storage.StorageExceptionType;

/**
 *
 * @author evans
 */
public class EnrollOrgHandler implements HttpHandler {
    Logr logger = LogrFactory.getLogger(getClass());
    CrocApp app;
    HttpExchange httpExchange;
    HttpExchangeInfo httpExchangeInfo;
    PrintStream out;

    public EnrollOrgHandler(CrocApp app) {
        super();
        this.app = app;
    }

    String userName;
    String orgName;
    
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        this.httpExchange = httpExchange;
        httpExchangeInfo = new HttpExchangeInfo(httpExchange);
        logger.info("handle", getClass().getSimpleName(), httpExchangeInfo.getParameterMap(), httpExchangeInfo.getCookieMap());
        if (httpExchangeInfo.getPathLength() < 2) {
            httpExchangeInfo.handleError(httpExchangeInfo.getPath());
        } else {
            userName = httpExchangeInfo.getPathString(1);
        }
        orgName = httpExchangeInfo.getPathString(2);
        try {
            handle();
        } catch (Exception e) {
            httpExchangeInfo.handleError(e);
        }
        httpExchange.close();
    }

    Org org;
    
    private void handle() throws Exception {
        AdminUser user = app.getUser(httpExchangeInfo, true);
        logger.info("user", user);
        String url = httpExchangeInfo.getParameterMap().get("url");
        if (url == null) {
            url = orgName;
        }
        if (url != null) {
            if (!Patterns.matchesUrl(url)) {
                throw new Exception("url " + url);
            }
        }
        String orgName = url;
        org = app.getStorage().getOrgStorage().find(orgName);
        if (org == null) {
            org = new Org(orgName);
        } else if (org.getId() != user.getOrgId()) {
            throw new StorageException(StorageExceptionType.ALREADY_EXISTS, orgName);
        }
        org.setDisplayName(httpExchangeInfo.getParameterMap().get("displayName"));
        org.setUrl(url);
        org.setRegion(httpExchangeInfo.getParameterMap().get("region"));
        org.setLocality(httpExchangeInfo.getParameterMap().get("locality"));
        org.setCountry(httpExchangeInfo.getParameterMap().get("country"));
        if (org.isStored()) {
            app.getStorage().getOrgStorage().update(org);
        } else {
            app.getStorage().getOrgStorage().insert(org);
        }
        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        out.printf("OK %s %d\n", org.getOrgName(), org.getId());
    }
    
}
