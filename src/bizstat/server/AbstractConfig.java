/*
 *    https://code.google.com/p/vellum - Contributed by Evan Summers
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
