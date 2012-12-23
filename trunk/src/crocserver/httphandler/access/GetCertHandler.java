
/*
 * Apache Software License 2.0, (c) Copyright 2012 Evan Summers, 2010 iPay (Pty) Ltd
 */
package crocserver.httphandler.access;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import vellum.httpserver.HttpExchangeInfo;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import crocserver.storage.common.CrocStorage;
import crocserver.storage.adminuser.AdminUser;
import crocserver.storage.org.Org;
import crocserver.storage.service.Service;
import java.util.Date;
import vellum.format.ListFormats;
import vellum.security.KeyStores;
import vellum.security.GeneratedRsaKeyPair;

/**
 *
 * @author evans
 */
public class GetCertHandler implements HttpHandler {
    Logr logger = LogrFactory.getLogger(getClass());
    CrocStorage storage;
    HttpExchange httpExchange;
    HttpExchangeInfo httpExchangeInfo;
    PrintStream out;

    String userName;
    String orgName;
    String hostName;
    String serviceName;
    String cert;
    
    public GetCertHandler(CrocStorage storage) {
        super();
        this.storage = storage;
    }
    
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        this.httpExchange = httpExchange;
        httpExchangeInfo = new HttpExchangeInfo(httpExchange);
        httpExchange.getResponseHeaders().set("Content-type", "text/plain");
        out = new PrintStream(httpExchange.getResponseBody());
        if (httpExchangeInfo.getPathArgs().length < 6) {
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            out.printf("ERROR %s\n", httpExchangeInfo.getPath());
        } else {
            userName = httpExchangeInfo.getPathString(2);
            orgName = httpExchangeInfo.getPathString(3);
            hostName = httpExchangeInfo.getPathString(4);
            serviceName = httpExchangeInfo.getPathString(5);
            logger.info("enroll", orgName, hostName, serviceName);
            try {
                Org org = storage.getOrgStorage().get(orgName);
                Service serviceKey = storage.getServiceStorage().find(org.getId(), hostName, serviceName);
                if (serviceKey == null) {
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
                    out.printf("ERROR: not found\n");
                } else {
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    out.println(serviceKey.getCert());
                }
            } catch (Exception e) {
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
                e.printStackTrace(out);
                e.printStackTrace(System.err);
                out.printf("ERROR %s\n", e.getMessage());
            }
        }
        httpExchange.close();
    }
}
