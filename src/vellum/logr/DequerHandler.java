/*
 * Apache Software License 2.0
 * (c) Copyright 2012, Evan Summers
 */

package vellum.logr;

import vellum.datatype.Millis;
import vellum.datatype.TimestampedDequer;

/**
 *
 * @author evanx
 */
public class DequerHandler implements LogrHandler {
    LogrContext context;
    TimestampedDequer<LogrRecord> dequer = new TimestampedDequer(Millis.fromMinutes(5));
    DefaultFormatter formatter = new DefaultFormatter();
    
    public DequerHandler() {
    }

    @Override
    public void handle(LogrContext context, LogrRecord record) {
        if (record.getLevel().ordinal() >= context.getLevel().ordinal()) {
            record.setContext(context);
            dequer.addLast(record);
        }
    }

    public TimestampedDequer<LogrRecord> getDequer() {
        return dequer;
    }        
}
