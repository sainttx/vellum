/*
       Source https://code.google.com/p/vellum by @evanxsummers
 */
package vellum.logr;

/**
 *
 * @author evan.summers
 */
public class LogrFactory {

    final static DequerProvider dequerProvider = new DequerProvider();
    static LogrProvider provider = dequerProvider;
    static ThreadLocal threadLocalLogger = new ThreadLocal();
    static LogrLevel defaultLevel = LogrLevel.INFO;

    public static DequerProvider getDequerProvider() {
        return dequerProvider;
    }
    
    public static LogrProvider newProvider(String providerName) {
        try {
            return (LogrProvider) Class.forName(providerName).newInstance();
        } catch (Throwable e) {
            throw new RuntimeException(providerName, e);
        }
    }
    
    public static void setProvider(LogrProvider provider) {
        LogrFactory.provider = provider;
    }
            
    public static void setDefaultLevel(LogrLevel defaultLevel) {
        LogrFactory.defaultLevel = defaultLevel;
    }

    public static LogrLevel getDefaultLevel() {
        return defaultLevel;
    }

    public static Logr getLogger(Class source) {
        return getLogger(new LogrContext(provider, defaultLevel, source, source.getSimpleName()));
    }

    public static Logr getLogger(Thread thread) {
        Logr logger = getLogger(new LogrContext(provider, defaultLevel, thread.getClass(), thread.getName()));
        threadLocalLogger.set(logger);
        return logger;
    }

    public static Logr getThreadLogger(Class source) {
        return getLogger(new LogrContext(provider, defaultLevel, source, Thread.currentThread().getName()));
    }

    private static Logr getLogger(LogrContext context) {
        return provider.getLogger(context);
    }
}
