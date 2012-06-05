package vellum.enigma;

import java.security.cert.X509Certificate;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class EnigmaCommonResources {
    String host = "localhost";
    int port = 80;
    int sslPort = 443;    
    String serverPublicKeyStorePassword = "publicstorepassword";
    String serverPublicKeyStoreResource = "/ssldemo/resource/serverpublic";
    boolean useDefaultSocketFactory = true;
}

