/*
 * Source https://code.google.com/p/vellum by @evanxsummers

       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements. See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.  
 */
package dualcontrol;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Properties;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 *
 * @author evan.summers
 */
public class PropertiesSSLContextFactory {    
    
    public static SSLContext createSSLContext(String sslPrefix, Properties properties, 
            MockableConsole console) throws Exception {
        DualControlProperties props = new DualControlProperties(properties);
        sslPrefix = props.getString(sslPrefix, sslPrefix);
        String keyStoreLocation = props.getString(sslPrefix + ".keyStore");
        if (keyStoreLocation == null) {
            throw new Exception("Missing -D property: " + sslPrefix + ".keyStore");
        }
        char[] keyStorePassword = props.getPassword(sslPrefix + ".keyStorePassword", null);
        if (keyStorePassword == null) {
            keyStorePassword = console.readPassword("Enter passphrase for %s: ", sslPrefix);
        }
        char[] keyPassword = props.getPassword(sslPrefix + ".keyPassword", keyStorePassword);
        String trustStoreLocation = props.getString(sslPrefix + ".trustStore", keyStoreLocation);
        char[] trustStorePassword = props.getPassword(sslPrefix + ".trustStorePassword", 
                keyStorePassword);
        SSLContext sslContext = createSSLContext(keyStoreLocation, keyStorePassword,
                keyPassword, trustStoreLocation, trustStorePassword);
        Arrays.fill(keyStorePassword, (char) 0);
        Arrays.fill(keyPassword, (char) 0);
        Arrays.fill(trustStorePassword, (char) 0);
        return sslContext;
    }
    
    public static SSLContext createSSLContext(String keyStoreLocation,
            char[] keyStorePassword, char[] keyPassword,
            String trustStoreLocation, char[] trustStorePassword) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(keyStoreLocation), keyStorePassword);
        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(new FileInputStream(trustStoreLocation), trustStorePassword);
        return createSSLContext(keyStore, keyPassword, trustStore);
    }
    
    public static SSLContext createSSLContext(KeyStore keyStore, char[] keyPassword,
            KeyStore trustStore) throws Exception {
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, keyPassword);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        trustManagerFactory.init(trustStore);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(),
                trustManagerFactory.getTrustManagers(), new SecureRandom());
        return sslContext;
    }
}