/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package crocserver.httphandler.access;

import crocserver.storage.servicerecord.ServiceRecord;
import bizstat.server.BizstatMessageBuilder;
import crocserver.httphandler.common.AbstractPageHandler;
import crocserver.storage.adminuser.User;
import java.util.Collection;
import java.util.Iterator;
import vellum.datatype.Millis;
import vellum.html.HtmlPrinter;
import vellum.logr.LogrFactory;
import vellum.logr.LogrRecord;
import vellum.format.ListFormats;
import crocserver.storage.common.CrocStorage;
import crocserver.storage.org.Org;
import crocserver.storage.servicecert.ClientCert;
import vellum.format.CalendarFormats;
import vellum.logr.LogrLevel;

/**
 *
 * @author evans
 */
public class AccessHomeHandler extends AbstractPageHandler {

    CrocStorage storage;

    public AccessHomeHandler(CrocStorage storage) {
        super();
        this.storage = storage;
    }

    @Override
    protected void handle() throws Exception {
        htmlPrinter.div("menuBarDiv");
        htmlPrinter.a_("/", "Home");
        htmlPrinter._div();
        printOrgs("orgs", storage.getOrgStorage().getList());
        printUsers("admin users", storage.getUserStorage().getList());
        printCerts("certs", storage.getClientCertStorage().getList());
        printSeviceRecords("service records", storage.getServiceRecordStorage().getList());
        if (LogrFactory.getDefaultLevel().ordinal() < LogrLevel.INFO.ordinal()) {
            printLog("log", LogrFactory.getDequerProvider().getDequerHandler().getDequer().tailDescending(100));
        }
    }

    private void printOrgs(String label, Collection<Org> orgs) {
        htmlPrinter.h(3, label);
        htmlPrinter.tableDiv("resultSet");
        htmlPrinter.trh("id", "org name", "display name", "url", "updated");
        for (Org org : orgs) {
            htmlPrinter.trd(
                    String.format("<a href='/view/org/%d'>%d</a>", org.getId(), org.getId()),
                    org.getName(),
                    org.getDisplayName(),
                    org.getUrl(),
                    CalendarFormats.timestampFormat.format(org.getUpdated()));
        }
        htmlPrinter._table();
        htmlPrinter._div();
    }

    private void printUsers(String label, Collection<User> users) {
        htmlPrinter.h(3, label);
        htmlPrinter.tableDiv("resultSet");
        htmlPrinter.trh("id", "username", "display name", "email", "updated");
        for (User user : users) {
            htmlPrinter.trd(
                    String.format("<a href='/view/user/%s'>%s</a>", user.getId(), user.getId()),
                    user.getUserName(),
                    user.getDisplayName(),
                    user.getEmail(),
                    CalendarFormats.timestampFormat.format(user.getUpdated()));
        }
        htmlPrinter._table();
        htmlPrinter._div();
    }

    private void printCerts(String label, Collection<ClientCert> certs) {
        htmlPrinter.h(3, label);
        htmlPrinter.tableDiv("resultSet");
        htmlPrinter.trh("id", "org", "host", "service", "updated", "updated by");
        for (ClientCert cert : certs) {
            htmlPrinter.trd(
                    String.format("<a href='/view/cert/%s'>%s</a>", cert.getId(), cert.getId()),
                    cert.getOrgId(),
                    cert.getHostName(),
                    cert.getClientName(),
                    CalendarFormats.timestampFormat.format(cert.getUpdated()),
                    cert.getUpdatedBy());
        }
        htmlPrinter._table();
        htmlPrinter._div();
    }

    private void printSeviceRecords(String label, Collection<ServiceRecord> serviceRecords) {
        htmlPrinter.h(3, label);
        htmlPrinter.tableDiv("resultSet");
        for (ServiceRecord serviceRecord : serviceRecords) {
            htmlPrinter.trd(
                    String.format("<a href='/view/serviceRecord/%d'>%d</a>", serviceRecord.getId(), serviceRecord.getId()),
                    Millis.formatTimestamp(serviceRecord.getTimestamp()),
                    serviceRecord.getHostName(),
                    serviceRecord.getServiceName(),
                    serviceRecord.getServiceStatus());
        }
        htmlPrinter._tableDiv();
    }

    private void printLog(String label, Collection<LogrRecord> records) {
        printLog(label, records.iterator());
    }

    private void printLog(String label, Iterator<LogrRecord> iterator) {
        HtmlPrinter p = new HtmlPrinter(out);
        p.h(3, label);
        p.tableDiv("resultSet");
        p.thead();
        p._thead();
        p.tbody();
        while (iterator.hasNext()) {
            LogrRecord record = iterator.next();
            p.trd(Millis.formatTime(record.getTimestamp()),
                    record.getContext().getName(),
                    record.getLevel(), record.getMessage(),
                    ListFormats.displayFormatter.formatArray(record.getArgs()));
        }
        p._tbody();
        p._tableDiv();
    }
}
