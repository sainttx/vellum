/*
 * Apache Software License 2.0
 * (c) Copyright 2012, Evan Summers
 */
package vellum.logr;

import java.util.logging.Logger;
import vellum.logr.Logr;
import vellum.logr.LogrContext;

/**
 *
 * @author evans
 */
public class NullLogr implements Logr {
    
    public NullLogr(LogrContext context) {
    }

    @Override
    public void trace(Object... args) {
    }

    @Override
    public void debug(Object... args) {
    }

    @Override
    public void info(Object... args) {
    }

    @Override
    public void warning(Object... args) {
    }

    @Override
    public void error(Object... args) {
    }
    
}
