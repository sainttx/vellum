/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
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
