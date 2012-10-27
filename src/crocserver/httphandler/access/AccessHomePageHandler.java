/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package crocserver.httphandler.access;

import bizstat.entity.ServiceRecord;
import bizstat.server.BizstatMessageBuilder;
import crocserver.httphandler.common.AbstractPageHandler;
import crocserver.storage.adminuser.AdminUser;
import java.util.Collection;
import java.util.Iterator;
import vellum.datatype.Millis;
import vellum.html.HtmlPrinter;
import vellum.logr.LogrFactory;
import vellum.logr.LogrRecord;
import vellum.format.ListFormats;
import crocserver.storage.CrocStorage;
import crocserver.storage.servicekey.ServiceKey;
import vellum.format.CalendarFormats;
import vellum.logr.LogrLevel;

/**
 *
 * @author evans
 */
public class AccessHomePageHandler extends AbstractPageHandler {

    CrocStorage storage;

    public AccessHomePageHandler(CrocStorage storage) {
        super();
        this.storage = storage;
    }

    @Override
    protected void handle() throws Exception {
        htmlPrinter.div("menuBarDiv");
        htmlPrinter.a_("/", "Home");
        htmlPrinter._div();
        printAdminUsers("admin users", storage.getAdminUserStorage().getList());
        printSeviceRecords("service records", storage.getServiceRecordStorage().getList());
        if (LogrFactory.getDefaultLevel().ordinal() < LogrLevel.INFO.ordinal()) {
            printLog("log", LogrFactory.getDequerProvider().getDequerHandler().getDequer().tailDescending(100));
        }
    }

    private void printServiceKeys(String label, Collection<ServiceKey> serviceKeys) {
        htmlPrinter.h(3, label);
        htmlPrinter.tableDiv("resultSet");
        htmlPrinter.trh("id", "username", "display name", "email");
        for (ServiceKey serviceKey : serviceKeys) {
            htmlPrinter.trd(
                    String.format("<a href='/view/serviceKey/%s'>%s</a>", serviceKey.getId(), serviceKey.getId()),
                    serviceKey.getAdminUserName(),
                    serviceKey.getHostName(),
                    serviceKey.getServiceName(),
                    CalendarFormats.timestampFormat.format(serviceKey.getCreated()));
        }
        htmlPrinter._table();
        htmlPrinter._div();
    }
    
    private void printAdminUsers(String label, Collection<AdminUser> adminUsers) {
        htmlPrinter.h(3, label);
        htmlPrinter.tableDiv("resultSet");
        htmlPrinter.trh("id", "username", "display name", "email");
        for (AdminUser adminUser : adminUsers) {
            htmlPrinter.trd(
                    String.format("<a href='/view/adminUser/%s'>%s</a>", adminUser.getId(), adminUser.getId()),
                    adminUser.getUsername(),
                    adminUser.getDisplayName(),
                    adminUser.getEmail(),
                    CalendarFormats.timestampFormat.format(adminUser.getCreated()));
        }
        htmlPrinter._table();
        htmlPrinter._div();
    }

    private void printSeviceRecords(String label, Collection<ServiceRecord> serviceRecords) {
        htmlPrinter.h(3, label);
        htmlPrinter.tableDiv("resultSet");
        int index = 0;
        for (ServiceRecord serviceRecord : serviceRecords) {
            out.printf("<tr class=row%d><td><a href='/view/serviceRecord/%d'>%d</a><td>%s<td>%s<td><b>%s</b><td>%s<td>%s\n",
                    ++index % 2,
                    serviceRecord.getId(),
                    serviceRecord.getId(),
                    Millis.formatTime(serviceRecord.getTimestamp()),
                    serviceRecord.getHostName(),
                    serviceRecord.getServiceName(),
                    serviceRecord.getServiceStatus(),
                    BizstatMessageBuilder.buildOutText(serviceRecord));
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
