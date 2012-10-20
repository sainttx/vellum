/*
 * Apache Software License 2.0
 * (c) Copyright 2012, Evan Summers
 */

package vellum.logr.other;

/**
 *
 * @author evanx
 */
public interface Logr {

    public void trace(String message, Object ... args);
    
    public void debug(String message, Object ... args);

    public void info(String message, Object ... args);

    public void warn(String message, Object ... args);
    
    public void error(String message, Object ... args);

    public void warning(Throwable throwable, String message, Object ... args);
    
    public void error(Throwable throwable, String message, Object ... args);
    
}
