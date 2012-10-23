/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package bizstat.server;

import vellum.util.Lists;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.util.Streams;
import bizstat.entity.*;
import java.util.*;
import vellum.config.PropertiesMap;

/**
 *
 * @author evan
 */
public class BizstatMessenger {

    Logr logger = LogrFactory.getLogger(BizstatMessenger.class);
    BizstatServer server;
    Contact contact;
    ProcessBuilder processBuilder = new ProcessBuilder();
    Map environment = processBuilder.environment();
    List<String> argList = new ArrayList();
    LinkedList<ServiceRecord> statusInfoList;

    public BizstatMessenger(BizstatServer server, Contact contact, List<ServiceRecord> statusInfoList) {
        this.server = server;
        this.contact = contact;
        this.statusInfoList = Lists.sortedReverseLinkedList(statusInfoList, 
                new StatusInfoServiceStatusComparator());
    }

    public void send() {
        ServiceRecord statusInfo = statusInfoList.getFirst();
        logger.info("send", statusInfoList.size(), statusInfo);
        String message = BizstatMessageBuilder.buildTextMessage(statusInfo);
        String text = statusInfo.getOutText();
        if (statusInfoList.size() > 1) {
            BizstatMessageBuilder builder = new BizstatMessageBuilder(statusInfoList);
            message = builder.buildMessage();
            text = builder.buildHtml();
            logger.info("send", text);
        }
        send(statusInfo, message, text);
        if (contact.getIm() != null) {
            if (server.gtalk != null) {
                try {
                    server.gtalk.sendMessage(contact, message);
                } catch (Exception e) {
                    logger.warn(e, "send", contact);
                }
            }
        }
    }

    private void send(ServiceRecord statusInfo, String message, String text) {
        logger.info("send", message);
        argList.add(server.config.notifyScript);
        argList.add(statusInfo.getHost().getName());
        argList.add(statusInfo.getService().getName());
        argList.add(statusInfo.getServiceStatus().name());
        argList.add(contact.getEmail());
        logger.info("notify send", argList);
        processBuilder.command(argList);
        environment.put("notify_host", statusInfo.getHost().getName());
        environment.put("notify_service", statusInfo.getService().getName());
        environment.put("notify_status", statusInfo.getServiceStatus().name());
        environment.put("notify_message", message);
        put("notify_", contact.getPropertiesMap());
        send(text);
    }

    private void send(String text) {
        if (server.getConfig().isRun()) {
            try {
                Process process = processBuilder.start();
                process.getOutputStream().write(text.getBytes());
                process.getOutputStream().close();
                int exitCode = process.waitFor();
                String outText = Streams.readString(process.getInputStream());
                String errText = Streams.readString(process.getErrorStream()).trim();
                if (errText.length() > 0) {
                    logger.warn(errText);
                }
                logger.verbose("notify exec", exitCode, outText.length(), outText);
                if (exitCode != 0) {
                    logger.warn("notify stderr", outText);
                } else {
                }
            } catch (Exception e) {
                logger.warn(e, "notify", argList);
            }
        }
    }

    private void put(String prefix, PropertiesMap propertiesMap) {
        for (String key : propertiesMap.keySet()) {
            environment.put(prefix + key, propertiesMap.get(key));
        }
    }
}
