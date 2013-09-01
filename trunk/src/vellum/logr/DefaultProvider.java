/*
 * Apache Software License 2.0
       Source https://code.google.com/p/vellum by @evanxsummers
 */

package vellum.logr;

import vellum.logr.*;

/**
 *
 * @author evan.summers
 */
public class DefaultProvider implements LogrProvider {

    public DefaultProvider() {
    }
    
    @Override
    public Logr getLogger(LogrContext context) {
        return new LogrAdapter(context, new DefaultHandler());
    }

}
