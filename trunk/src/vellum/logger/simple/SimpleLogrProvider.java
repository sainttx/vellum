/*
 * Apache Software License 2.0
 * (c) Copyright 2012, Evan Summers
 */

package vellum.logger.simple;

import vellum.logger.*;

/**
 *
 * @author evanx
 */
public class SimpleLogrProvider implements LogrProvider {

    @Override
    public LogrHandler getHandler(LogrContext context) {
        return new SimpleLogrHandler(context);
    }

    @Override
    public LogrLevel getLevel() {
        return LogrLevel.DEBUG;
    }
}
