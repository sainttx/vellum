/*
 * Apache Software License 2.0
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 */
package vellum.logr;

/**
 *
 * @author evans
 */
public class NullProvider implements LogrProvider {

    @Override
    public Logr getLogger(LogrContext context) {
        return new LogrAdapter(context, new NullLogrHandler());
    }

}
