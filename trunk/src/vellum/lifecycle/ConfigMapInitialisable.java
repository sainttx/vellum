/*
 */
package vellum.lifecycle;

import vellum.config.ConfigMap;

/**
 *
 * @author evan.summers
 */
public interface ConfigMapInitialisable {
    public void init(ConfigMap configMap) throws Exception;
    
}
