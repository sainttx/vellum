/*
 * Copyright Evan Summers
 * 
 */
package venigmon.httpserver;

/**
 *
 * @author evan
 */
public class HttpServerConfig {
    boolean enabled;
    int port;

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
