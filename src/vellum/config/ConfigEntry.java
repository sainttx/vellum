/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
 * 
 */
package vellum.config;

import vellum.type.ComparableTuple;

/**
 *
 * @author evan.summers
 */
public class ConfigEntry {
    final String type;
    final String name;
    final ComparableTuple key;
    final PropertiesStringMap properties = new PropertiesStringMap();

    public ConfigEntry(String type, String name) {
        this.type = type;
        this.name = name;
        this.key = ComparableTuple.newInstance(type, name);
    }

    public ComparableTuple getKey() {
        return key;
    }
    
    public String getType() {
        return type;
    }
    
    public String getName() {
        return name;
    }

    public PropertiesStringMap getProperties() {
        return properties;
    }
    
    
}
