/*
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
 * 
 */
package vellum.httpserver;

import vellum.config.PropertiesStringMap;

/**
 *
 * @author evan.summers
 */
public class HttpServerConfig {
    int port;
    boolean enabled;
    boolean clientAuth;
            
    public HttpServerConfig(PropertiesStringMap props) {
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
