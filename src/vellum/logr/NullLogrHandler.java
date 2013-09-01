/*
 * Apache Software License 2.0
       Source https://code.google.com/p/vellum by @evanxsummers
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
