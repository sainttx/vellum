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
import vellum.security.DefaultKeyStores;
import vellum.security.KeyStores;
import vellum.security.GeneratedRsaKeyPair;

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

    String dname;
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
            logger.info("enroll", userName, hostName, serviceName);
            try {
                generate();
            } catch (Exception e) {
                handle(e);
            }
        }
        httpExchange.close();
    }
    
    private void handle(Exception e) throws IOException {
        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
        e.printStackTrace(out);
        e.printStackTrace(System.err);
        out.printf("ERROR %s\n", e.getMessage());
    }
    
    private void generate() throws Exception {
        setDname();
        logger.info("generate", dname);
        GeneratedRsaKeyPair keyPair = new GeneratedRsaKeyPair();
        keyPair.generate(dname, new Date(), 999);
        String alias = "croc-server";
        keyPair.sign(DefaultKeyStores.getPrivateKey(alias), DefaultKeyStores.getCert(alias));
        ServiceKey serviceKey = new ServiceKey(userName, hostName, serviceName,
                KeyStores.buildCertPem(keyPair.getCert()));
        storage.getServiceKeyStorage().insert(serviceKey);
        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        out.println(KeyStores.buildPrivateKeyPem(keyPair.getPrivateKey()));
    }    
    
    private void setDname() throws Exception {
        dname = KeyStores.formatDname(hostName, serviceName, userName, "local", "local", "local");
        AdminUser adminUser = storage.getAdminUserStorage().get(userName);
        String orgName = adminUser.getOrgName();
        if (orgName != null) {
            Org org = storage.getOrgStorage().get(orgName);
            dname = KeyStores.formatDname(hostName, serviceName,
                    org.getName(), org.getRegion(), org.getCity(), org.getCountry());
        }        
    }       
}
