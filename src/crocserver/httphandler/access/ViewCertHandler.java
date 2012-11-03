/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package crocserver.httphandler.access;

import crocserver.httphandler.common.AbstractPageHandler;
import vellum.html.HtmlPrinter;
import crocserver.storage.common.CrocStorage;
import crocserver.storage.servicecert.ClientCert;

/**
 *
 * @author evans
 */
public class ViewCertHandler extends AbstractPageHandler {

    CrocStorage storage;

    public ViewCertHandler(CrocStorage storage) {
        super();
        this.storage = storage;
    }

    @Override
    protected void handle() throws Exception {
        Long id = Long.parseLong(pathArgs[2]);
        HtmlPrinter p = new HtmlPrinter(out);
        p.div("menuBarDiv");
        p.a_("/", "Home");
        p._div();
        p.spanf("pageTitle", "ClientCert %s", id);
        ClientCert cert = storage.getClientCertStorage().find(id);
        p.tableDiv("resultSet");
        p.thead();
        p._thead();
        p.tbody();
        p.trhd("Org", cert.getOrgId());
        p.trhd("Host", cert.getHostName());
        p.trhd("Client", cert.getClientName());
        p.trhd("Updated", cert.getUpdated());
        p.trhd("Updated by", cert.getUpdatedBy());
        p._tbody();
        p._tableDiv();
        p.pre(cert.getCert());
    }
}
