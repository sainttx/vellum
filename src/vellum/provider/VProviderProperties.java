/*
 * Copyright Evan Summers
 * 
 */
package vellum.provider;

/**
 *
 * @author evan
 */
public class VProviderProperties {
    String serverIp = "localhost";
    String keyAlias = "provider.key";
    String trustAlias = "server.cert";
    int sslPort = 7443;
    String keyStore; 
    String trustStore; 
}
