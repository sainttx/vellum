/*
 * Apache Software License 2.0
 * Licensed to the Apache Software Foundation (ASF) by Evan Summers
 */

package vellum.logr;

/**
 *
 * @author evan.summers
 */
public interface LogrProvider {

    public Logr getLogger(LogrContext context);
}
