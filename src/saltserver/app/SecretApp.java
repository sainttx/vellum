/*
 * Apache Software License 2.0, (c) Copyright 2012 Evan Summers
 * 
 */
package saltserver.app;

import vellum.httpserver.HttpServerConfig;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLConnection;
import org.h2.tools.Server;
import vellum.config.ConfigMap;
import vellum.config.ConfigParser;
import vellum.config.PropertiesMap;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.logr.LogrLevel;
import vellum.storage.DataSourceConfig;
import vellum.storage.SimpleConnectionPool;
import vellum.util.Streams;
import saltserver.storage.schema.SaltSchema;
import vellum.crypto.Base64;
import vellum.crypto.PBECipher;
import vellum.httpserver.VellumHttpsServer;
import vellum.security.DefaultKeyStores;

/**
 *
 * @author evan
 */
public class SecretApp {

    Logr logger = LogrFactory.getLogger(getClass());
    SecretAppStorage storage;
    DataSourceConfig dataSourceConfig;
    PropertiesMap configProperties;
    Thread serverThread;
    String confFileName;
    ConfigMap configMap;
    Server h2Server;
    VellumHttpsServer httpsServer;
    PBECipher cipher; 
    
    public void init() throws Exception {
        initConfig();
        sendShutdown();
        if (configProperties.getBoolean("startH2TcpServer")) {
            h2Server = Server.createTcpServer().start();
        }
        dataSourceConfig = new DataSourceConfig(configMap.get("DataSource",
                configProperties.getString("dataSource")).getProperties());
        storage = new SecretAppStorage(new SimpleConnectionPool(dataSourceConfig));
        storage.init();
        new SaltSchema(storage).verifySchema();
        String httpsServerConfigName = configProperties.getString("httpsServer");
        if (httpsServerConfigName != null) {
            HttpServerConfig httpsServerConfig = new HttpServerConfig(
                    configMap.find("HttpsServer", httpsServerConfigName).getProperties());
            if (httpsServerConfig.isEnabled()) {
                httpsServer = new VellumHttpsServer(httpsServerConfig);
                httpsServer.init(DefaultKeyStores.createSSLContext());
            }
        }
        String pbeSalt = configProperties.getString("pbeSalt");
        String pbeSecret = configProperties.getString("pbeSecret");
        cipher = new PBECipher(pbeSecret.toCharArray(), Base64.decode(pbeSalt));
    }
    
    private void initConfig() throws Exception {
        confFileName = getString("salt.conf");
        File confFile = new File(confFileName);
        logger.info("conf", confFileName, confFile);
        configMap = ConfigParser.newInstance(new FileInputStream(confFile));
        configProperties = configMap.find("Config", "default").getProperties();
        String logLevelName = configProperties.get("logLevel");
        if (logLevelName != null) {
            LogrFactory.setDefaultLevel(LogrLevel.valueOf(logLevelName));
        }
    }

    public void start() throws Exception {
        if (httpsServer != null) {
            httpsServer.start();
            httpsServer.startContext("/", new SecretHttpHandler(this));
            logger.info("HTTPS server started");
        }
    }

    public void sendShutdown() {
        String shutdownUrl = configProperties.getString("shutdownUrl");
        logger.info("sendShutdown", shutdownUrl);
        try {
            URL url = new URL(shutdownUrl);
            URLConnection connection = url.openConnection();
            String response = Streams.readString(connection.getInputStream());
            connection.getInputStream().close();
            logger.info(response);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    public void stop() throws Exception {
        if (httpsServer != null) {
            httpsServer.stop();
        }
        if (h2Server != null) {
            h2Server.stop();
        }
    }

    private String getString(String name) {
        String string = System.getProperty(name);
        if (string == null) {
            throw new RuntimeException(name);
        }
        return string;
    }

    public SecretAppStorage getStorage() {
        return storage;
    }

    public PBECipher getCipher() {
        return cipher;
    }
    
    public static void main(String[] args) throws Exception {
        try {
            SecretApp starter = new SecretApp();
            starter.init();
            starter.start();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
