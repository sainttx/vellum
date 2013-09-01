/*
 * Apache Software License 2.0
       Source https://code.google.com/p/vellum by @evanxsummers
 */

package vellum.logr;

/**
 *
 * @author evan.summers
 */
public interface LogrProvider {

    public Logr getLogger(LogrContext context);
}
