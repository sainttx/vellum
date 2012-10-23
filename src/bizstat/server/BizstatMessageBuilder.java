/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 */
package bizstat.server;

import vellum.util.DateFormats;
import vellum.util.Lists;
import bizstat.entity.ServiceRecord;
import bizstat.enumtype.ServiceStatus;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import vellum.datatype.IntegerCounterMap;

/**
 *
 * @author evans
 */
public class BizstatMessageBuilder {

    List<ServiceRecord> statusInfos;
    StringBuilder messageBuilder = new StringBuilder();
    IntegerCounterMap<ServiceStatus> counterMap = new IntegerCounterMap();

    public BizstatMessageBuilder(LinkedList<ServiceRecord> statusInfos) {
        this.statusInfos = statusInfos;
    }

    public String buildHtml() {
        StringBuilder textBuilder = new StringBuilder();
        textBuilder.append(spanStyle("font-size: 14pt;", buildMessage()));
        for (ServiceRecord statusInfo : statusInfos) {
            textBuilder.append("\n<hr>");
            textBuilder.append(spanStyle("font-size: 12pt;", buildHtmlMessage(statusInfo)));
            textBuilder.append("\n<br>");
            textBuilder.append(statusInfo.getOutText());
            textBuilder.append("\n");
            if (statusInfo.getErrText().length() > 0) {
                textBuilder.append(String.format("\n<b>stderr:</b>\n"));
                textBuilder.append(statusInfo.getErrText());
                textBuilder.append("\n");
            }
        }
        return textBuilder.toString();
    }

    private String spanStyle(String style, String content) {
        return String.format("<span style=\"%s\">%s</span>", style, content);
    }

    public String buildMessage() {
        if (messageBuilder.length() == 0) {
            for (ServiceRecord statusInfo : statusInfos) {
                counterMap.increment(statusInfo.getServiceStatus());
            }
            append(ServiceStatus.CRITICAL);
            append(ServiceStatus.WARNING);
            append(ServiceStatus.OK);
        }
        return messageBuilder.toString();
    }

    private void append(ServiceStatus serviceStatus) {
        if (counterMap.getInt(serviceStatus) > 0) {
            if (messageBuilder.length() > 0) {
                messageBuilder.append("; ");
            }
            messageBuilder.append(String.format("%d %s", counterMap.getInt(serviceStatus), serviceStatus));
            StringBuilder builder = new StringBuilder();
            for (ServiceRecord statusInfo : statusInfos) {
                if (statusInfo.getServiceStatus() == serviceStatus) {
                    if (builder.length() > 0) {
                        builder.append(", ");
                    }
                    builder.append(String.format("%s",
                            statusInfo.getService().getName(),
                            statusInfo.getHost().getName()));
                }
            }
            messageBuilder.append(" (");
            messageBuilder.append(builder.toString());
            messageBuilder.append(")");
        }
    }

    public static String buildHtmlMessage(ServiceRecord statusInfo) {
        StringBuilder builder = new StringBuilder();
        builder.append(DateFormats.timeFormat.format(new Date(statusInfo.getTimestamp())));
        builder.append(" ");
        builder.append("<i>");
        builder.append(statusInfo.getHost().getName());
        builder.append("</i>");
        builder.append(" ");
        builder.append("<b>");
        builder.append(statusInfo.getService().getName());
        builder.append("</b>");
        builder.append(" ");
        builder.append(statusInfo.getServiceStatus().name());
        return builder.toString();
    }

    public static String buildTextMessage(ServiceRecord statusInfo) {
        StringBuilder builder = new StringBuilder();
        builder.append(DateFormats.timeFormat.format(new Date(statusInfo.getTimestamp())));
        builder.append(" ");
        builder.append(statusInfo.getHost().getName());
        builder.append(" ");
        builder.append(statusInfo.getService().getName());
        builder.append(" ");
        builder.append(statusInfo.getServiceStatus().name());
        if (statusInfo.getOutText() != null) {
            String text = statusInfo.getOutText().trim();
            int index = text.lastIndexOf("\n");
            if (index > 0) {
                builder.append(" - ");
                builder.append(text.substring(index + 1));
            } else {
                builder.append(" - ");
                builder.append(text);
            }
        }
        return builder.toString();
    }
    
    public static String buildOutText(ServiceRecord statusInfo) {
        if (statusInfo.getOutText() != null) {
            String text = statusInfo.getOutText().trim();
            int index = text.lastIndexOf("\n");
            if (index > 0) {
                return text.substring(index + 1);
            } else {
                return text;
            }
        }
        return "";
    }
    
}
