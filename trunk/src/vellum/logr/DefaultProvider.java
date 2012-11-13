/*
 * Apache Software License 2.0
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 */

package vellum.logr;

import vellum.logr.*;

/**
 *
 * @author evanx
 */
public class DefaultProvider implements LogrProvider {

    public DefaultProvider() {
    }
    
    @Override
    public Logr getLogger(LogrContext context) {
        return new LogrAdapter(context, new DefaultHandler());
    }

}
