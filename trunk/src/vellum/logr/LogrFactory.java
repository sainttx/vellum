/*
 * Apache Software License 2.0
 * (c) Copyright 2012, Evan Summers
 */

package vellum.logr;

import org.slf4j.LoggerFactory;

/**
 *
 * @author evanx
 */
public class LogrFactory {
    static LogrProvider provider = newProvider();
    
    static ThreadLocal threadLocalLogger = new ThreadLocal();

    static Logr getLogger(LogrContext context) {
        return provider.getLogger(context);
    }
    
    public static Logr getLogger(Class source) {
        return getLogger(new LogrContext(provider, source, source.getClass().getSimpleName()));
    }

    public static Logr getLogger(Thread thread) {
        Logr logger = getLogger(new LogrContext(provider, thread.getClass(), thread.getName()));
        threadLocalLogger.set(logger);
        return logger;
    }

    public static Logr getThreadLogger(Class source) {
        return getLogger(new LogrContext(provider, source, Thread.currentThread().getName()));
    }
    
    private static LogrProvider newProvider() {
        String provider = System.getProperty("logr.provider");
        if (provider != null) {
            try {
                return (LogrProvider) Class.forName(provider).newInstance();
            } catch (Throwable e) {
                throw new RuntimeException(provider, e);
            }
        } else {
            return new SimpleLogrProvider();
        }
    }
}
