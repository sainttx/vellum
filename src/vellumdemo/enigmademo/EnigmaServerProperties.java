/*
 * Copyright Evan Summers
 * 
 */
package vellumdemo.enigmademo;

/**
 *
 * @author evan
 */
public class EnigmaServerProperties extends EnigmaCommonProperties {
    String serverKeyStorePassword = "storepassword";
    String serverKeyPassword = "storepassword";
    String serverKeyStoreFileName = System.getProperty("javax.net.ssl.keyStore");
}
