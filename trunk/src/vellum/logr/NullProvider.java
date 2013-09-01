/*
 * Apache Software License 2.0
       Source https://code.google.com/p/vellum by @evanxsummers
 */
package vellum.logr;

/**
 *
 * @author evan.summers
 */
public class NullProvider implements LogrProvider {

    @Override
    public Logr getLogger(LogrContext context) {
        return new LogrAdapter(context, new NullLogrHandler());
    }

}
