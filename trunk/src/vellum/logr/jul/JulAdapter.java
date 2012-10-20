/*
 * Apache Software License 2.0
 * (c) Copyright 2012, Evan Summers
 */
package vellum.logr.jul;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import vellum.logr.LogrContext;
import vellum.logr.LogrHandler;
import vellum.logr.LogrLevel;
import vellum.logr.LogrRecord;
import vellum.util.Args;

/**
 *
 * @author evans
 */
public class JulAdapter implements LogrHandler {
    Logger logger;
    
    public JulAdapter(LogrContext context) {
        logger = Logger.getLogger(context.getName());
    }

    @Override
    public void handle(LogrRecord record) {
        LogRecord logRecord = new LogRecord(getLevel(record.getLevel()), format(record));
        logger.log(logRecord);
    }

    String format(LogrRecord message) {
        return Args.format(message.getArgs());
    }
    
    Level getLevel(LogrLevel level) {
        if (level == LogrLevel.TRACE) return Level.FINER;
        else if (level == LogrLevel.VERBOSE) return Level.FINE;
        else if (level == LogrLevel.INFO) return Level.INFO;
        else if (level == LogrLevel.WARN) return Level.WARNING;
        else if (level == LogrLevel.ERROR) return Level.SEVERE;
        return Level.OFF;
    }
}
