/*
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
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
