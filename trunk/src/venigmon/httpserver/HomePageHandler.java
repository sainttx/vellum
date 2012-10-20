/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package venigmon.httpserver;

import bizstat.entity.HostServiceKey;
import bizstat.entity.HostServiceStatus;
import bizstat1.entity.Service;
import bizstat.entity.StatusInfo;
import bizstat.server.BizstatMessageBuilder;
import bizstat.server.BizstatServer;
import java.util.Collection;
import java.util.Iterator;
import vellum.util.DateFormats;
import vellum.util.Strings;
import java.util.List;
import net.bizswitch.bizmon.bizlog.AbstractPageHandler;
import net.bizswitch.bizmon.bizlog.LogRecord;
import vellum.datatype.Millis;
import vellum.html.TablePrinter;
import vellum.logr.LogrFactory;
import vellum.logr.LogrRecord;
import vellum.util.ListFormatter;
import venigmon.storage.VenigmonStorage;

/**
 *
 * @author evans
 */
public class HomePageHandler extends AbstractPageHandler {

    BizstatServer context;
    VenigmonStorage storage;

    public HomePageHandler(BizstatServer context) {
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
        print("stored status", storage.getStatusInfoStorage().getList());
    }

    private void print(String label, Collection<StatusInfo> statusInfos) {
        out.printf("<h3>%s</h3>\n", label);
        out.printf("<div class='resultSet'>\n");
        out.printf("<table>\n");
        int index = 0;
        for (StatusInfo statusInfo : statusInfos) {
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
            if (status.getServiceStatus() != null && status.getStatusInfo() != null) {
                out.printf("<tr class=row%d><td>%s<td><b>%s</b><td>%s<td>%s\n",
                        ++index % 2,
                        key.getHost(), key.getService(), status.getServiceStatus(),
                        BizstatMessageBuilder.buildOutText(status.getStatusInfo()));
            }
        }
        out.printf("</table>\n");
        out.printf("</div>\n");
    }

    private void print(List<LogRecord> records) {
        out.printf("<h3>log</h3>\n");
        out.printf("<table class='resultSet'>\n");
        int rowCount = 0;
        for (LogRecord message : records) {
            out.printf("<tr class=row%d>\n", ++rowCount % 2);
            String string = Strings.escapeXml(message.getMessage().trim());
            if (message.getConciseMessage() != null) {
                string = message.getConciseMessage();
            }
            string = string.trim();
            StringBuilder detailBuilder = new StringBuilder();
            if (string.length() > 0) {
                detailBuilder.append(string);
            }
            if (message.getDetail() != null) {
                if (detailBuilder.length() > 0) {
                    detailBuilder.append("<br>");
                }
                detailBuilder.append("<pre>");
                detailBuilder.append(Strings.escapeXml(message.getDetail().trim()));
                detailBuilder.append("</pre>");
            }
            out.printf("<td>%s<td>%s<td><b>%s</b><td>%s\n",
                    DateFormats.formatTime(message.getTimestamp()),
                    message.getLevel(),
                    message.getCategory(),
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
                   ListFormatter.displayFormatter.formatArray(record.getArgs()));
       }
       p.tbodyClose();
       p.tableDivClose();
    }
    
}
