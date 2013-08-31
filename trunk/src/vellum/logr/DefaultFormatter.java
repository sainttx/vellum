/*
 * Apache Software License 2.0
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
 */
package vellum.logr;

import vellum.util.Args;

/**
 *
 * @author evan.summers
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
        if (record.getArgs() != null && record.getArgs().length > 0) {
            builder.append(": ");
            builder.append(format(record.getArgs()));
        }
        return builder.toString();
    }
    
    String format(Object[] args) {
        return Args.format(args);
    }
 
}
