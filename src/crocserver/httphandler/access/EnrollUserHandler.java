/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package crocserver.httphandler.access;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import crocserver.app.CrocApp;
import crocserver.httpserver.HttpExchangeInfo;
import crocserver.storage.adminuser.AdminRole;
import crocserver.storage.adminuser.User;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.sql.SQLException;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.storage.StorageException;
import crocserver.storage.common.CrocStorage;
import crocserver.storage.servicecert.ClientCert;
import java.security.cert.X509Certificate;
import java.util.Date;
import sun.security.pkcs.PKCS10;
import vellum.format.ListFormats;
import vellum.security.DefaultKeyStores;
import vellum.security.KeyStores;
import vellum.util.Streams;

/**
 *
 * @author evans
 */
public class EnrollUserHandler implements HttpHandler {
    Logr logger = LogrFactory.getLogger(getClass());
    CrocApp app;
    CrocStorage storage;
    HttpExchange httpExchange;
    HttpExchangeInfo httpExchangeInfo;
    PrintStream out;

    String certReqPem;
    String userName;
    String dname;
    String signedCertPem;    
    User user;    
    X509Certificate signedCert;

    public EnrollUserHandler(CrocApp app) {
        super();
        this.app = app;
        this.storage = app.getStorage();
    }
    
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        this.httpExchange = httpExchange;
        httpExchangeInfo = new HttpExchangeInfo(httpExchange);
        httpExchange.getResponseHeaders().set("Content-type", "text/plain");
        certReqPem = Streams.readString(httpExchange.getRequestBody());        
        out = new PrintStream(httpExchange.getResponseBody());
        if (httpExchangeInfo.getPathLength() < 3) {
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            out.printf("ERROR %s\n", httpExchangeInfo.getPath());
        } else {
            userName = httpExchangeInfo.getPathString(2);
            try {
                handle();
                app.getGtalkConnection().sendMessage(user.getEmail(), signedCert.getSubjectDN().toString());
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                out.print(signedCertPem);
            } catch (Exception e) {
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
                e.printStackTrace(out);
                e.printStackTrace(System.err);
                out.printf("ERROR %s\n", e.getMessage());
            }
        }
        httpExchange.close();
    }

    private void handle() throws Exception {
        user = new User(userName);
        user.setDisplayName(httpExchangeInfo.getParameterMap().get("userDisplayName"));
        user.setEmail(httpExchangeInfo.getParameterMap().get("email"));
        user.formatSubject();
        user.setLocality(httpExchangeInfo.getParameterMap().get("locality"));
        user.setRegion(httpExchangeInfo.getParameterMap().get("region"));
        user.setCountry(httpExchangeInfo.getParameterMap().get("country"));
        user.setRole(AdminRole.DEFAULT);
        sign();
        storage.getUserStorage().insert(user);
        
    }
    
    private void sign() throws Exception {
        logger.info("sign", dname, certReqPem.length());
        String alias = app.getServerKeyAlias();
        PKCS10 certReq = KeyStores.createCertReq(certReqPem);
        signedCert = KeyStores.signCert(
                DefaultKeyStores.getPrivateKey(alias), DefaultKeyStores.getCert(alias), 
                certReq, new Date(), 999);
        signedCertPem = KeyStores.buildCertPem(signedCert);
        logger.info("subject", KeyStores.getSubjectDname(signedCertPem));
        logger.info("issuer", KeyStores.getIssuerDname(signedCertPem));
    }       
}
