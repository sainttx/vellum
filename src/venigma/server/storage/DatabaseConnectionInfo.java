/*
 * Apache Software License 2.0, Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2012, Evan Summers
 * 
 */
package venigma.server.storage;

/**
 *
 * @author evan
 */
public class DatabaseConnectionInfo {
    String driver;
    String name;
    String url;
    String username;

    public DatabaseConnectionInfo(String driver, String url, String username) {
        this.driver = driver;
        this.url = url;
        this.username = username;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
        
}
