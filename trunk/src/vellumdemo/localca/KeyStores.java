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
package vellumdemo.localca;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 *
 * @author evan.summers
 */
public class KeyStores {

    public static X509Certificate getPrivateKeyCertificate(KeyStore keyStore, 
            String keyAlias) throws KeyStoreException {
        if (!keyStore.entryInstanceOf(keyAlias, KeyStore.PrivateKeyEntry.class)) {
            throw new KeyStoreException("Not private key entry: " + keyAlias);
        }
        return (X509Certificate) keyStore.getCertificate(keyAlias);
    }

    public static X509Certificate getPrivateKeyCertificate(KeyStore keyStore) 
            throws KeyStoreException {
        if (countKeys(keyStore) == 1) {
            for (String alias : Collections.list(keyStore.aliases())) {
                if (keyStore.entryInstanceOf(alias, KeyStore.PrivateKeyEntry.class)) {
                    return (X509Certificate) keyStore.getCertificate(alias);
                }
            }
        }
        throw new KeyStoreException("No sole private key found in keystore");
    }
            
    public static int countKeys(KeyStore keyStore) throws KeyStoreException {
        int count = 0;
        for (String alias : Collections.list(keyStore.aliases())) {
            if (keyStore.entryInstanceOf(alias, KeyStore.PrivateKeyEntry.class)) {
                count++;
            }
        }
        return count;
    }

    public static X509TrustManager getX509TrustManager(KeyStore trustStore)
            throws GeneralSecurityException {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        trustManagerFactory.init(trustStore);
        if (trustManagerFactory.getTrustManagers().length != 1) {
            throw new GeneralSecurityException("Multiple default trust managers");
        }
        if (trustManagerFactory.getTrustManagers()[0] instanceof X509TrustManager) {
            return (X509TrustManager) trustManagerFactory.getTrustManagers()[0];
        }
        throw new GeneralSecurityException("Default X509TrustManager not found");
    }
}