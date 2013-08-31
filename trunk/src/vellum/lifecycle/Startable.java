/*
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
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
