/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package bizstat.http;

import venigmon.httpserver.*;
import bizstat.entity.HostServiceKey;
import bizstat.entity.HostServiceStatus;
import bizstat.entity.Service;
import bizstat.entity.ServiceRecord;
import bizstat.server.BizstatMessageBuilder;
import bizstat.server.BizstatServer;
import java.util.Collection;
import java.util.Iterator;
import vellum.util.DateFormats;
import vellum.util.Strings;
import java.util.List;
import vellum.datatype.Millis;
import vellum.html.TablePrinter;
import vellum.logr.LogrFactory;
import vellum.logr.LogrRecord;
import vellum.format.ListFormats;
import venigmon.storage.CrocStorage;

/**
 *
 * @author evans
 */
public class BizstatHomePageHandler extends AbstractPageHandler {

    BizstatServer context;
    CrocStorage storage;

    public BizstatHomePageHandler(BizstatServer context) {
        super();
        this.context = context;
        this.storage = context.getDataStorage();       
    }

    @Override
    protected void handle() throws Exception {
        printStatus();
        selectStatus();
        if (false) {
            printServices();
        }
        printLog(LogrFactory.getDequerProvider().getDequerHandler().getDequer().tailDescending(100));
    }

    private void selectStatus() throws Exception {
        print("stored status", storage.getServiceRecordStorage().getList());
    }

    private void print(String label, Collection<ServiceRecord> statusInfos) {
        out.printf("<h3>%s</h3>\n", label);
        out.printf("<div class='resultSet'>\n");
        out.printf("<table>\n");
        int index = 0;
        for (ServiceRecord statusInfo : statusInfos) {
            out.printf("<tr class=row%d><td>%s<td>%s<td><b>%s</b><td>%s<td>%s\n",
                    ++index % 2,
                    Millis.formatTime(statusInfo.getTimestamp()),
                    statusInfo.getHost(),
                    statusInfo.getService(),
                    statusInfo.getServiceStatus(),
                    BizstatMessageBuilder.buildOutText(statusInfo));
        }
        out.printf("</table>\n");
        out.printf("</div>\n");
    }

    private void printServices() {
        out.printf("<h3>services</h3>\n");
        out.printf("<table>\n");
        for (Service service : context.getServiceList()) {
            out.printf("<tr><td><i>%s</i>\n", service.getName());
        }
        out.printf("</table>\n");
        out.println("<hr>");
    }

    private void printStatus() {
        out.printf("<h3>latest status</h3>\n");
        out.printf("<div class='resultSet'>\n");
        out.printf("<table>\n");
        int index = 0;
        for (HostServiceKey key : context.getStatusMap().keySet()) {
            HostServiceStatus status = context.getStatusMap().get(key);
            if (status.getServiceStatus() != null && status.getServiceRecord() != null) {
                out.printf("<tr class=row%d><td>%s<td><b>%s</b><td>%s<td>%s\n",
                        ++index % 2,
                        key.getHost(), key.getService(), status.getServiceStatus(),
                        BizstatMessageBuilder.buildOutText(status.getServiceRecord()));
            }
        }
        out.printf("</table>\n");
        out.printf("</div>\n");
    }

    private void print(List<LogrRecord> records) {
        out.printf("<h3>log</h3>\n");
        out.printf("<table class='resultSet'>\n");
        int rowCount = 0;
        for (LogrRecord message : records) {
            out.printf("<tr class=row%d>\n", ++rowCount % 2);
            String string = Strings.escapeXml(message.getMessage().trim());
            string = string.trim();
            StringBuilder detailBuilder = new StringBuilder();
            if (string.length() > 0) {
                detailBuilder.append(string);
            }
            if (message.getMessage() != null) {
                if (detailBuilder.length() > 0) {
                    detailBuilder.append("<br>");
                }
                detailBuilder.append("<pre>");
                detailBuilder.append(Strings.escapeXml(message.getMessage().trim()));
                detailBuilder.append("</pre>");
            }
            out.printf("<td>%s<td>%s<td><b>%s</b><td>%s\n",
                    DateFormats.formatTime(message.getTimestamp()),
                    message.getLevel(),
                    message.getContext().getName(),
                    detailBuilder.toString());
        }
        out.printf("</table>\n");
    }

    private void printLog(Collection<LogrRecord> records) {
        printLog(records.iterator());
    }
    
    private void printLog(Iterator<LogrRecord> iterator) {
       TablePrinter p = new TablePrinter(out);
       p.h(3, "log");
       p.tableDiv("resultSet");
       p.thead();
       p.theadClose();
       p.tbody();
       while (iterator.hasNext()) {
           LogrRecord record = iterator.next();
           p.trd(Millis.formatTime(record.getTimestamp()), 
                   record.getContext().getName(),
                   record.getLevel(), record.getMessage(),
                   ListFormats.displayFormatter.formatArray(record.getArgs()));
       }
       p.tbodyClose();
       p.tableDivClose();
    }
    
}
