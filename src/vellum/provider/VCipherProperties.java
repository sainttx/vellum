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
    String keyAlias = "cipher.key";
    String trustAlias = "provider.cert";
    int sslPort = 7443;
    int backlog = 4;
    String keyStore; 
    String trustStore; 
}
