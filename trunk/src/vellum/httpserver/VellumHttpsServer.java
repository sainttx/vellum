/*
 */
package vellum.httpserver;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import crocserver.httpserver.HttpServerConfig;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;
import vellum.lifecycle.Startable;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.util.Threads;

/**
 *
 * @author evans
 */
public class VellumHttpsServer implements Startable {
    private Logr logger = LogrFactory.getLogger(VellumHttpsServer.class);
    SSLContext sslContext;
    HttpsServer httpsServer;
    HttpServerConfig config;     
    ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 8, 0, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(4));
    
    public VellumHttpsServer(HttpServerConfig config) {
        this.config = config;
    }    

    private boolean portAvailable(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.close();
            return true;
        } catch (Exception e) {
            logger.warn("portAvailable", e.getMessage());
            return false;
        }
    }
    
    private boolean waitPort(int port, long millis, long sleep) {
        long time = System.currentTimeMillis() + millis;
        while (!portAvailable(port)) {
            if (System.currentTimeMillis() > time) {
                return false;
            }
            Threads.sleep(sleep);
            logger.warn("waitPort");
        }
        return true;
    }

    public void start(HttpHandler httpHandler) throws Exception {
        start();
        httpsServer.createContext("/", httpHandler);
    }
        
    public void start() throws Exception {
        init();
        waitPort(config.getPort(), 4000, 500);
        InetSocketAddress socketAddress = new InetSocketAddress(config.getPort());
        httpsServer = HttpsServer.create(socketAddress, 4);
        httpsServer.setHttpsConfigurator(createHttpsConfigurator());
        httpsServer.setExecutor(executor);
        httpsServer.start();
        logger.info("start", config.getPort());
    }

    protected void init() throws Exception {
        sslContext = SSLContext.getInstance("TLS");
        char[] password = System.getProperty("javax.net.ssl.keyStorePassword").toCharArray();
        KeyStore ks = KeyStore.getInstance("JKS");
        FileInputStream fis = new FileInputStream(System.getProperty("javax.net.ssl.keyStore"));
        ks.load(fis, password);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, password);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());        
    }
    
    private HttpsConfigurator createHttpsConfigurator() throws Exception {
        return new HttpsConfigurator(sslContext) {

            @Override
            public void configure(HttpsParameters httpsParameters) {
                SSLContext sslContext = getSSLContext();
                InetSocketAddress remote = httpsParameters.getClientAddress();
                if (remote.getHostName().equals("localhost")) {
                }
                SSLParameters defaultSSLParameters = sslContext.getDefaultSSLParameters();
                defaultSSLParameters.setNeedClientAuth(false);
                httpsParameters.setSSLParameters(defaultSSLParameters);
            }
        };
    }
    
    public void createContext(String contextName, HttpHandler httpHandler) {
        httpsServer.createContext(contextName, httpHandler);
    }

    @Override
    public boolean stop() {
        if (httpsServer != null) {
            httpsServer.stop(0); 
            executor.shutdown();
            return true;
        }        
        return false;
    }    
}
