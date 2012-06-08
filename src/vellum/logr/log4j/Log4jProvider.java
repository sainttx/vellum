/*
 * Apache Software License 2.0
 * (c) Copyright 2012, Evan Summers
 */
package vellum.logr.log4j;

import vellum.logr.*;

/**
 *
 * @author evans
 */
public class Log4jProvider implements LogrProvider {

    @Override
    public Logr getLogger(LogrContext context) {
        return new Log4jAdapter(context);
    }

}
