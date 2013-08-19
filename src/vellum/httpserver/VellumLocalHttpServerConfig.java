/*
 * Copyright Evan Summers
 * 
 */
package vellum.httpserver;

import vellum.util.SystemProperties;

/**
 *
 * @author evan
 */
public class VellumLocalHttpServerConfig {
    int port = 8080;
    String[] allowHosts = {"127.0.0.1"};
    String rootDir = SystemProperties.getString("vellum.web.root");
    String rootFile = "home.html";
    
    public int getPort() {
        return port;
    }

    public String[] getAllowHosts() {
        return allowHosts;
    }   

    public String getRootDir() {
        return rootDir;
    }

    public String getRootFile() {
        return rootFile;
    }    
}