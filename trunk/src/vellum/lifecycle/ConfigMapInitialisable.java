/*
 */
package vellum.lifecycle;

import vellum.config.ConfigMap;

/**
 *
 * @author evans
 */
public interface ConfigMapInitialisable {
    public void init(ConfigMap configMap) throws Exception;
    
}
