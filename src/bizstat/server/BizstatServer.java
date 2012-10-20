/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package bizstat.server;

import venigmon.httpserver.BizstatHttpServer;
import bizstat.entity.*;
import bizstat.enumtype.ServiceStatus;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.h2.tools.Server;
import vellum.config.ConfigMap;
import vellum.config.PropertiesMap;
import venigmon.storage.VenigmonStorage;

/**
 *
 * @author evan
 */
public class BizstatServer implements Runnable {

    Logr logger = LogrFactory.getLogger(BizstatServer.class);
    ConfigMap configMap;
    PropertiesMap configProperties;
    BizstatConfig config;
    BizstatConfigStorage configStorage;
    VenigmonStorage dataStorage;
    BizstatHttpServer httpServer;
    BizstatGtalkConnection gtalk;
    boolean stopped = false;
    List<Network> networkList = new ArrayList();
    List<Host> hostList;
    List<ServicePath> servicePathList;
    List<Service> serviceList;
    long startMillis = System.currentTimeMillis();
    long dispatcherMillis;
    long notifiedMillis;
    Map<HostServiceKey, HostServiceStatus> statusMap = new ConcurrentHashMap();
    ScheduledExecutorService scheduledExecutorService;
    Contact adminContact;
    Set<Contact> adminContacts = new TreeSet();
    Server h2Server;

    public BizstatServer() {
    }

    public void init(ConfigMap configMap, PropertiesMap configProperties) throws Exception {
        this.configMap = configMap;
        this.configProperties = configProperties;
        config = new BizstatConfig(this);
        config.init();
        if (config.isH2()) {
            h2Server = Server.createTcpServer().start();            
        }    
        if (config.getDataSourceInfo() != null && config.getDataSourceInfo().isEnabled()) {
            dataStorage = new VenigmonStorage(this, config.getDataSourceInfo());
            dataStorage.init();
        }
        configStorage = new BizstatConfigStorage(this);
        configStorage.init(configMap);
        adminContact = configStorage.find(Contact.class, config.adminContact);
        if (config.verbose) {
            new BizstatStorageHtmlPrinter(this).print(System.out);
        }
        for (String networkName : config.networks) {
            networkList.add(configStorage.find(Network.class, networkName));
        }
        if (!configMap.getList("Gtalk").isEmpty() && configMap.get("Gtalk", "default") != null && 
                configMap.get("Gtalk", "default").getProperties().getBoolean("enabled")) {
            gtalk = new BizstatGtalkConnection(this);
            gtalk.connect();
        }
        hostList = configStorage.getExtentList(Host.class);
        servicePathList = configStorage.getExtentList(ServicePath.class);
        serviceList = configStorage.getExtentList(Service.class);
        logger.info("init", hostList.size());
        scheduledExecutorService = Executors.newScheduledThreadPool(config.threadPoolSize);
        test();
    }
    
    public void start() throws Exception {
        if (config.getHttpServerConfig() != null && config.getHttpServerConfig().isEnabled()) {
            httpServer = new BizstatHttpServer(this, config.httpServerConfig);
            httpServer.start();
        }        
    }
    
    void test() {
        HostServiceKey key = new HostServiceKey(hostList.get(0), serviceList.get(0));
        HostServiceStatus status = getStatus(key);
        StatusInfo statusInfo = new StatusInfo(key, System.currentTimeMillis());
        statusInfo.setServiceStatus(ServiceStatus.UNKNOWN);
        statusInfo.setTimestampMillis(System.currentTimeMillis());
        statusInfo.setExitCode(2);
        dataStorage.insert(statusInfo);
    }
    
    public void sendAdminMessage(String message) {
        if (gtalk != null && !isStopped()) {
            try {
                gtalk.sendMessage(adminContact, message);
            } catch (Exception e) {
                logger.warn(e, "sendAdminMessage", message);
            }
        }
    }

    public void shutdown(boolean restarting) {
        scheduledExecutorService.shutdown();
        if (!restarting && h2Server != null) {
            h2Server.stop();
        }
        if (gtalk != null) {
            gtalk.close();
        }
        if (httpServer != null) {
            httpServer.stop();
        }
        logger.info("stopped");
    }

    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public boolean isStopped() {
        return stopped;
    }

    @Override
    public void run() {
        sendAdminMessage("Started " + getClass().getSimpleName());
        new BizstatScheduler(this).run();
        while (!stopped) {
            dispatcherMillis = System.currentTimeMillis();
            new BizstatDispatcher(this).run();
            if (dispatcherMillis - notifiedMillis < config.notifyMillis) {
                logger.warn("ignore notifyMillis", dispatcherMillis - notifiedMillis);
            } else {
                runNotifier();
            }
        }
    }

    private synchronized void runNotifier() {
        new BizstatNotifier(this).run();
    }

    public HostServiceStatus getStatus(HostServiceKey key) {
        HostServiceStatus status = statusMap.get(key);
        if (status == null) {
            status = new HostServiceStatus(this, key);
            statusMap.put(status.getKey(), status);
        }
        return status;
    }

    public synchronized void setStatusInfo(StatusInfo statusInfo) {
        HostServiceStatus status = getStatus(statusInfo.getKey());
        status.setStatusInfo(statusInfo);
        logger.info("setStatusInfo", statusInfo);
    }

    public VenigmonStorage getDataStorage() {
        return dataStorage;
    }

    public BizstatConfigStorage getConfigStorage() {
        return configStorage;
    }

    public BizstatConfig getConfig() {
        return config;
    }

    public ConfigMap getConfigMap() {
        return configMap;
    }

    public PropertiesMap getConfigProperties() {
        return configProperties;
    }

    public long getDispatcherMillis() {
        return dispatcherMillis;
    }

    public Set<Contact> getAdminContacts() {
        return adminContacts;
    }

    public Map<HostServiceKey, HostServiceStatus> getStatusMap() {
        return statusMap;
    }

    public List<Service> getServiceList() {
        return serviceList;
    }

        
}
