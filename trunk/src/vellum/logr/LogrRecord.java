/*
 * Apache Software License 2.0
 *    https://code.google.com/p/vellum - Contributed by Evan Summers
 */
package vellum.logr;

import vellum.datatype.Timestamped;

/**
 *
 * @author evan.summers
 */
public class LogrRecord implements Timestamped {
    LogrLevel level;
    Throwable throwable;
    String message;
    Object[] args;
    long timestamp = System.currentTimeMillis();
    LogrContext context;
    
    public LogrRecord(LogrLevel level, String message, Object[] args) {
        this.level = level;
        this.message = message;
        this.args = args;
    }

    public LogrRecord(Throwable throwable, LogrLevel level, String message, Object[] args) {
        this(level, message, args);
        this.throwable = throwable;
    }

    public LogrRecord(Throwable throwable, LogrLevel level) {
        this.level = level;
        this.throwable = throwable;
    }
    
    public void setContext(LogrContext context) {
        this.context = context;
    }

    public LogrContext getContext() {
        return context;
    }
        
    public LogrLevel getLevel() {
        return level;
    }

    public Throwable getThrowable() {
        return throwable;
    }
    
    public String getMessage() {
        return message;
    }

    public Object[] getArgs() {
        return args;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

}
