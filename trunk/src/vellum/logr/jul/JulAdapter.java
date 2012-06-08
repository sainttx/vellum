/*
 * Apache Software License 2.0
 * (c) Copyright 2012, Evan Summers
 */
package vellum.logr.jul;

import java.util.logging.Logger;
import vellum.logr.Logr;
import vellum.logr.LogrContext;

/**
 *
 * @author evans
 */
public class JulAdapter implements Logr {
    
    public JulAdapter(LogrContext context) {
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
    public void warn(Object... args) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void error(Object... args) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
