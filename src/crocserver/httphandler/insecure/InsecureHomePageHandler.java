/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package crocserver.httphandler.insecure;

import crocserver.httphandler.access.*;
import bizstat.entity.ServiceRecord;
import bizstat.server.BizstatMessageBuilder;
import crocserver.httphandler.common.AbstractPageHandler;
import java.util.Collection;
import java.util.Iterator;
import vellum.util.DateFormats;
import vellum.util.Strings;
import java.util.List;
import vellum.datatype.Millis;
import vellum.html.HtmlPrinter;
import vellum.logr.LogrFactory;
import vellum.logr.LogrRecord;
import vellum.format.ListFormats;
import crocserver.storage.CrocStorage;
import vellum.logr.LogrLevel;

/**
 *
 * @author evans
 */
public class InsecureHomePageHandler extends AbstractPageHandler {

    CrocStorage storage;

    public InsecureHomePageHandler(CrocStorage storage) {
        super();
        this.storage = storage;
    }

    @Override
    protected void handle() throws Exception {
       HtmlPrinter p = new HtmlPrinter(out);
       p.h(3, "welcome");       
       p.a_(String.format("https://%s:%d/", httpExchange.getLocalAddress().getHostName(), 8444), "secure site");
           
    }    
}
