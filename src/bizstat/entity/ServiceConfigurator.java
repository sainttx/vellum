/*
 * Copyright Evan Summers
 * 
 */
package bizstat.entity;

import bizstat.enumtype.NotifyType;
import bizstat.enumtype.StatusChangeType;
import bizstat.enumtype.MetricType;
import bizstat.server.BizstatServer;
import vellum.datatype.Millis;
import java.util.Date;
import java.util.List;
import vellum.config.PropertiesMap;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evan
 */
public class ServiceConfigurator {
    Logr logger = LogrFactory.getLogger(ServiceConfigurator.class);
    BizstatService service;
    BizstatServer server;
    PropertiesMap properties;

    public ServiceConfigurator(BizstatServer server, PropertiesMap properties, BizstatService service) {
        this.server = server;
        this.properties = properties;
        this.service = service;
    }
    
    public void configure() {
        service.label = properties.getString("label", null);
        service.enabled = properties.getBoolean("enabled", true);
        service.host = server.getConfigStorage().get(Host.class, properties.getString("host", null));
        for (String contactGroupName : properties.splitCsv("contactGroups")) {            
            service.contactGroupList.add(server.getConfigStorage().find(ContactGroup.class, contactGroupName));
        }               
        service.notifyType = properties.getEnum("notifyType", NotifyType.class, NotifyType.STATUS_CHANGED);
        service.scheduleTime = properties.getTime("scheduleTime", null);
        if (service.scheduleTime != null) {
            if (service.notifyType == null) {
                service.notifyType = NotifyType.ALWAYS;
            }
            service.intervalMillis = properties.getMillis("interval", Millis.fromDays(1));
            if (false) {
                long millis = System.currentTimeMillis(); 
                millis -= millis % Millis.fromMinutes(10);
                millis += Millis.fromMinutes(10);
                service.scheduleTime = new Date(millis);
            }
        } else {
            service.intervalMillis = properties.getMillis("interval", server.getConfig().getIntervalMillis());
            if (service.notifyType == null) {
                service.notifyType = NotifyType.STATUS_CHANGED;
            } else if (service.notifyType != NotifyType.STATUS_CHANGED) {
                service.notifyMillis = properties.getMillis("notify", service.intervalMillis);
            }
            service.repeatCountMap.putAll(server.getConfig().getRepeatCountMap());
            List<String> repeatCounts = properties.splitCsv("repeatCounts");
            if (!repeatCounts.isEmpty()) {
                service.repeatCountMap.putAll(StatusChangeType.newIntegerMap(repeatCounts));
            }
            service.notifyIntervalMap.putAll(server.getConfig().getNotifyIntervalMap());
            List<String> notifyIntervals = properties.splitCsv("notifyIntervals");
            if (!notifyIntervals.isEmpty()) {
                service.notifyIntervalMap.putAll(StatusChangeType.newIntervalMap(notifyIntervals));
            }
            configureRepeatCount();
        }
        if (properties.containsKey("values")) {
            MetricInfo metricInfo = new MetricInfo(service, service.name);
            metricInfo.setMetricType(properties.getEnum("metricType", MetricType.class, MetricType.FLOAT));
            metricInfo.setValueMap(StatusChangeType.newValueMap(properties.splitCsv("values")));
        }
        for (String metricName : properties.splitCsv("metrics")) {
            MetricInfo metricInfo = server.getConfigStorage().get(MetricInfo.class, metricName);
            if (metricInfo == null) {
                metricInfo = new MetricInfo(service, metricName);
            }
            service.metrics.put(metricName, metricInfo);
        }
    }
    
    private void configureRepeatCount() {
        for (StatusChangeType notifyEventType : service.notifyIntervalMap.keySet()) {
            Integer repeatCount = service.repeatCountMap.get(notifyEventType);
            if (repeatCount != null) {
                Long notifyInterval = service.notifyIntervalMap.get(notifyEventType);
                if (notifyInterval != null) {
                    int minimumRepeatCount = (int) (notifyInterval / service.intervalMillis);
                    if (repeatCount < minimumRepeatCount) {
                        service.repeatCountMap.put(notifyEventType, minimumRepeatCount);
                    }
                    logger.verbose("configureRepeatCount", notifyEventType, repeatCount, notifyEventType, minimumRepeatCount);
                }
            }
        }
    }
}
