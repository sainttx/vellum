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
    LogrContext context;

    public LogrAdapter(LogrContext context, LogrHandler handler) {
        this.context = context;
        this.handler = handler;
    }

    private boolean isLevel(LogrLevel level) {
        return level.ordinal() >= context.getLevel().ordinal();
    }

    @Override
    public void trace(String message, Object... args) {
        if (isLevel(LogrLevel.TRACE)) {
            handler.handle(context, new LogrRecord(LogrLevel.TRACE, message, args));
        }
    }

    @Override
    public void verbose(String message, Object... args) {
        if (isLevel(LogrLevel.VERBOSE)) {
            handler.handle(context, new LogrRecord(LogrLevel.VERBOSE, message, args));
        }
    }

    @Override
    public void verboseArray(String message, Object[] args) {
        if (isLevel(LogrLevel.VERBOSE)) {
            handler.handle(context, new LogrRecord(LogrLevel.VERBOSE, message, args));
        }
    }
    
    @Override
    public void info(String message, Object... args) {
        if (isLevel(LogrLevel.INFO)) {
            handler.handle(context, new LogrRecord(LogrLevel.INFO, message, args));
        }
    }

    @Override
    public void infoArray(String message, Object[] args) {
        if (isLevel(LogrLevel.INFO)) {
            handler.handle(context, new LogrRecord(LogrLevel.INFO, message, args));
        }
    }
    
    @Override
    public void feature(String message, Object... args) {
        if (isLevel(LogrLevel.FEATURE)) {
            handler.handle(context, new LogrRecord(LogrLevel.INFO, message, args));
        }
    }
    
    @Override
    public void warn(String message, Object... args) {
        if (isLevel(LogrLevel.WARN)) {
            handler.handle(context, new LogrRecord(LogrLevel.WARN, message, args));
        }
    }

    @Override
    public void error(String message, Object... args) {
        if (isLevel(LogrLevel.ERROR)) {
            handler.handle(context, new LogrRecord(LogrLevel.ERROR, message, args));
        }
    }

    @Override
    public void warn(Throwable throwable) {
        if (isLevel(LogrLevel.WARN)) {
            handler.handle(context, new LogrRecord(throwable, LogrLevel.WARN));
        }
    }
    
    @Override
    public void warn(Throwable throwable, String message, Object... args) {
        if (isLevel(LogrLevel.WARN)) {
            handler.handle(context, new LogrRecord(throwable, LogrLevel.WARN, message, args));
        }
    }

    @Override
    public void error(Throwable throwable, String message, Object... args) {
        if (isLevel(LogrLevel.ERROR)) {
            handler.handle(context, new LogrRecord(throwable, LogrLevel.ERROR, message, args));
        }
    }

}
