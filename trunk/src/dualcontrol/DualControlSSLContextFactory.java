/*
       https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF

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

import vellum.util.VellumProperties;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author evan.summers
 */
public class DualControlSSLContextFactory {    
    private static final Logger logger = Logger.getLogger(DualControlSSLContextFactory.class);

    public static SSLContext createSSLContext(String keyStoreLocation, char[] keyStorePassword)
            throws Exception {    
        return DualControlSSLContextFactory.createSSLContext(keyStoreLocation, keyStorePassword,
                keyStorePassword, keyStoreLocation, keyStorePassword);
    }
        
    public static SSLContext createSSLContext(VellumProperties properties) throws Exception {
        String keyStoreLocation = properties.getString("dualcontrol.ssl.keyStore");
        if (keyStoreLocation == null) {
            throw new Exception("Missing -D property: dualcontrol.ssl.keyStore");
        }
        char[] keyStorePassword = properties.getPassword("dualcontrol.ssl.keyStorePassword", 
                null);
        if (keyStorePassword == null) {
            keyStorePassword = System.console().readPassword(
                    "Enter passphrase for dual control SSL connection: ");
        }
        char[] keyPassword = properties.getPassword("dualcontrol.ssl.keyPassword", 
                keyStorePassword);
        String trustStoreLocation =
                properties.getString("dualcontrol.ssl.trustStore", keyStoreLocation);
        char[] trustStorePassword =
                properties.getPassword("dualcontrol.ssl.trustStorePassword", keyStorePassword);
        SSLContext sslContext = createSSLContext(keyStoreLocation, keyStorePassword,
                keyPassword, trustStoreLocation, trustStorePassword);
        Arrays.fill(keyStorePassword, (char) 0);
        Arrays.fill(keyPassword, (char) 0);
        Arrays.fill(trustStorePassword, (char) 0);
        return sslContext;
    }
    
    public static void clearPassword(char[] password) {
        if (password != null) {
            Arrays.fill(password, (char) 0);
        }
    }

    public static SSLContext createSSLContext(String keyStoreLocation,
            char[] keyStorePassword, char[] keyPassword,
            String trustStoreLocation, char[] trustStorePassword) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(keyStoreLocation), keyStorePassword);
        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(new FileInputStream(trustStoreLocation), trustStorePassword);
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
