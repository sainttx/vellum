/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.storage;

/**
 *
 * @author evan
 */
public class DataSourceConfig {
    String driver;
    String name;
    String url;
    String user;
    String password;
    boolean enabled;
    
    public DataSourceConfig() {
    }
    
    public DataSourceConfig(String driver, String url, String user, String password, boolean enabled) {
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.password = password;
        this.enabled = enabled;
    }
    
    public String getDriver() {
        return driver;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }
    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }
       
}
