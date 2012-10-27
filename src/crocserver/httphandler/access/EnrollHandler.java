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
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import crocserver.storage.CrocStorage;
import crocserver.storage.adminuser.AdminUser;
import crocserver.storage.org.Org;
import crocserver.storage.servicekey.ServiceKey;
import java.util.Date;
import vellum.format.ListFormats;
import vellum.security.KeyStores;
import vellum.security.KeyPairGenerator;

/**
 *
 * @author evans
 */
public class EnrollHandler implements HttpHandler {
    Logr logger = LogrFactory.getLogger(getClass());
    CrocStorage storage;
    HttpExchange httpExchange;
    HttpExchangeInfo httpExchangeInfo;
    PrintStream out;

    String userName;
    String hostName;
    String serviceName;
    String publicKey;
    
    public EnrollHandler(CrocStorage storage) {
        super();
        this.storage = storage;
    }
    
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        this.httpExchange = httpExchange;
        httpExchangeInfo = new HttpExchangeInfo(httpExchange);
        httpExchange.getResponseHeaders().set("Content-type", "text/plain");
        out = new PrintStream(httpExchange.getResponseBody());
        if (httpExchangeInfo.getPathArgs().length < 4) {
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            out.printf("ERROR %s\n", httpExchangeInfo.getPath());
        } else {
            userName = httpExchangeInfo.getPathString(1);
            hostName = httpExchangeInfo.getPathString(2);
            serviceName = httpExchangeInfo.getPathString(3);
            try {
                String dname = KeyStores.formatDname(hostName, serviceName, userName, "local", "local", "local");
                AdminUser adminUser = storage.getAdminUserStorage().find(userName);
                String orgName = adminUser.getOrgName();
                if (orgName != null) {
                    Org org = storage.getOrgStorage().get(orgName);
                    dname = KeyStores.formatDname(hostName, serviceName, 
                            org.getName(), org.getRegion(), org.getCity(), org.getCountry());
                }
                KeyPairGenerator keyPair = new KeyPairGenerator();
                keyPair.genKeyPair(dname, new Date(), 999, 1024);
                ServiceKey serviceKey = new ServiceKey(userName, hostName, serviceName, 
                        KeyStores.buildCertPem(keyPair.getCert()));
                out.println(KeyStores.buildPrivateKeyPem(keyPair.getPrivateKey()));
                storage.getServiceKeyStorage().insert(serviceKey);
                out.printf("OK %s\n", ListFormats.displayFormatter.formatArgs(
                        getClass().getName(), userName, hostName, serviceName, httpExchangeInfo.getParameterMap()));
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
}
