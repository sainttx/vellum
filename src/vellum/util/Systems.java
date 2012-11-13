/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.util;

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
    
}
