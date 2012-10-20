/*
 * Apache Software License 2.0
 * (c) Copyright 2012, Evan Summers
 */
package vellum.logr.other;

/**
 *
 * @author evans
 */
public class LogrRecord {
    LogrLevel level;
    Throwable throwable;
    String message;
    Object[] args;
    
    public LogrRecord(LogrLevel level, String message, Object[] args) {
        this.level = level;
        this.message = message;
        this.args = args;
    }

    public LogrRecord(Throwable throwable, LogrLevel level, String message, Object[] args) {
        this(level, message, args);
        this.throwable = throwable;
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

}
