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
public class Threads {
    public static Logr logger = LogrFactory.getLogger(Threads.class);

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
