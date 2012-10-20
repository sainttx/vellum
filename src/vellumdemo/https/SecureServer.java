/*
 */
package vellumdemo.https;

import com.sun.net.httpserver.*;
import java.io.FileInputStream;
import java.io.IOException;
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
import vellumdemo.servlet.EchoHandler;

/**
 *
 * @author evan
 */
public class SecureServer {

    SSLContext sslContext;
    
    public SecureServer() {
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
        HttpsServer httpsServer = HttpsServer.create(new InetSocketAddress(443), 8);
        httpsServer.setHttpsConfigurator(httpsConfigurator);
        httpsServer.setExecutor(new ThreadPoolExecutor(4, 8, 0, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(4)));
        httpsServer.createContext("/", new HttpHandler() {

            @Override
            public void handle(HttpExchange he) throws IOException {
                new EchoHandler().handle(he);
            }
        });
        httpsServer.start();
    }

    public static final void main(String[] args) {
        try {
            new SecureServer().start();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
