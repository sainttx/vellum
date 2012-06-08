/*
 * Apache Software License 2.0
 * (c) Copyright 2012, Evan Summers
 */
package vellum.logger;

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
    public void trace(Object... args) {
        handler.handle(new LogrMessage(LogrLevel.TRACE, args));
    }

    @Override
    public void debug(Object... args) {
        handler.handle(new LogrMessage(LogrLevel.DEBUG, args));
    }
    
    @Override
    public void info(Object... args) {
        handler.handle(new LogrMessage(LogrLevel.INFO, args));
    }

    @Override
    public void warn(Object... args) {
        handler.handle(new LogrMessage(LogrLevel.WARN, args));
    }

    @Override
    public void error(Object... args) {
        handler.handle(new LogrMessage(LogrLevel.ERROR, args));
    }
}
