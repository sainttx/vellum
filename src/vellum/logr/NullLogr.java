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
    public void trace(String message, Object... args) {
    }

    @Override
    public void debug(String message, Object... args) {
    }

    @Override
    public void info(String message, Object... args) {
    }

    @Override
    public void warn(String message, Object... args) {
    }

    @Override
    public void error(String message, Object... args) {
    }

    @Override
    public void warning(Throwable throwable, String message, Object... args) {
    }

    @Override
    public void error(Throwable throwable, String message, Object... args) {
    }

    
}
