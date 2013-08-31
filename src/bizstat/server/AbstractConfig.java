/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
 * 
 */
package bizstat.server;

import vellum.config.PropertiesStringMap;

/**
 *
 * @author evan.summers
 */
public class AbstractConfig {
    protected PropertiesStringMap properties;

    public AbstractConfig(PropertiesStringMap properties) {
        this.properties = properties;
    }
        
}
