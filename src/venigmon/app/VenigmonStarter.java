/*
 * Copyright Evan Summers
 * 
 */
package venigmon.app;

import bizstat.server.BizstatServer;
import bizstat.server.BizstatStarter;
import java.io.File;
import java.io.FileInputStream;
import vellum.config.ConfigMap;
import vellum.config.ConfigParser;
import vellum.config.PropertiesMap;
import vellum.datatype.SimpleEntityCache;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.logr.LogrLevel;
import vellum.storage.DataSourceInfo;
import vellum.storage.SimpleConnectionPool;
import venigmon.httpserver.HttpServerConfig;
import venigmon.httpserver.VenigmonHttpServer;
import venigmon.storage.VenigmonStorage;

/**
 *
 * @author evan
 */
public class VenigmonStarter {
    static final String MAIN_CONFIG_FILE = "venigmon.conf";
    
    VenigmonStorage storage;
    VenigmonHttpServer httpServer;
    HttpServerConfig httpServerConfig;
    DataSourceInfo dataSourceInfo;

    Logr logger = LogrFactory.getLogger(BizstatStarter.class);
    ConfigMap configMap;
    PropertiesMap configProperties;
    BizstatServer server;
    Thread serverThread;
    
    String confDirName;
    File confDir;
    
    public void init() {
        confDirName = System.getProperty("confDir");
        if (confDirName == null) {
            throw new RuntimeException("no confDir");
        }
        confDir = new File(confDirName);
        logger.info(confDir.getAbsolutePath());
        try {
            initConfigMap();
            dataSourceInfo = new DataSourceInfo("org.h2.Driver", "jdbc:h2:mem", "sa", null, true, 1);
            storage = new VenigmonStorage(new SimpleEntityCache(), new SimpleConnectionPool(dataSourceInfo));
            httpServerConfig = new HttpServerConfig(8080, true);
            httpServer = new VenigmonHttpServer(storage, httpServerConfig);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }                
    }
    
    public void start() throws Exception {
        httpServer.start();
    }
  
    private void initConfigMap() throws Exception {
        configMap = ConfigParser.newInstance(new FileInputStream(new File(confDirName, MAIN_CONFIG_FILE)));
        configProperties = configMap.find("Config", "default").getProperties();
        String logLevelName = configProperties.get("logLevel");
        if (logLevelName != null) {
            LogrFactory.setDefaultLevel(LogrLevel.valueOf(logLevelName));
        }
    }
    
    public static void main(String[] args) throws Exception {
        VenigmonStarter starter = new VenigmonStarter();        
        starter.init();
        starter.start();
    }
}
