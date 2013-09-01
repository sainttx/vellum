/*
       Source https://code.google.com/p/vellum by @evanxsummers
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
