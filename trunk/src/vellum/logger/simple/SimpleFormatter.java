/*
 * Apache Software License 2.0
 * (c) Copyright 2012, Evan Summers
 */
package vellum.logger.simple;

import vellum.logger.LogrContext;
import vellum.logger.LogrFormatter;
import vellum.logger.LogrMessage;
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
