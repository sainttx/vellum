/*
 * Apache Software License 2.0
       Source https://code.google.com/p/vellum by @evanxsummers
 */
package vellum.logr;

/**
 *
 * @author evan.summers
 */
public interface LogrHandler {
   
    public void handle(LogrContext context, LogrRecord message);

}
