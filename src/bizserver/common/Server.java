/*
 * (c) Copyright 2010, iPay (Pty) Ltd, Evan Summers
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 */
package bizserver.common;

import bizmon.logger.Logr;
import bizmon.logger.LogrFactory;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author evanx
 */
public class Server {

    Logr logger = LogrFactory.getLogger(getClass());
    int serverPort = Integer.getInteger("serverPort", 8099);
    int shutdownPort = Integer.getInteger("shutdownPort", 8098);
    HttpServer server;

    protected void start() throws Exception {
        requestShutdown();
        server = HttpServer.create(new InetSocketAddress(serverPort), 4);
        server.createContext("/echo", new EchoHandler());
        server.createContext("/", new GenericPageHandler(HomePageHandler.class));
        createContext();
        server.setExecutor(new ThreadPoolExecutor(4, 8, 0, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(4)));
        server.start();
        new ServerSocket(shutdownPort).accept();
        logger.info("bizmon restarted");
        System.exit(0);
    }

    protected void requestShutdown() {
        try {
            URL url = new URL("http://localhost:" + shutdownPort);
            URLConnection connection = url.openConnection();
            connection.connect();
            connection.getContentLength();
            logger.info("restarting server");
        } catch (Exception e) {
            logger.info("starting server");
        }
    }

    protected void createContext(String context, Class type) {
        logger.info(context, type);
        server.createContext(context, new GenericPageHandler(type));
    }

    protected void createContext() {
        for (PageHandlerInfo info : PageHandlerInfoManager.getInstance().getHandlerInfoList()) {
            createContext("/" + info.getName(), info.getType());
        }
    }

    public static void main(String[] args) throws Exception {
        new Server().start();
    }
}
