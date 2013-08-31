/*
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
 * 
 */
package vellum.entity;

import vellum.config.PropertiesStringMap;

/**
 *
 * @author evan.summers
 */
public interface ConfigurableEntity<C> extends IdEntity, Named {
    public void setName(String name);
    public void config(C context, PropertiesStringMap properties);
}
