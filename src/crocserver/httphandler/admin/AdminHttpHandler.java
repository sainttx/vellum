/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package crocserver.httphandler.admin;

import crocserver.httphandler.access.GenKeyHandler;
import crocserver.httphandler.access.AccessHomePageHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import crocserver.httphandler.access.CertReqHandler;
import crocserver.httphandler.access.GetPublicKeyHandler;
import crocserver.httphandler.access.StoragePageHandler;
import crocserver.httphandler.access.ViewServiceRecordPageHandler;
import java.io.IOException;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import crocserver.storage.CrocStorage;

/**
 *
 * @author evans
 */
public class AdminHttpHandler implements HttpHandler {
    Logr logger = LogrFactory.getLogger(AdminHttpHandler.class);
    CrocStorage storage;
            
    public AdminHttpHandler(CrocStorage storage) {
        this.storage = storage;
    }
    
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        logger.info("path", path);
        if (path.startsWith("/storage/")) {
            new StoragePageHandler(storage).handle(httpExchange);
        } else if (path.startsWith("/view/serviceRecord/")) {
            new ViewServiceRecordPageHandler(storage).handle(httpExchange);
        } else if (path.startsWith("/genKey/")) {
            new GenKeyHandler(storage).handle(httpExchange);
        } else if (path.startsWith("/publicKey/")) {
            new GetPublicKeyHandler(storage).handle(httpExchange);
        } else if (path.startsWith("/certReq/")) {
            new CertReqHandler(storage).handle(httpExchange);
        } else {
            new AdminHomePageHandler(storage).handle(httpExchange);
        }        
    }
}
