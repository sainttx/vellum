/*
 * Apache Software License 2.0
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
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
