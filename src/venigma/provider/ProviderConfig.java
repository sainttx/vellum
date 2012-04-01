/*
 * Copyright Evan Summers
 * 
 */
package venigma.provider;

/**
 *
 * @author evan
 */
public class ProviderConfig {
    public String serverIp = "localhost";
    public String keyAlias = "provider.key";
    public String trustAlias = "cipher.cert";
    public int sslPort = 7443;
    public String keyStore; 
    public String trustStore; 
}
