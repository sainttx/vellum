/*
 * Apache Software License 2.0
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
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
