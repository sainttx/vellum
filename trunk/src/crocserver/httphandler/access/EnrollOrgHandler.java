/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package crocserver.httphandler.access;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import crocserver.app.CrocApp;
import crocserver.httpserver.HttpExchangeInfo;
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

    String userName;
    String orgName;
    
    public EnrollOrgHandler(CrocApp app) {
        super();
        this.app = app;
    }
    
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        this.httpExchange = httpExchange;
        httpExchangeInfo = new HttpExchangeInfo(httpExchange);
        httpExchange.getResponseHeaders().set("Content-type", "text/plain");
        out = new PrintStream(httpExchange.getResponseBody());
        if (httpExchangeInfo.getPathLength() < 4) {
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            out.printf("ERROR %s\n", httpExchangeInfo.getPath());
        } else {
            userName = httpExchangeInfo.getPathString(2);
            orgName = httpExchangeInfo.getPathString(3);
            try {
                handle();
            } catch (Exception e) {
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
                e.printStackTrace(out);
                e.printStackTrace(System.err);
                out.printf("ERROR %s\n", e.getMessage());
            }
        }
        httpExchange.close();
    }

    Org org;
    
    private void handle() throws Exception {
        AdminUser user = app.getStorage().getUserStorage().get(userName);
        logger.info("user", user);
        String url = httpExchangeInfo.getParameterMap().get("url");
        if (url == null) {
            url = orgName;
        }
        if (!Patterns.matchesUrl(url)) {
            throw new Exception("url " + url);
        }
        org = app.getStorage().getOrgStorage().find(orgName);
        if (org == null) {
            org = new Org(orgName, userName);
        } else if (!org.getUpdatedBy().equals(userName)) {
            throw new StorageException(StorageExceptionType.ALREADY_EXISTS, orgName);
        }
        org.setDisplayName(httpExchangeInfo.getParameterMap().get("displayName"));
        org.setUrl(url);
        org.setRegion(httpExchangeInfo.getParameterMap().get("region"));
        org.setLocality(httpExchangeInfo.getParameterMap().get("locality"));
        org.setCountry(httpExchangeInfo.getParameterMap().get("country"));
        org.setUpdatedBy(userName);
        if (org.isStored()) {
            app.getStorage().getOrgStorage().update(org);
        } else {
            app.getStorage().getOrgStorage().insert(org);
        }
        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        out.printf("OK %s %d\n", org.getName(), org.getId());
    }
    
}
