/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package bizstat.entity;

import bizstat.enumtype.NotifyType;
import bizstat.enumtype.StatusChangeType;
import vellum.util.Args;
import bizstat.enumtype.ServiceStatus;
import bizstat.server.BizstatConfig;
import bizstat.server.BizstatServer;
import bizstat.server.HostServiceExecuter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.type.UniqueList;
import vellum.util.Strings;

/**
 *
 * @author evan
 */
public class HostServiceStatus implements Runnable {

    public static final int MAX_REPEAT_COUNT = 99;
    Logr logger = LogrFactory.getLogger(HostServiceStatus.class);
    BizstatServer server;
    BizstatConfig config;
    int repeatCount = MAX_REPEAT_COUNT;
    long dispatchedMillis;
    long changedMillis;
    long notifiedMillis;
    long receivedMillis;
    HostServiceKey key;
    Host host;
    Service service;
    ServiceStatus serviceStatus;
    ServiceStatus notifiedServiceStatus;
    StatusChangeType statusChangeType;
    StatusInfo statusInfo;
    StatusInfo notifiedStatusInfo;
    StatusInfo previousStatusInfo;
    List<ContactGroup> contactGroupList = new UniqueList();
    ScheduledFuture scheduledFuture;
    boolean outputChanged = false;
    
    public HostServiceStatus(BizstatServer server, HostServiceKey key) {
        this.server = server;
        this.config = server.getConfig();
        this.key = key;
        this.host = key.getHost();
        this.service = key.getService();
        contactGroupList.addAll(host.getNetwork().getContactGroupList());
        contactGroupList.addAll(host.getContactGroupList());
        contactGroupList.addAll(service.getContactGroupList());
    }

    public HostServiceKey getKey() {
        return key;
    }

    public List<ContactGroup> getContactGroupList() {
        return contactGroupList;
    }

