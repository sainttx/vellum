/*
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 * (c) Copyright 2011, iPay (Pty) Ltd, Evan Summers
 */

package vellum.logger;

/**
 *
 * @author evanx
 */
public class LogrFactory {
    static ThreadLocal threadLocalLogger = new ThreadLocal();

    public static Logr getLogger(Class source) {
        return new Logr(source);
    }

    public static Logr getLogger(Thread thread) {
        Logr logger = new Logr(thread.getClass(), thread.getName());
        threadLocalLogger.set(logger);
        return logger;
    }

    public static Logr getThreadLogger(Class source) {
        return new Logr(source, Thread.currentThread().getName());
    }
    
    public static Logr getLogger(String name) {
        return new Logr(name);
    }
    
    public static final Logr globalLogger = new Logr("global");
}
