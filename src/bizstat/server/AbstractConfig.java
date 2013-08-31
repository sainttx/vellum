/*
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
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
