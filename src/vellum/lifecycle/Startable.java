/*
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
 * 
 */
package vellum.lifecycle;

/**
 *
 * @author evan.summers
 */
public interface Startable {
    public void start() throws Exception;
    public boolean stop();

}
