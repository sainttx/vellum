/*
 * Copyright Evan Summers
 * 
 */
package vellum.lifecycle;

/**
 *
 * @author evan
 */
public interface Startable {
    public void start() throws Exception;
    public boolean stop();
    
}
