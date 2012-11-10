/*
 * Apache Software License 2.0, Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2012 Evan Summers, Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2010 iPay (Pty) Ltd
 */
package crocserver.httphandler.access;

import crocserver.httphandler.common.AbstractPageHandler;
import vellum.html.HtmlPrinter;
import crocserver.storage.common.CrocStorage;
import crocserver.storage.servicecert.ClientService;

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
        p.span("pageTitle", String.format("ClientCert %s", id));
        ClientService cert = storage.getClientCertStorage().find(id);
        p.tableDiv("resultSet");
        p.thead();
        p._thead();
        p.tbody();
        p.trhd("Org", cert.getOrgId());
        p.trhd("Host", cert.getHostName());
        p.trhd("Client", cert.getServiceName());
        p.trhd("Updated", cert.getUpdated());
        p.trhd("Updated by", cert.getUpdatedBy());
        p._tbody();
        p._tableDiv();
        p.pre(cert.getCert());
    }
}
