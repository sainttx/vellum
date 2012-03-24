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
    int sslPort = 7443;
    int backlog = 4;
    String keyStore; 
    String trustStore; 
    char[] keyStorePassword;
    char[] keyPassword;
}
