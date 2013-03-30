/*
 * Copyright Evan Summers
 * 
 */
package crocserver.app;

import vellum.util.SystemProperties;

/**
 *
 * @author evan
 */
public class CrocConfig {

    String confFileName = SystemProperties.getString("croc.conf", "conf/croc.conf");;
    String serverKeyAlias = SystemProperties.getString("serverKeyAlias", "crocserver");
    String keyStorePath = SystemProperties.getString("javax.net.ssl.keyStore", "security/croc.jks");
    char[] keyStorePassword = SystemProperties.getString("javax.net.ssl.keyStorePassword", "").toCharArray();
    char[] keyPassword = SystemProperties.getString("javax.net.ssl.keyStorePassword", "").toCharArray();
    String trustStorePath = SystemProperties.getString("javax.net.ssl.trustStore", "security/croc.jks");
    char[] trustStorePassword = SystemProperties.getString("javax.net.ssl.trustStorePassword", "").toCharArray();


    
}
