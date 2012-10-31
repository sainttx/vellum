/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package crocserver.httphandler.secure;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.ssl.internal.pkcs12.PKCS12KeyStore;
import crocserver.app.CrocApp;
import crocserver.httpserver.HttpExchangeInfo;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import crocserver.storage.common.CrocStorage;
import crocserver.storage.org.Org;
import crocserver.storage.servicecert.ClientCert;
import java.security.cert.X509Certificate;
import java.util.Date;
import vellum.security.DefaultKeyStores;
import vellum.security.KeyStores;
import vellum.security.GeneratedRsaKeyPair;

/**
 *
 * @author evans
 */
public class GenP12Handler implements HttpHandler {
    Logr logger = LogrFactory.getLogger(getClass());
    CrocApp app;
    CrocStorage storage;
    HttpExchange httpExchange;
    HttpExchangeInfo httpExchangeInfo;
    PrintStream out;

    String userName;
    String orgName;
    String hostName;
    String clientName;
 
    public GenP12Handler(CrocApp app) {
        super();
        this.app = app;
        this.storage = app.getStorage();
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
            clientName = httpExchangeInfo.getPathString(5);
            try {
                handle();
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
    
    Org org;
    
    private void handle() throws Exception {
        org = storage.getOrgStorage().get(orgName);
        if (org == null) {
            org = new Org(orgName, userName);
            storage.getOrgStorage().insert(org);
        }
        String dname = KeyStores.formatDname(clientName, hostName, orgName, 
                org.getRegion(), org.getLocality(), org.getCountry());
        logger.info("generate", dname);
        GeneratedRsaKeyPair keyPair = new GeneratedRsaKeyPair();
        keyPair.generate(dname, new Date(), 999);
        String alias = app.getServerKeyAlias();
        X509Certificate serverCert = DefaultKeyStores.getCert(alias);
        keyPair.sign(DefaultKeyStores.getPrivateKey(alias), serverCert);
        ClientCert clientCert = new ClientCert(userName, org.getId(), hostName, clientName);
        clientCert.setX509Cert(keyPair.getCert());
        storage.getClientCertStorage().insert(userName, org, clientCert);
        PKCS12KeyStore p12 = new PKCS12KeyStore();
        X509Certificate[] chain = new X509Certificate[] {serverCert, keyPair.getCert()};
        char[] password = httpExchangeInfo.getParameterMap().getString("password", clientName).toCharArray();
        p12.engineSetKeyEntry(clientName, keyPair.getPrivateKey(), password, chain);
        logger.verbose(KeyStores.buildPem(p12, clientName, password));
        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        out.println(KeyStores.buildPem(p12, clientName, password));
    }    
    
}
