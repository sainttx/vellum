/*
 * Copyright Evan Summers
 * 
 */
package vellum.util;

import java.net.ServerSocket;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evan
 */
public class Sockets {
    public static Logr logger = LogrFactory.getLogger(Sockets.class);
    
    public static boolean portAvailable(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.close();
            return true;
        } catch (Exception e) {
            logger.warn("portAvailable", e.getMessage());
            return false;
        }
    }

    public static boolean waitPort(int port, long timeoutMillis, long sleepMillis) {
        long time = System.currentTimeMillis() + timeoutMillis;
        while (!portAvailable(port) && System.currentTimeMillis() < time) {
            Threads.sleep(sleepMillis);
            logger.warn("waitPort");
        }
        return true;
    }
    
}
