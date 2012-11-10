/*
 * Apache Software License 2.0
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2012, Evan Summers
 */
package vellum.logr;

import vellum.util.Args;

/**
 *
 * @author evans
 */
public class DefaultFormatter implements LogrFormatter {

    @Override
    public String format(LogrContext context, LogrRecord record) {
        StringBuilder builder = new StringBuilder();
        builder.append(record.getLevel().name());
        builder.append(" ");
        builder.append(context.toString());
        builder.append(" ");
        builder.append(record.getMessage());
        if (record.getArgs().length > 0) {
            builder.append(": ");
            builder.append(format(record.getArgs()));
        }
        return builder.toString();
    }
    
    String format(Object[] args) {
        return Args.format(args);
    }
 
}
