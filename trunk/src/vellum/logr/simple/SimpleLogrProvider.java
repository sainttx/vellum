/*
 * Apache Software License 2.0
 * (c) Copyright 2012, Evan Summers
 */

package vellum.logr.simple;

import vellum.logr.other.Logr;
import vellum.logr.other.LogrAdapter;
import vellum.logr.other.LogrContext;
import vellum.logr.other.LogrLevel;
import vellum.logr.other.LogrProvider;

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
