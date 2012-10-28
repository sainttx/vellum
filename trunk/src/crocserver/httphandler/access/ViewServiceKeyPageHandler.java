/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package crocserver.httphandler.access;

import crocserver.httphandler.common.AbstractPageHandler;
import vellum.html.HtmlPrinter;
import crocserver.storage.CrocStorage;
import crocserver.storage.servicekey.ServiceCert;

/**
 *
 * @author evans
 */
public class ViewServiceKeyPageHandler extends AbstractPageHandler {
    CrocStorage storage;

    public ViewServiceKeyPageHandler(CrocStorage storage) {
        super();
        this.storage = storage;
    }

    @Override
    protected void handle() throws Exception {
       HtmlPrinter p = new HtmlPrinter(out);
       Long id = Long.parseLong(pathArgs[2]);
       p.div("menuBarDiv");
       p.a_("/", "Home");
       p._div();
       p.spanf("pageTitle", "ServiceKey %s", id);
       ServiceCert serviceKey = storage.getServiceKeyStorage().find(id);
       p.tableDiv("resultSet");
       p.thead();
       p._thead();
       p.tbody();
       p.trhd("Org", serviceKey.getOrgId());
       p.trhd("Host", serviceKey.getHostName());
       p.trhd("Service", serviceKey.getServiceName());
       p._tbody();
       p._tableDiv();
       p.pre(serviceKey.getCert());
    }    
}
