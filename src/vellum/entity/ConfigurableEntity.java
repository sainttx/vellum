/*
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
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
