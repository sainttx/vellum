/*
 * Apache Software License 2.0
 * (c) Copyright 2012, Evan Summers
 */
package vellum.logr;

/**
 *
 * @author evans
 */
public class LogrAdapter implements Logr {
    LogrHandler handler;

    public LogrAdapter(LogrHandler handler) {
        this.handler = handler;
    }
    
    @Override
    public void trace(String message, Object... args) {
        handler.handle(new LogrRecord(LogrLevel.TRACE, message, args));
    }

    @Override
    public void debug(String message, Object... args) {
        handler.handle(new LogrRecord(LogrLevel.DEBUG, message, args));
    }
    
    @Override
    public void info(String message, Object... args) {
        handler.handle(new LogrRecord(LogrLevel.INFO, message, args));
    }

    @Override
    public void warn(String message, Object... args) {
        handler.handle(new LogrRecord(LogrLevel.WARN, message, args));
    }

    @Override
    public void error(String message, Object... args) {
        handler.handle(new LogrRecord(LogrLevel.ERROR, message, args));
    }
    
    @Override
    public void warning(Throwable throwable, String message, Object... args) {
        handler.handle(new LogrRecord(throwable, LogrLevel.WARN, message, args));
    }

    @Override
    public void error(Throwable throwable, String message, Object... args) {
        handler.handle(new LogrRecord(throwable, LogrLevel.ERROR, message, args));
    }
}
