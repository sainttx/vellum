/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package crocserver.httphandler.access;

import crocserver.app.CrocApp;
import crocserver.httphandler.common.AbstractPageHandler;
import crocserver.storage.servicecert.ClientService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import vellum.format.CalendarFormats;

/**
 *
 * @author evans
 */
public class AccessHomeHandler extends AbstractPageHandler {

    CrocApp app;

    public AccessHomeHandler(CrocApp app) {
        super();
        this.app = app;
    }

    @Override
    public void handle() throws IOException, SQLException {
        htmlPrinter.div("menuBarDiv");
        htmlPrinter.a_("/", "Home");
        htmlPrinter.span("style", "|");
        htmlPrinter.a_(app.getGoogleApi().getLoginUrl(), "Login with Google");
        htmlPrinter._div();
        printCerts("certs", app.getStorage().getClientCertStorage().getList());
    }
    
    private void printCerts(String label, Collection<ClientService> certs) {
        htmlPrinter.h(3, label);
        htmlPrinter.tableDiv("resultSet");
        htmlPrinter.trh("id", "org", "host", "client", "updated", "updated by");
        for (ClientService cert : certs) {
            htmlPrinter.trd(
                    String.format("<a href='/view/cert/%s'>%s</a>", cert.getId(), cert.getId()),
                    cert.getOrgId(),
                    cert.getHostName(),
                    cert.getServiceName(),
                    CalendarFormats.timestampFormat.format(cert.getUpdated()),
                    cert.getUpdatedBy());
        }
        htmlPrinter._table();
        htmlPrinter._div();
    }      
}
