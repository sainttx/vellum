/*
 * Apache Software License 2.0
 * Contributed (2013) by Evan Summers via https://code.google.com/p/vellum
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
