/*
 * Copyright Evan Summers
 * 
 */
package crocserver.app;

import bizstat.server.BizstatServer;
import crocserver.gtalk.GtalkConnection;
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
import java.security.Security;
import vellum.httpserver.VellumHttpServer;
import vellum.httpserver.VellumHttpsServer;
import vellum.security.KeyStores;

/**
 *
 * @author evan
 */
public class CrocStarter {

    Logr logger = LogrFactory.getLogger(getClass());
    CrocStorage storage;
    DataSourceConfig dataSourceConfig;
    PropertiesMap configProperties;
    BizstatServer server;
    Thread serverThread;
    String confFileName;
    ConfigMap configMap;
    Server h2Server;
    VellumHttpServer httpServer;
    VellumHttpsServer publicHttpsServer;
    VellumHttpsServer privateHttpsServer;
    CrocTrustManager trustManager;
    GtalkConnection gtalkConnection;
    
    public void init() throws Exception {
        initConfig();
        if (configProperties.getBoolean("startH2TcpServer")) {
            h2Server = Server.createTcpServer().start();
        }
        dataSourceConfig = new DataSourceConfig(configMap.get("DataSource",
                configProperties.getString("dataSource")).getProperties());
        storage = new CrocStorage(new SimpleEntityCache(), new SimpleConnectionPool(dataSourceConfig));
        trustManager = new CrocTrustManager(storage);
        trustManager.init();
        new CrocSchema(storage).verifySchema();
        String httpServerConfigName = configProperties.getString("httpServer");
        if (httpServerConfigName != null) {
            HttpServerConfig httpServerConfig = new HttpServerConfig(
                    configMap.find("HttpServer", httpServerConfigName).getProperties());
            if (httpServerConfig.isEnabled()) {
                httpServer = new VellumHttpServer(httpServerConfig);
            }
        }
        String publicHttpsServerConfigName = configProperties.getString("publicHttpsServer");
        if (publicHttpsServerConfigName != null) {
            if (false) {
                System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
                Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            }
            HttpServerConfig httpsServerConfig = new HttpServerConfig(
                    configMap.find("HttpsServer", publicHttpsServerConfigName).getProperties());
            if (httpsServerConfig.isEnabled()) {
                publicHttpsServer = new VellumHttpsServer(httpsServerConfig);
                publicHttpsServer.init(KeyStores.createSSLContext());
            }
        }
        String privateHttpsServerConfigName = configProperties.getString("privateHttpsServer");
        if (privateHttpsServerConfigName != null) {
            HttpServerConfig httpsServerConfig = new HttpServerConfig(
                    configMap.find("HttpsServer", privateHttpsServerConfigName).getProperties());
            if (httpsServerConfig.isEnabled()) {
                privateHttpsServer = new VellumHttpsServer(httpsServerConfig);
                privateHttpsServer.init(KeyStores.createSSLContext(trustManager));
            }
        }
        String gtalkConfigName = configProperties.getString("gtalk");
        if (gtalkConfigName != null) {
            gtalkConnection = new GtalkConnection(configMap.find("Gtalk", gtalkConfigName).getProperties());
        }                
    }

    public void start() throws Exception {
        if (httpServer != null) {
            httpServer.start();
            httpServer.startContext("/", new CrocHttpHandler(storage));
            logger.info("HTTP server started");
        }
        if (publicHttpsServer != null) {
            publicHttpsServer.start();
            publicHttpsServer.startContext("/", new CrocHttpHandler(storage));
            logger.info("public HTTPS secure server started");
        }
        if (privateHttpsServer != null) {
            privateHttpsServer.start();
            privateHttpsServer.startContext("/", new CrocHttpHandler(storage));
            logger.info("private HTTPS secure server started");
        }
        if (gtalkConnection != null) {
            gtalkConnection.open();
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
        if (httpServer != null) {
            httpServer.stop();
        }
        if (publicHttpsServer != null) {
            publicHttpsServer.stop();
        }
        if (privateHttpsServer != null) {
            privateHttpsServer.stop();
        }
        if (h2Server != null) {
            h2Server.stop();
        }
        if (gtalkConnection != null) {
            gtalkConnection.close();
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
