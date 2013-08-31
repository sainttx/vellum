/*
 * Apache Software License 2.0
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
 */
package vellum.logr;

/**
 *
 * @author evan.summers
 */
public class NullLogrHandler implements LogrHandler {

    @Override
    public void handle(LogrContext context, LogrRecord message) {
    }
    
    
}
