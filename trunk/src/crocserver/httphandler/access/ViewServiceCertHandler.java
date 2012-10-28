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
public class ViewServiceCertHandler extends AbstractPageHandler {
    CrocStorage storage;

    public ViewServiceCertHandler(CrocStorage storage) {
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
       p.spanf("pageTitle", "ServiceCert %s", id);
       ServiceCert serviceCert = storage.getServiceCertStorage().find(id);
       p.tableDiv("resultSet");
       p.thead();
       p._thead();
       p.tbody();
       p.trhd("Org", serviceCert.getOrgId());
       p.trhd("Host", serviceCert.getHostName());
       p.trhd("Service", serviceCert.getServiceName());
       p.trhd("Updated", serviceCert.getUpdated());
       p.trhd("Updated by", serviceCert.getUpdatedBy());
       p._tbody();
       p._tableDiv();
       p.pre(serviceCert.getCert());
    }    
}
