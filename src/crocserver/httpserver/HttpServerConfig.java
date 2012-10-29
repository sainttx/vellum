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
    int port;
    boolean enabled;
    boolean clientAuth;
            
    public HttpServerConfig(PropertiesMap props) {
        this(props.getInt("port"),
                props.getBoolean("clientAuth", false),
                props.getBoolean("enabled", true));
    }
    
    public HttpServerConfig(int port, boolean clientAuth, boolean enabled) {
        this.port = port;
        this.clientAuth = clientAuth;
        this.enabled = enabled;
    }

    public int getPort() {
        return port;
    }

    public boolean isClientAuth() {
        return clientAuth;
    }
    
    public boolean isEnabled() {
        return enabled;
    }          
}
