/*
 * Apache Software License 2.0
 * (c) Copyright 2012, Evan Summers
 */
package vellum.logr.jul;

import vellum.logr.*;

/**
 *
 * @author evans
 */
public class JulProvider implements LogrProvider {

    @Override
    public Logr getLogger(LogrContext context) {
        return new JulAdapter(context);
    }

}
