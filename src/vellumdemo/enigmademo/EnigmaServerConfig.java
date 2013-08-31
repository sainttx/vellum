/*
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
 * 
 */
package vellumdemo.enigmademo;

/**
 *
 * @author evan.summers
 */
public class EnigmaServerConfig extends EnigmaConfig {
    String serverKeyStorePassword = "storepassword";
    String serverKeyPassword = "storepassword";
    String serverKeyStoreFileName = System.getProperty("javax.net.ssl.keyStore");
}
