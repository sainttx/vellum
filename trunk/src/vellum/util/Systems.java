/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.util;

import java.net.InetAddress;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author evan
 */
public class Systems {
    public static Logr logger = LogrFactory.getLogger(Systems.class);

    public static final String osName = System.getProperty("os.name");
    public static final String userDir = System.getProperty("user.dir");
    public static final String homeDir = System.getProperty("user.home");

    public static boolean isLinux() {
        return osName.toLowerCase().startsWith("linux");
    }
    
    public static void sleep(long duration, TimeUnit timeUnit) {
        sleep(timeUnit.toMillis(duration));
    }
    
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            logger.warn(e, null);
        }
    }
   
    public static String getHostName() {
        String hostName = System.getProperty("hostName");
        if (hostName == null) {
            try {
                hostName = InetAddress.getLocalHost().getHostName();
            } catch (Exception e) {
                return e.getMessage();
            }
        }
        int index = hostName.indexOf(".");
        if (index > 0) {
            hostName = hostName.substring(0, index);
        }
        return hostName;
    }

    
}
