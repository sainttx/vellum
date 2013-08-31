/*
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
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
