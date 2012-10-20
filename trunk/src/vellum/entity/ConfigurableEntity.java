/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.entity;

import bizstat.server.BizstatServer;
import vellum.config.PropertiesMap;

/**
 *
 * @author evan
 */
public interface ConfigurableEntity extends IdEntity, Named {
    public void setName(String name);
    public void set(BizstatServer server, PropertiesMap properties);
}
