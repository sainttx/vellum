/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package crocserver.httphandler.secure;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import crocserver.app.CrocApp;
import crocserver.httphandler.access.*;
import crocserver.storage.common.CrocStorage;
import java.io.IOException;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evans
 */
public class SecureHttpHandler implements HttpHandler {
    Logr logger = LogrFactory.getLogger(SecureHttpHandler.class);
    CrocApp app;
    CrocStorage storage;
    AccessHttpHandler childHandler; 
    
    public SecureHttpHandler(CrocApp app) {
        this.app = app;
        this.storage = app.getStorage();        
        childHandler = new AccessHttpHandler(app);
    }
    
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        if (!handlePath(httpExchange) && !childHandler.handlePath(httpExchange)) {
            new SecureHomeHandler(app).handle(httpExchange);
        }  
    }
    
    public boolean handlePath(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        logger.info("path", path);
        if (path.startsWith("/enable/service/")) {
            new EnableServiceHandler(storage).handle(httpExchange);
        } else if (path.startsWith("/gen/p12/")) {
            new GenP12Handler(app).handle(httpExchange);
        } else if (path.startsWith("/sign/cert/")) {
            new SignCertHandler(app).handle(httpExchange);
        } else if (path.startsWith("/view/user/")) {
            new ViewUserHandler(storage).handle(httpExchange);
        } else if (path.startsWith("/view/cert/")) {
            new ViewCertHandler(storage).handle(httpExchange);
        } else if (path.startsWith("/view/serviceRecord/")) {
            new ViewServiceRecordHandler(storage).handle(httpExchange);
        } else if (path.startsWith("/view/org/")) {
            new ViewOrgHandler(storage).handle(httpExchange);
        } else if (path.startsWith("/storage")) {
            new StoragePageHandler(storage).handle(httpExchange);
        } else if (path.startsWith("/post/")) {
            new PostHandler(app).handle(httpExchange);
        } else {
            return false;            
        }
        return true;
    }
}
