/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
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
