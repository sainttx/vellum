/*
 * Apache Software License 2.0
 * (c) Copyright 2012, Evan Summers
 */
package vellum.logr.simple;

import vellum.logr.LogrContext;
import vellum.logr.LogrFormatter;
import vellum.logr.LogrMessage;
import vellum.util.formatter.ArgFormatter;

/**
 *
 * @author evans
 */
public class SimpleFormatter implements LogrFormatter {

    @Override
    public String format(LogrContext context, LogrMessage message) {
        StringBuilder builder = new StringBuilder();
        builder.append(context.getName());
        builder.append(" ");        
        builder.append(message.getLevel().name());        
        builder.append(" ");        
        builder.append(ArgFormatter.formatter.formatArgs(message.getArgs()));
        return builder.toString();
    }
    
}
