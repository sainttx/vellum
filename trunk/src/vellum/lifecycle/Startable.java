/*
 * Contributed (2013) by Evan Summers via https://code.google.com/p/vellum
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
