/*
 Source https://code.google.com/p/vellum by @evanxsummers

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
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.net.ssl.SSLContext;
import org.apache.log4j.Logger;

/**
 *
 * @author evan.summers
 */
public class DualControlGenSecKey {

    final static Logger logger = Logger.getLogger(DualControlGenSecKey.class);
    private int submissionCount;
    private String keyAlias;
    private String keyStoreLocation;
    private String keyStoreType;
    private String keyAlg;
    private int keySize;
    private char[] keyStorePassword;
    private Map<String, char[]> dualPasswordMap;
    private SSLContext sslContext;

    public static void main(String[] args) throws Exception {
        logger.info("main " + Arrays.toString(args));
        try {
            new DualControlGenSecKey().call(System.getProperties());
        } catch (DualControlException e) {
            logger.error(e.getMessage());
        }
    }

    public void call(Properties properties) throws Exception {
        sslContext = DualControlSSLContextFactory.createSSLContext(properties);
        String purpose = "new key " + keyAlias;
        KeyStore keyStore = generate(properties, new DualControlReader().readDualMap(
                purpose, submissionCount, sslContext));
        keyStore.store(new FileOutputStream(keyStoreLocation), keyStorePassword);
        clear();

    }

    public KeyStore generate(Properties properties,
            Map<String, char[]> dualPasswordMap) throws Exception {
        configure(new VellumProperties(properties));
        KeyGenerator keyGenerator = KeyGenerator.getInstance(keyAlg);
        keyGenerator.init(keySize);
        SecretKey secretKey = keyGenerator.generateKey();
        KeyStore keyStore = DualControlKeyStores.loadLocalKeyStore(keyStoreLocation,
                keyStoreType, keyStorePassword);
        setEntry(keyStore, secretKey, keyAlias, dualPasswordMap);
        return keyStore;
    }

    private static void setEntry(KeyStore keyStore, SecretKey secretKey,
            String keyAlias, Map<String, char[]> dualPasswordMap) throws Exception {
        KeyStore.Entry entry = new KeyStore.SecretKeyEntry(secretKey);
        for (String dualAlias : dualPasswordMap.keySet()) {
            char[] dualPassword = dualPasswordMap.get(dualAlias);
            String alias = keyAlias + "-" + dualAlias;
            KeyStore.ProtectionParameter prot =
                    new KeyStore.PasswordProtection(dualPassword);
            keyStore.setEntry(alias, entry, prot);
            logger.info("alias " + alias);
        }
    }

    private void configure(VellumProperties properties) throws Exception {
        submissionCount = properties.getInt("dualcontrol.submissions", 3);
        keyAlias = properties.getString("alias");
        keyStoreLocation = properties.getString("keystore");
        keyStoreType = properties.getString("storetype");
        keyAlg = properties.getString("keyalg");
        keySize = properties.getInt("keysize");
        keyStorePassword = properties.getPassword("storepass", null);
        if (keyStorePassword == null) {
            keyStorePassword = System.console().readPassword("storepass: ");
        }
    }

    private void clear() {
        Arrays.fill(keyStorePassword, (char) 0);
        for (char[] password : dualPasswordMap.values()) {
            Arrays.fill(password, (char) 0);
        }
    }
}
