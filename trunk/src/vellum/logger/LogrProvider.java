/*
 * Apache Software License 2.0
 * (c) Copyright 2012, Evan Summers
 */

package vellum.logger;

/**
 *
 * @author evanx
 */
public interface LogrProvider {

    public LogrLevel getLevel();
    public LogrHandler getHandler(LogrContext context);
}
