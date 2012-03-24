/*
 */
package vellum.venigma;

import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;

/**
 *
 * @author evan
 */
public class VenigmaServer {

    SSLContext sslContext;
    
    public VenigmaServer() {
    }
    
    private void init() throws Exception {
        sslContext = SSLContext.getInstance("TLS");
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        KeyStore ks = KeyStore.getInstance("JKS");
        FileInputStream fis = new FileInputStream(System.getProperty("javax.net.ssl.keyStore"));
        char[] password = System.getProperty("javax.net.ssl.keyStorePassword").toCharArray();
        ks.load(fis, password);
        kmf.init(ks, password);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        KeyStore ts = KeyStore.getInstance("JKS");
        FileInputStream trustStoreStream = new FileInputStream(System.getProperty("javax.net.ssl.trustStore"));
        ts.load(trustStoreStream, password);
        tmf.init(ts);
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());                
    }
    
    protected void start() throws Exception {
        init();
        HttpsConfigurator httpsConfigurator = new HttpsConfigurator(sslContext) {

            @Override
            public void configure(HttpsParameters httpsParameters) {
                SSLContext sslContext = getSSLContext();
                InetSocketAddress remote = httpsParameters.getClientAddress();
                if (remote.getHostName().equals("localhost")) {
                }
                SSLParameters defaultSSLParameters = sslContext.getDefaultSSLParameters();
                defaultSSLParameters.setNeedClientAuth(true);
                httpsParameters.setSSLParameters(defaultSSLParameters);
            }
        };
        InetSocketAddress isa = new InetSocketAddress(InetAddress.getLocalHost(), VenigmaApp.config.port);
        HttpsServer httpsServer = HttpsServer.create(isa, VenigmaApp.config.backlog);
        httpsServer.setHttpsConfigurator(httpsConfigurator);
        httpsServer.setExecutor(new ThreadPoolExecutor(4, 8, 0, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(4)));
        httpsServer.createContext("/", new VenigmaHandler());
        httpsServer.start();
    }

    public static void main(String[] args) {
        try {
            new VenigmaServer().start();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
