/*
 * Copyright Evan Summers
 * 
 */
package crocserver.app;

import bizstat.server.BizstatServer;
import crocserver.httpserver.CrocHttpHandler;
import crocserver.httpserver.HttpServerConfig;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import org.h2.tools.Server;
import vellum.config.ConfigMap;
import vellum.config.ConfigParser;
import vellum.config.PropertiesMap;
import vellum.datatype.SimpleEntityCache;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.logr.LogrLevel;
import vellum.storage.DataSourceConfig;
import vellum.storage.SimpleConnectionPool;
import vellum.util.Streams;
import vellum.util.Threads;
import crocserver.storage.CrocSchema;
import crocserver.storage.CrocStorage;
import vellum.httpserver.VellumHttpServer;
import vellum.httpserver.VellumHttpsServer;
import vellum.util.Keystores;

/**
 *
 * @author evan
 */
public class CrocStarter {

    Logr logger = LogrFactory.getLogger(CrocStarter.class);
    CrocStorage storage;
    DataSourceConfig dataSourceConfig;
    PropertiesMap configProperties;
    BizstatServer server;
    Thread serverThread;
    String confFileName;
    ConfigMap configMap;
    Server h2Server;
    VellumHttpServer httpServer;
    VellumHttpsServer httpsServer;
    CrocTrustManager trustManager;
    
    public void init() throws Exception {
        initConfig();
        if (configProperties.getBoolean("startH2TcpServer")) {
            h2Server = Server.createTcpServer().start();    
        }            
        dataSourceConfig = new DataSourceConfig(configMap.get("DataSource", 
                configProperties.getString("dataSource")).getProperties());
        storage = new CrocStorage(new SimpleEntityCache(), new SimpleConnectionPool(dataSourceConfig));
        trustManager = new CrocTrustManager(storage);
        new CrocSchema(storage).verifySchema();
        String httpServerConfigName = configProperties.getString("httpServer", null);
        if (httpServerConfigName != null) {
            HttpServerConfig httpServerConfig = new HttpServerConfig(
                    configMap.find("HttpServer", httpServerConfigName).getProperties());
            if (httpServerConfig.isEnabled()) {
                httpServer = new VellumHttpServer(httpServerConfig);
                httpServer.createContext("/", new CrocHttpHandler(storage));
            }
        }
        String httpsServerConfigName = configProperties.getString("httpsServer", null);
        if (httpsServerConfigName != null) {
            HttpServerConfig httpsServerConfig = new HttpServerConfig(
                    configMap.find("HttpsServer", httpsServerConfigName).getProperties());
            if (httpsServerConfig.isEnabled()) {
                httpsServer = new VellumHttpsServer(httpsServerConfig);
                httpsServer.init(Keystores.createSSLContext(trustManager));
                httpsServer.createContext("/", new CrocHttpHandler(storage));
            }
        }
    }

    public void start() throws Exception {
        if (httpServer != null) {
            httpServer.start();
            logger.info("HTTP server started");
        }
        if (httpsServer != null) {
            httpsServer.start();
            logger.info("HTTPS secure server started");
        }        
        if (configProperties.getBoolean("testPost", false)) {
            try {
                testPost();
                Threads.sleep(16000);
            } finally {
                stop();
            }
        }
    }
    
    private void testPost() throws IOException {
        URL url = new URL(configProperties.getString("testPostUrl"));
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);
        PrintWriter out = new PrintWriter(connection.getOutputStream());
        out.println("hello");
        out.close();
        String response = Streams.readString(connection.getInputStream());
        logger.info(response);
    }

    public void stop() throws Exception {
        httpServer.stop();
        if (h2Server != null) {
            h2Server.stop();
        }
    }
    
    private void initConfig() throws Exception {
        confFileName = getString("conf");
        configMap = ConfigParser.newInstance(new FileInputStream(new File(confFileName)));
        configProperties = configMap.find("Config", "default").getProperties();
        String logLevelName = configProperties.get("logLevel");
        if (logLevelName != null) {
            LogrFactory.setDefaultLevel(LogrLevel.valueOf(logLevelName));
        }
    }

    private String getString(String name) {
        String string = System.getProperty(name);
        if (string == null) {
            throw new RuntimeException(name);
        }
        return string;
    }
    
    public static void main(String[] args) throws Exception {
        try {
            CrocStarter starter = new CrocStarter();
            starter.init();
            starter.start();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
