/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
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
