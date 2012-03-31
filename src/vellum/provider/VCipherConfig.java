/*
 * Copyright Evan Summers
 * 
 */
package vellum.provider;

/**
 *
 * @author evan
 */
public class VCipherConfig {
    String serverIp = "localhost";
    String privateAlias = "cipher.private";
    String trustAlias = "provider.cert";
    int sslPort = 7443;
    int backlog = 4;
    String keyStore; 
    String trustKeyStore; 
    String secretKeyStore; 
    String secretAlias = "cipher.secret";
}
