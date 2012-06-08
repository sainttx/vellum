/*
 * Apache Software License 2.0
 * (c) Copyright 2012, Evan Summers
 */

package vellum.logger;

/**
 *
 * @author evanx
 */
public interface Logr {

    public void trace(Object ... args);
    
    public void debug(Object ... args);

    public void info(Object ... args);

    public void warn(Object ... args);
    
    public void error(Object ... args);

}
