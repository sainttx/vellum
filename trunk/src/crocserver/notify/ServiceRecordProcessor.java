/*
 * Copyright Evan Summers
 * 
 */
package crocserver.notify;

import bizstat.enumtype.NotifyType;
import bizstat.enumtype.ServiceStatus;
import crocserver.storage.servicerecord.ServiceRecord;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.util.Strings;

/**
 *
 * @author evan
 */
public class ServiceRecordProcessor {
    Logr logger = LogrFactory.getLogger(getClass());

    NotifyType notifyType;
    ServiceRecord previousRecord; 
    ServiceRecord currentRecord;
    boolean notify = false;
    
    public ServiceRecordProcessor(NotifyType notifyType, ServiceRecord previousRecord, ServiceRecord currentRecord) {
        this.notifyType = notifyType;
        this.previousRecord = previousRecord;
        this.currentRecord = currentRecord;
    }

    public void process() {
        processNotifyType();
    }

    public void processNotifyType() {
        if (notifyType == NotifyType.ALWAYS) {
            notify = true;
        } else if (notifyType == NotifyType.OUTPUT_CHANGED) {
            if (!Strings.equals(previousRecord.getOutText(), currentRecord.getOutText())) {
                notify = true;
                logger.info("output changed", previousRecord.getOutText().length(), currentRecord.getOutText().length());
            }    
        } else if (notifyType == NotifyType.NOT_OK) {
            if (currentRecord.getServiceStatus() == ServiceStatus.WARNING || currentRecord.getServiceStatus() == ServiceStatus.CRITICAL) {
                notify = true;
            }
        } else if (notifyType == NotifyType.STATUS_CHANGED) {
            if (previousRecord.getServiceStatus().isKnown() &&
                    currentRecord.getServiceStatus().isKnown() &&
                    previousRecord.getServiceStatus() != currentRecord.getServiceStatus()) {
                notify = true;
            }
        }      
    }

    public boolean isNotify() {
        return notify;
    }        
}
