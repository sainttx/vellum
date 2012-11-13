/*
 * Apache Software License 2.0
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 */

package vellum.logr;

/**
 *
 * @author evanx
 */
public interface LogrProvider {

    public Logr getLogger(LogrContext context);
}
