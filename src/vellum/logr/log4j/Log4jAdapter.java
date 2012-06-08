/*
 * Apache Software License 2.0
 * (c) Copyright 2012, Evan Summers
 */
package vellum.logr.log4j;

import java.util.logging.Logger;
import vellum.logr.Logr;
import vellum.logr.LogrContext;

/**
 *
 * @author evans
 */
public class Log4jAdapter implements Logr {
    
    public Log4jAdapter(LogrContext context) {
    }

    @Override
    public void trace(Object... args) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void debug(Object... args) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void info(Object... args) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void warning(Object... args) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void error(Object... args) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
