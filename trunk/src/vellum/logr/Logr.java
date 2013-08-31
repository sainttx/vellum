/*
 * Apache Software License 2.0
 * Contributed (2013) by Evan Summers via https://code.google.com/p/vellum
 */

package vellum.logr;

/**
 *
 * @author evan.summers
 */
public interface Logr {

    public void trace(String message, Object ... args);
    
    public void verbose(String message, Object ... args);

    public void verboseArray(String message, Object ... args);

    public void info(String message, Object ... args);
    
    public void infoArray(String message, Object[] args);
    
    public void feature(String message, Object ... args);

    public void warn(String message, Object ... args);
    
    public void error(String message, Object ... args);

    public void warn(Throwable throwable);
    
    public void warn(Throwable throwable, String message, Object ... args);
    
    public void error(Throwable throwable, String message, Object ... args);
    
}
