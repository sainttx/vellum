/*
 * Copyright Evan Summers
 * 
 */
package vellum.provider;

/**
 *
 * @author evan
 */
public class VCipherProperties {
    String serverIp = "localhost";
    String privateAlias = "cipher.private";
    String trustAlias = "provider.cert";
    String secretAlias = "cipher.secret";
    int sslPort = 7443;
    int backlog = 4;
    String keyStore; 
    String trustStore; 
    String cipherKeyStore; 
}
