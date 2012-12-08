/*
 * Apache Software License 2.0, (c) Copyright 2012 Evan Summers, 2010 iPay (Pty) Ltd
 */
package crocserver.httphandler.access;

import crocserver.httphandler.common.AbstractPageHandler;
import crocserver.storage.clientcert.Cert;
import vellum.html.HtmlPrinter;
import crocserver.storage.common.CrocStorage;

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
        Long id = Long.parseLong(pathArgs[1]);
        HtmlPrinter p = new HtmlPrinter(out);
        p.div("menuBarDiv");
        p.aClosed("/", "Home");
        p.divClose();
        p.span("pageTitle", String.format("Cert %s", id));
        Cert cert = storage.getCertStorage().find(id);
        p.tableDiv("resultSet");
        p.thead();
        p.theadClose();
        p.tbody();
        p.trhd("Subject", cert.getSubject());
        p.trhd("Updated", cert.getUpdated());
        p.trhd("Updated by", cert.getUpdatedBy());
        p.tbodyClose();
        p.tableDivClose();
        p.pre(cert.getCert());
    }
}
