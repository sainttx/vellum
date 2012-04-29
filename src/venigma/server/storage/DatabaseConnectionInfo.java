/*
 * Copyright Evan Summers
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
    String password;
    char[] storePassword;
    char[] userPassword;
    
    public String getDriver() {
        return driver;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setStorePassword(char[] storePassword) {
        this.storePassword = storePassword;
    }

    public char[] getStorePassword() {
        return storePassword;
    }

    public void setUserPassword(char[] userPassword) {
        this.userPassword = userPassword;
    }

    public char[] getUserPassword() {
        return userPassword;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
        
}
