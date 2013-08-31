/*
 * Licensed to the Apache Software Foundation (ASF) by Evan Summers
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
