/*
 * Apache Software License 2.0
 * Licensed to the Apache Software Foundation (ASF) by Evan Summers
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