    public void schedule(long initialDelay, long period) {
        this.scheduledFuture = server.getScheduledExecutorService().
                scheduleAtFixedRate(this, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        executeStatusInfo();
    }

    public void executeStatusInfo() {
        server.setStatusInfo(new HostServiceExecuter(server, key).execute());
    }

    public void setNotifiedStatusInfo(StatusInfo notifiedStatusInfo) {
        this.notifiedStatusInfo = notifiedStatusInfo;
        this.notifiedServiceStatus = notifiedStatusInfo.getServiceStatus();
        this.notifiedMillis = notifiedStatusInfo.getNotifiedMillis();
    }

    public ServiceStatus getNotifiedServiceStatus() {
        return notifiedServiceStatus;
    }

    public long getReceivedMillis() {
        return receivedMillis;
    }

    public long getNotifiedMillis() {
        return notifiedMillis;
    }

    public void setNotifiedMillis(long notifiedMillis) {
        this.notifiedStatusInfo = statusInfo;
        this.notifiedServiceStatus = serviceStatus;
        this.notifiedMillis = notifiedMillis;
    }

    public long getChangedMillis() {
        return changedMillis;
    }

    public void setChangedMillis(long changedMillis) {
        this.changedMillis = changedMillis;
    }

    public long getDispatchedMillis() {
        return dispatchedMillis;
    }

    public void setDispatchedMillis(long dispatchedMillis) {
        this.dispatchedMillis = dispatchedMillis;
    }

    public Host getHost() {
        return host;
    }

    public Service getService() {
        return service;
    }

    public ServiceStatus getServiceStatus() {
        return serviceStatus;
    }

    public StatusInfo getStatusInfo() {
        return statusInfo;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public StatusChangeType getStatusChangeType() {
        return statusChangeType;
    }

    private void setServiceStatus(ServiceStatus serviceStatus) {
        if (this.serviceStatus == null) {
            if (statusChangeType != null) {
                logger.warn("setServiceStatus notifyEventType");
            }
        } else if (serviceStatus == null) {
            statusChangeType = null;
            logger.warn("setServiceStatus");
        } else if (this.serviceStatus != serviceStatus) {
            statusChangeType = serviceStatus.getStatusChangeType(this.serviceStatus);
            if (statusChangeType == null) {
                logger.warn("setServiceStatus statusChangeType");
                statusChangeType = StatusChangeType.OK_WARNING;
            }
            changedMillis = System.currentTimeMillis();
            repeatCount = 0;
        } else if (repeatCount < MAX_REPEAT_COUNT) {
            repeatCount++;
        }
        this.serviceStatus = serviceStatus;
    }

    public void setStatusInfo(StatusInfo statusInfo) {
        previousStatusInfo = this.statusInfo;
        receivedMillis = System.currentTimeMillis();
        if (service.getNotifyType() == NotifyType.OUTPUT_CHANGED) {
            outputChanged = false;
            if (previousStatusInfo != null && !Strings.equals(previousStatusInfo.getOutList(), statusInfo.getOutList())) {
                outputChanged = true;
                statusInfo.setServiceStatus(ServiceStatus.WARNING);
            } else {
                statusInfo.setServiceStatus(ServiceStatus.OK);                
            }
        }
        if (statusInfo.getServiceStatus() == null) {
            statusInfo.setServiceStatus(ServiceStatus.UNKNOWN);
        }
        setServiceStatus(statusInfo.getServiceStatus());
        this.statusInfo = statusInfo;
        parseMetrics();
    }

    public boolean isOutputChanged() {
        return outputChanged;
    }
    
    private void parseMetrics() {
        Map<String, String> metrics = new HashMap();
        for (String line : statusInfo.outList) {
            if (line.startsWith("value ")) {
                logger.info("parseMetrics", line);
                String[] words = line.split(" ");
                if (words.length == 3) {
                    metrics.put(words[1], words[2]);
                }
            }
        }
        if (!metrics.isEmpty()) {
            setMetrics(metrics);
        }
    }

    private void setMetrics(Map<String, String> metrics) {
        logger.verbose("setMetrics", service.metrics.keySet());
        for (MetricInfo metric : service.metrics.values()) {
            String valueString = metrics.get(metric.getName());
            if (valueString != null) {
                try {
                    float value = Float.parseFloat(valueString);
                    logger.info("setStatusInfo value", valueString, value);
                } catch (NumberFormatException pe) {
                    logger.warn(pe, "parseMetrics", metric.getName(), valueString);
                }
            }
        }
    }

    public boolean isNotify() {
        if (receivedMillis > server.getDispatcherMillis() && serviceStatus != null && serviceStatus.isKnown()) {
            if (service.getNotifyType() == NotifyType.OUTPUT_CHANGED) {
                return outputChanged;
            } else if (service.getNotifyType() == NotifyType.ALWAYS) {
                if (service.getScheduleTime() != null) {
                    return true;
                }
            } else if (service.getNotifyType() == NotifyType.NONZERO) {
                if (serviceStatus != ServiceStatus.OK) {
                    if (notifiedMillis < server.getDispatcherMillis() - service.getNotifyMillis()) {
                        return true;
                    } else {
                        logger.info("ignore notifyMillis", service, service.getNotifyMillis(),
                                server.getDispatcherMillis() - notifiedMillis);
                    }
                }
            } else if (service.getNotifyType() == NotifyType.STATUS_CHANGED) {
                if (statusChangeType != null) {
                    Long notifyInterval = service.getNotifyIntervalMap().get(statusChangeType);
                    if (notifyInterval == null) {
                        logger.info("ignore notifyInterval null", service, statusChangeType);
                    } else if (notifyInterval.longValue() == 0) {
                        logger.info("ignore notifyInterval zero", service, statusChangeType);
                    } else if (repeatCount >= service.getRepeatCountMap().get(statusChangeType)) {
                        if (notifiedServiceStatus == null) {
                            notifiedServiceStatus = serviceStatus;
                            logger.info("initial", service, serviceStatus, statusChangeType, repeatCount);
                        } else if (serviceStatus != notifiedServiceStatus) {
                            return true;
                        }
                    } else {
                        logger.info("ignore repeatCount", service, serviceStatus, statusChangeType, repeatCount);
                    }
                } else {
                    logger.info("ignore statusChangeType", service, serviceStatus, statusChangeType);
                }
            } else {
                logger.warn("ignore notifyType", service.getNotifyType());
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return Args.format(host, service, serviceStatus, notifiedServiceStatus);
    }
}
