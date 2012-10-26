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
    
    public static boolean waitPort(int port, long millis, long sleep) {
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
    
}
