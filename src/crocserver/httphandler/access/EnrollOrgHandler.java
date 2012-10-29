/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package crocserver.httphandler.access;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import crocserver.httpserver.HttpExchangeInfo;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.sql.SQLException;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.storage.StorageException;
import crocserver.storage.common.CrocStorage;
import crocserver.storage.org.Org;
import vellum.format.ListFormats;

/**
 *
 * @author evans
 */
public class EnrollOrgHandler implements HttpHandler {
    Logr logger = LogrFactory.getLogger(getClass());
    CrocStorage storage;
    HttpExchange httpExchange;
    HttpExchangeInfo httpExchangeInfo;
    PrintStream out;

    String userName;
    String orgName;
    
    public EnrollOrgHandler(CrocStorage storage) {
        super();
        this.storage = storage;
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
                insert();
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            } catch (Exception e) {
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
                e.printStackTrace(out);
                e.printStackTrace(System.err);
                out.printf("ERROR %s\n", e.getMessage());
            }
        }
        httpExchange.close();
    }

    private void insert() throws StorageException, SQLException {
        Org org = new Org(orgName, userName);
        org.setDisplayName(httpExchangeInfo.getParameterMap().get("displayName"));
        org.setUrl(httpExchangeInfo.getParameterMap().get("url"));
        org.setRegion(httpExchangeInfo.getParameterMap().get("region"));
        org.setCity(httpExchangeInfo.getParameterMap().get("city"));
        org.setCountry(httpExchangeInfo.getParameterMap().get("country"));
        storage.getOrgStorage().insert(org);
        out.printf("OK %s\n", ListFormats.displayFormatter.formatArgs(
                getClass().getName(), userName, orgName, httpExchangeInfo.getParameterMap()
                ));
    }
    
}
