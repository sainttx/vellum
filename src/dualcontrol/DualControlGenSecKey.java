/*
       PROJECT INFO

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
    private SSLContext sslContext;

    public static void main(String[] args) throws Exception {
        logger.info("main " + Arrays.toString(args));
        try {
            new DualControlGenSecKey().call(VellumProperties.systemProperties);
        } catch (DualControlException e) {
            logger.error(e.getMessage());
        }
    }

    public KeyStore call(VellumProperties properties) throws Exception {
        submissionCount = properties.getInt("dualcontrol.submissions", 3);
        keyAlias = properties.getString("alias");
        keyStoreLocation = properties.getString("keystore");
        keyStoreType = properties.getString("storetype");
        keyAlg = properties.getString("keyalg");
        keySize = properties.getInt("keysize");
        sslContext = DualControlSSLContextFactory.createSSLContext(properties);
        return call();
    }
    
    private KeyStore call() throws Exception {
        String purpose = "new key " + keyAlias;
        Map<String, char[]> dualMap = new DualControlReader().readDualMap(
                purpose, submissionCount, sslContext);
        char[] keyStorePassword = DualControlKeyStoreTools.getKeyStorePassword();
        KeyGenerator keyGenerator = KeyGenerator.getInstance(keyAlg);
        keyGenerator.init(keySize);
        SecretKey secretKey = keyGenerator.generateKey();
        KeyStore keyStore = DualControlKeyStores.loadLocalKeyStore(keyStoreLocation, 
                keyStoreType, keyStorePassword);
        KeyStore.Entry entry = new KeyStore.SecretKeyEntry(secretKey);
        for (String dualAlias : dualMap.keySet()) {
            char[] dualPassword = dualMap.get(dualAlias);
            String alias = keyAlias + "-" + dualAlias;
            KeyStore.ProtectionParameter prot = 
                    new KeyStore.PasswordProtection(dualPassword);
            keyStore.setEntry(alias, entry, prot);
            logger.info("alias " + alias);
        }
        keyStore.store(new FileOutputStream(keyStoreLocation), keyStorePassword);
        return keyStore;
    }
}
