/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package crocserver.httphandler.insecure;

import crocserver.app.CrocApp;
import crocserver.httphandler.common.AbstractPageHandler;
import vellum.html.HtmlPrinter;

/**
 *
 * @author evans
 */
public class InsecureHomePageHandler extends AbstractPageHandler {

    CrocApp app;

    public InsecureHomePageHandler(CrocApp app) {
        super();
        this.app = app;
    }

    @Override
    protected void handle() throws Exception {
       HtmlPrinter p = new HtmlPrinter(out);
       p.h(3, "welcome");
       p.a_(String.format("https://%s:%d/", httpExchange.getLocalAddress().getHostName(), 8444), "secure site");
           
    }    
}
