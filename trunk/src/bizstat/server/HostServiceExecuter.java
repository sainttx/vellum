/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package bizstat.server;

import bizstat.entity.*;
import vellum.util.Lists;
import vellum.util.Streams;
import bizstat.enumtype.ServiceStatus;
import java.io.InputStream;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evan
 */
public class HostServiceExecuter {
        
    Logr logger = LogrFactory.getLogger(HostServiceExecuter.class);
    BizstatServer server;
    BizstatConfig config;
    HostServiceKey key;
    ServiceRecord statusInfo;
    
    public HostServiceExecuter(BizstatServer server, Host host, Service service) {
        this.server = server;
        this.config = server.getConfig();
        this.key = key;
        this.statusInfo = new ServiceRecord(host, service, server.dispatcherMillis);
    }

    public ServiceRecord execute() {
        return exec(config.getCheckScript(), key.getService().getName(), key.getHost().getName());
    }

    private ServiceRecord exec(String... args) {
        logger.verbose("exec", Lists.format(args));
        statusInfo.setArgs(args);
        if (config.isExec()) {
            try {
                Process process = Runtime.getRuntime().exec(args);
                InputStream inputStream = process.getInputStream();
                InputStream errorStream = process.getErrorStream();
                statusInfo.setExitCode(process.waitFor());
                statusInfo.setOutList(Streams.readLineList(inputStream, config.getOutputSize()));
                statusInfo.setErrText(Streams.readString(errorStream));
                logger.verbose("exec", statusInfo.getExitCode(), statusInfo.getServiceStatus());
                if (statusInfo.getOutText().length() > 0) {
                }
            } catch (Exception e) {
                logger.warn(e, null);
                statusInfo.setServiceStatus(ServiceStatus.INDETERMINATE);
            }
        } else {
            statusInfo.setServiceStatus(ServiceStatus.DISABLED);
        }
        statusInfo.setTimestampMillis(System.currentTimeMillis());
        return statusInfo;
    }        

    
}
