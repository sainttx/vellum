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
import crocserver.storage.common.CrocStorage;
import crocserver.storage.org.Org;
import crocserver.storage.servicecert.ClientCert;
import java.security.cert.X509Certificate;
import java.util.Date;
import sun.security.pkcs.PKCS10;
import vellum.security.DefaultKeyStores;
import vellum.security.KeyStores;
import vellum.util.Streams;

/**
 *
 * @author evans
 */
public class SignCertHandler implements HttpHandler {

    Logr logger = LogrFactory.getLogger(getClass());
    CrocStorage storage;
    HttpExchange httpExchange;
    HttpExchangeInfo httpExchangeInfo;
    PrintStream out;
    
    String certReqPem;    
    String userName;
    String orgName;
    String hostName;
    String clientName;
    String dname;
    String cert;
    Org org;

    public SignCertHandler(CrocStorage storage) {
        super();
        this.storage = storage;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        this.httpExchange = httpExchange;
        httpExchangeInfo = new HttpExchangeInfo(httpExchange);
        httpExchange.getResponseHeaders().set("Content-type", "text/plain");
        certReqPem = Streams.readString(httpExchange.getRequestBody());
        out = new PrintStream(httpExchange.getResponseBody());
        logger.info(getClass().getSimpleName(), httpExchangeInfo.getPathArgs().length);
        if (httpExchangeInfo.getPathArgs().length == 6) {
            userName = httpExchangeInfo.getPathString(2);
            orgName = httpExchangeInfo.getPathString(3);
            hostName = httpExchangeInfo.getPathString(4);
            clientName = httpExchangeInfo.getPathString(5);
            try {
                sign();
            } catch (Exception e) {
                handle(e);
            }
        } else {
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            out.printf("ERROR %s\n", httpExchangeInfo.getPath());
        }
        httpExchange.close();
    }

    private void handle(Exception e) throws IOException {
        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
        e.printStackTrace(out);
        e.printStackTrace(System.err);
        out.printf("ERROR %s\n", e.getMessage());
    }

    private void sign() throws Exception {
        org = storage.getOrgStorage().get(orgName);
        setDname();
        logger.info("sign", dname, certReqPem.length());
        String alias = "croc-server";
        PKCS10 certReq = KeyStores.createCertReq(certReqPem);
        X509Certificate signedCert = KeyStores.signCert(
                DefaultKeyStores.getPrivateKey(alias), DefaultKeyStores.getCert(alias), 
                certReq, new Date(), 999);
        String signedCertPem = KeyStores.buildCertPem(signedCert);
        ClientCert clientCert = storage.getClientCertStorage().find(org.getId(), hostName, clientName);
        if (clientCert == null) {
            clientCert = new ClientCert(org.getId(), hostName, clientName);
            clientCert.setX509Cert(signedCert);
            storage.getClientCertStorage().insert(userName, org, clientCert);
        } else {
            logger.info("updateCert", clientCert.getId());    
            clientCert.setX509Cert(signedCert);
            storage.getClientCertStorage().updateCert(userName, clientCert);
        }
        logger.info("issuer", KeyStores.getIssuerDname(signedCertPem));
        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        out.println(signedCertPem);
    }

    private void setDname() throws Exception {
        dname = KeyStores.formatDname(clientName, hostName, orgName,
                org.getRegion(), org.getLocality(), org.getCountry());
    }
}
