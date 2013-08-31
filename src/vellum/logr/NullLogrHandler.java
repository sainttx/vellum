/*
 * Apache Software License 2.0
 *    https://code.google.com/p/vellum - Contributed by Evan Summers
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
