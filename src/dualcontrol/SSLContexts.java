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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import org.apache.log4j.Logger;
import sun.security.validator.Validator;

/**
 *
 * @author evan.summers
 */
public class SSLContexts {

    static Logger logger = Logger.getLogger(SSLContexts.class);

    public static SSLContext create(String sslPrefix, Properties properties,
            MockableConsole console) throws Exception {
        ExtendedProperties props = new ExtendedProperties(properties);
        sslPrefix = props.getString(sslPrefix, sslPrefix);
        String keyStoreLocation = props.getString(sslPrefix + ".keyStore");
        if (keyStoreLocation == null) {
            throw new Exception("Missing -D property: " + sslPrefix + ".keyStore");
        }
        char[] pass = props.getPassword(sslPrefix + ".pass", null);
        if (pass == null) {
            pass = console.readPassword("Enter passphrase for %s: ", sslPrefix);
        }
        String trustStoreLocation = props.getString(sslPrefix + ".trustStore",
                keyStoreLocation);
        SSLContext sslContext = create(keyStoreLocation, pass, trustStoreLocation);
        String crlFile = props.getString(sslPrefix + ".crlFile", null);
        if (crlFile != null) {
            sslContext = create(keyStoreLocation, pass, trustStoreLocation,
                    readRevocationList(crlFile));
        }
        Arrays.fill(pass, (char) 0);
        return sslContext;
    }

    public static SSLContext create(String keyStoreLocation, char[] pass,
            String trustStoreLocation) throws Exception {
        return create(keyStoreLocation, pass, pass, trustStoreLocation, pass);
    }

    public static SSLContext create(String keyStoreLocation,
            char[] keyStorePassword, char[] keyPassword,
            String trustStoreLocation, char[] trustStorePassword) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(keyStoreLocation), keyStorePassword);
        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(new FileInputStream(trustStoreLocation), trustStorePassword);
        return create(keyStore, keyPassword, trustStore);
    }

    public static SSLContext create(KeyStore keyStore, char[] keyPassword,
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

    public static SSLContext create(String keyStoreLocation, char[] pass,
            String trustStoreLocation,
            Collection<BigInteger> revocationList) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(keyStoreLocation), pass);
        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(new FileInputStream(trustStoreLocation), pass);
        return create(keyStore, pass, trustStore, revocationList);
    }

    public static SSLContext create(KeyStore keyStore, char[] keyPassword,
            KeyStore trustStore, Collection<BigInteger> recovationList) throws Exception {
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, keyPassword);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        Validator validator = Validator.getInstance(Validator.TYPE_SIMPLE, 
                Validator.VAR_GENERIC, trustStore);
        TrustManager revocableTrustManager = new RevocableClientTrustManager(
                validator, getX509TrustManager(trustStore), 
                getSoleCertificate(trustStore), 
                recovationList);
        sslContext.init(keyManagerFactory.getKeyManagers(), 
                new TrustManager[] {revocableTrustManager}, 
                new SecureRandom());
        return sslContext;
    }

    public static X509Certificate getSoleCertificate(KeyStore trustStore)
            throws KeyStoreException {
        if (Collections.list(trustStore.aliases()).size() > 1) {
            throw new KeyStoreException("Multiple keys in keystore");
        }
        String alias = trustStore.aliases().nextElement();
        return (X509Certificate) trustStore.getCertificate(alias);
    }

    private static Collection<BigInteger> readRevocationList(String crlFile)
            throws FileNotFoundException, IOException {
        List<BigInteger> revocationList = new ArrayList();
        BufferedReader reader = new BufferedReader(new FileReader(crlFile));
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            revocationList.add(new BigInteger(line.trim()));
        }
        return revocationList;
    }

    private static X509TrustManager getX509TrustManager(KeyStore trustStore)
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