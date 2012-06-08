/*
 * Apache Software License 2.0
 * (c) Copyright 2012, Evan Summers
 */

package vellum.logger.simple;

import vellum.logger.*;
import java.io.PrintStream;
import vellum.util.formatter.ArgFormatter;

/**
 *
 * @author evanx
 */
public class SimpleLogrHandler implements LogrHandler {
    LogrContext context;
    PrintStream err = System.err;
    LogrLevel level = LogrLevel.DEBUG;
    SimpleFormatter formatter = new SimpleFormatter();
    
    public SimpleLogrHandler(LogrContext context) {
        this.context = context;
    }

    @Override
    public void handle(LogrMessage message) {
        if (message.getLevel().ordinal() >= context.getLevel().ordinal()) {
            err.println(formatter.format(context, message));
            Throwable throwable = getThrowable(message.getArgs());
            if (throwable != null) {
                throwable.printStackTrace(err);
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
