/*
 * Apache Software License 2.0
 * (c) Copyright 2012, Evan Summers
 */

package vellum.logr.simple;

import vellum.logr.LogrHandler;
import vellum.logr.LogrContext;
import vellum.logr.LogrRecord;
import vellum.logr.LogrLevel;
import java.io.PrintStream;
import vellum.logr.other.SimpleFormatter;
import vellum.util.ArgFormatter;

/**
 *
 * @author evanx
 */
public class SimpleLogrHandler implements LogrHandler {
    LogrContext context;
    PrintStream err = System.err;
    LogrLevel level = LogrLevel.DEBUG;
    SimpleFormatter formatter = new SimpleFormatter();
    
    public SimpleLogrHandler(LogrContext context, LogrLevel level) {
        this.context = context;
        this.level = level;
    }

    @Override
    public void handle(LogrRecord record) {
        if (record.getLevel().ordinal() >= level.ordinal()) {
            err.println(formatter.format(context, record));
            if (record.getThrowable() != null) {
                record.getThrowable().printStackTrace(err);
            }
        }
    }
    
    Throwable getThrowable(Object[] args) {
        if (args.length > 0 && args[0] instanceof Throwable) {
            return (Throwable) args[0];
        }
        return null;
    }    
}
