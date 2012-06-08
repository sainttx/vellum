/*
 * Apache Software License 2.0
 * (c) Copyright 2012, Evan Summers
 */
package vellum.logr;

import vellum.logr.LogrContext;
import vellum.logr.LogrFormatter;
import vellum.logr.LogrRecord;
import vellum.util.formatter.ArgFormatter;

/**
 *
 * @author evans
 */
public class SimpleFormatter implements LogrFormatter {

    @Override
    public String format(LogrContext context, LogrRecord message) {
        StringBuilder builder = new StringBuilder();
        builder.append(context.getName());
        builder.append(" ");        
        builder.append(message.getLevel().name());
        if (message.getArgs().length > 0) {
            builder.append(" ");        
            builder.append(format(message.getArgs()));
        }
        return builder.toString();
    }
    
    String format(Object[] args) {
        return ArgFormatter.formatter.formatArgs(args);
    }
 
}
