/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package venigma.provider;

/**
 *
 * @author evan
 */
public class ClientConfig {
    public String serverIp = "localhost";
    public String keyAlias = "client.key";
    public String cn = "client";
    public String trustAlias = "cipher.cert";
    public int sslPort = 7443;
    public String keyStore; 
    public String trustStore; 
}
