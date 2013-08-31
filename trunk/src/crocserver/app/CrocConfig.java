/*
 *    https://code.google.com/p/vellum - Contributed by Evan Summers
 * 
 * -Djavax.net.ssl.keyStore=security/croc.jks
 * -Djavax.net.ssl.keyStorePassword=crocserver
 * -Djavax.net.ssl.keyPassword=crocserver
 * -Djavax.net.ssl.trustStore=security/croc.jks
 * -Djavax.net.ssl.trustStorePassword=crocserver
 */
package crocserver.app;

import vellum.util.SystemProperties;

/**
 * 
 * @author evan.summers
 */
public class CrocConfig {
    boolean testing = SystemProperties.getBoolean(
            "croc.testing");    
    String confFileName = SystemProperties.getString(
            "croc.conf", "conf/croc.conf");
    String serverKeyAlias = SystemProperties.getString(
            "serverKeyAlias", "crocserver");
    String keyStoreLocation = SystemProperties.getString(
            "javax.net.ssl.keyStore", "security/croc.jks");
    char[] keyStorePassword = SystemProperties.getString(
            "javax.net.ssl.keyStorePassword", "crocserver").toCharArray();
    char[] keyPassword = SystemProperties.getString(
            "javax.net.ssl.keyPassword", "crocserver").toCharArray();
    String trustStoreLocation = SystemProperties.getString(
            "javax.net.ssl.trustStore", "security/croc.jks");
    char[] trustStorePassword = SystemProperties.getString(
            "javax.net.ssl.trustStorePassword", "crocserver").toCharArray();

    public boolean isTesting() {
        return testing;
    }        
}
