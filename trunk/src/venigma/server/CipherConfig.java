/*
 * Copyright Evan Summers
 * 
 */
package venigma.server;

/**
 *
 * @author evan
 */
public class CipherConfig {
    public String serverIp = "localhost";
    public String privateAlias = "cipher.private";
    public String trustAlias = "provider.cert";
    public int sslPort = 7443;
    public int backlog = 4;
    public String keyStore; 
    public String trustKeyStore; 
    public String secretKeyStore; 
    public String secretAlias = "cipher.secret";
}
