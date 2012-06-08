/*
 * Apache Software License 2.0
 * (c) Copyright 2012, Evan Summers
 */

package vellum.logr.simple;

import vellum.logr.*;

/**
 *
 * @author evanx
 */
public class SimpleLogrProvider implements LogrProvider {

    @Override
    public Logr getLogger(LogrContext context) {
        return new LogrAdapter(new SimpleLogrHandler(context, LogrLevel.DEBUG));
    }

}
