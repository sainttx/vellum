/*
 * Copyright Evan Summers
 * 
 */
package crocserver.httpserver;

import vellum.config.PropertiesMap;

/**
 *
 * @author evan
 */
public class HttpServerConfig {
    boolean enabled;
    int port;
    
    public HttpServerConfig(PropertiesMap props) {
        this(props.getInt("port"),
                props.getBoolean("enabled", true));
    }
    
    public HttpServerConfig(int port, boolean enabled) {
        this.port = port;
        this.enabled = enabled;
    }

    public int getPort() {
        return port;
    }

    public boolean isEnabled() {
        return enabled;
    }                
}
