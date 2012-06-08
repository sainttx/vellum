/*
 * Apache Software License 2.0
 * (c) Copyright 2012, Evan Summers
 */
package vellum.logr.jul;

import java.util.logging.Logger;
import vellum.logr.Logr;
import vellum.logr.LogrContext;
import vellum.util.Args;

/**
 *
 * @author evans
 */
public class JulAdapter implements Logr {
    Logger logger;
    
    public JulAdapter(LogrContext context) {
        logger = Logger.getLogger(context.getName());
    }

    @Override
    public void trace(Object... args) {
        logger.finer(Args.format(args));
    }

    @Override
    public void debug(Object... args) {
        logger.fine(Args.format(args));
    }

    @Override
    public void info(Object... args) {
        logger.info(Args.format(args));
    }

    @Override
    public void warning(Object... args) {
        logger.warning(Args.format(args));
    }

    @Override
    public void error(Object... args) {
        logger.severe(Args.format(args));
    }
    
}
