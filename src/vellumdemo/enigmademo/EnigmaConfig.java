/*
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
 * 
 */
package vellumdemo.enigmademo;

/**
 *
 * @author evan.summers
 */
public class EnigmaConfig {
    String host = "localhost";
    int port = 80;
    int sslPort = 443;    
    String serverPublicKeyStorePassword = "publicstorepassword";
    String serverPublicKeyStoreResource = "/ssldemo/resource/serverpublic";
}
