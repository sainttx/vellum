/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.storage;

/**
 *
 * @author evan
 */
public class DataSourceInfo {
    String driver;
    String name;
    String url;
    String user;
    String password;
    boolean enabled;
    Integer poolSize; 
    
    public DataSourceInfo() {
    }
    
    public DataSourceInfo(String driver, String url, String user, String password, boolean enabled, Integer poolSize) {
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.password = password;
        this.enabled = enabled;
        this.poolSize = poolSize;
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

    public Integer getPoolSize() {
        return poolSize;
    }               
}
