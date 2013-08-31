/*
 * Contributed (2013) by Evan Summers via https://code.google.com/p/vellum
 * 
 */
package venigma.provider;

/**
 *
 * @author evan.summers
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
