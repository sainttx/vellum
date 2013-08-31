/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
 * 
 */
package crocserver.notify;

import bizstat.enumtype.NotifyType;
import bizstat.enumtype.ServiceStatus;
import crocserver.app.CrocApp;
import crocserver.storage.servicerecord.ServiceRecord;
import java.text.MessageFormat;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.util.Strings;

/**
 *
 * @author evan.summers
 */
public class ServiceRecordProcessor {
    Logr logger = LogrFactory.getLogger(getClass());

    CrocApp app;
    NotifyType notifyType;
    ServiceRecord previousRecord; 
    ServiceRecord newRecord;
    boolean notify = false;
    
    public ServiceRecordProcessor(CrocApp app) {
        this.app = app;
    }

    public void process(NotifyType notifyType, ServiceRecord previousRecord, ServiceRecord newRecord) {
        this.notifyType = notifyType;
        this.previousRecord = previousRecord;
        this.newRecord = newRecord;
        processNotifyType();
    }

    public void processNotifyType() {
        if (notifyType == NotifyType.ALWAYS) {
            notify = true;
        } else if (notifyType == NotifyType.OUTPUT_CHANGED) {
            if (previousRecord == null) {
                notify = false;
            } else if (!Strings.equals(previousRecord.getOutText(), newRecord.getOutText())) {
                notify = true;
                logger.info("output changed", previousRecord.getOutText().length(), newRecord.getOutText().length());
            }    
        } else if (notifyType == NotifyType.NOT_OK) {
            if (newRecord.getServiceStatus() == ServiceStatus.WARNING || 
                    newRecord.getServiceStatus() == ServiceStatus.CRITICAL) {
                notify = true;
            }
        } else if (notifyType == NotifyType.STATUS_CHANGED) {
            if (previousRecord == null) {
                notify = false;
            } else if (previousRecord.getServiceStatus().isKnown() &&
                    newRecord.getServiceStatus().isKnown() &&
                    previousRecord.getServiceStatus() != newRecord.getServiceStatus()) {
                notify = true;
            }
        }
        if (notify) {
            newRecord.setNotify(notify);
            app.sendAdminGtalkMessage(MessageFormat.format("CHANGED @{0} {1} {2}/view/serviceRecord/{3}",
                    newRecord.getCertName(), newRecord.getServiceName(), app.getSecureUrl(), newRecord.getId()));
        }        
    }

    public boolean isNotify() {
        return notify;
    }  
    
}
