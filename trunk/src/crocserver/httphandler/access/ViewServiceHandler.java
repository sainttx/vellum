/*
 * Apache Software License 2.0, (c) Copyright 2012 Evan Summers, 2010 iPay (Pty) Ltd
 */
package crocserver.httphandler.access;

import crocserver.httphandler.common.AbstractPageHandler;
import vellum.html.HtmlPrinter;
import crocserver.storage.common.CrocStorage;
import crocserver.storage.servicecert.Service;

/**
 *
 * @author evans
 */
public class ViewServiceHandler extends AbstractPageHandler {

    CrocStorage storage;

    public ViewServiceHandler(CrocStorage storage) {
        super();
        this.storage = storage;
    }

    @Override
    protected void handle() throws Exception {
        Long id = Long.parseLong(pathArgs[1]);
        HtmlPrinter p = new HtmlPrinter(out);
        p.div("menuBarDiv");
        p.a_("/", "Home");
        p._div();
        p.span("pageTitle", String.format("Service %s", id));
        Service service = storage.getServiceStorage().find(id);
        p.tableDiv("resultSet");
        p.thead();
        p._thead();
        p.tbody();
        p.trhd("Org", service.getOrgId());
        p.trhd("Host", service.getHostName());
        p.trhd("Client", service.getServiceName());
        p.trhd("Updated", service.getUpdated());
        p.trhd("Updated by", service.getUpdatedBy());
        p._tbody();
        p._tableDiv();
        p.pre(service.getCert());
    }
}
