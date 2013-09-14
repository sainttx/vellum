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

import java.io.File;
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
    private char[] keyStorePassword;
    private VellumProperties properties;
    private MockableConsole console;
    private SSLContext sslContext;

    public DualControlGenSecKey(VellumProperties properties, MockableConsole console) {
        this.properties = properties;
        this.console = console;
        submissionCount = properties.getInt("dualcontrol.submissions", 3);
        keyStorePassword = properties.getPassword("storepass", null);
        keyAlias = properties.getString("alias");
    }

    public void init(SSLContext sslContext) {
        this.sslContext = sslContext;
    }

    public void init() throws Exception {
        sslContext = DualControlSSLContextFactory.createSSLContext(properties, console);
    }

    public static void main(String[] args) throws Exception {
        logger.info("main " + Arrays.toString(args));
        DualControlGenSecKey instance = new DualControlGenSecKey(
                new VellumProperties(System.getProperties()), 
                new ConsoleAdapter(System.console()));
        try {
            instance.init();
            instance.call();
        } catch (DualControlException e) {
            instance.console.println(e.getMessage());
        } finally {
            instance.clear();
        }
    }
        
    public void call() throws Exception {
        keyStoreLocation = properties.getString("keystore");
        if (new File(keyStoreLocation).exists()) {
            throw new Exception("Keystore file already exists: " + keyStoreLocation);
        }
        if (keyStorePassword == null) {
            keyStorePassword = console.readPassword(
                    "Enter passphrase for keystore (%s): ", keyStoreLocation);
            if (keyStorePassword == null) {
                throw new Exception("No keystore passphrase from console");
            }
        }
        KeyStore keyStore = createKeyStore();
        keyStore.store(new FileOutputStream(keyStoreLocation), keyStorePassword);
    }

    public KeyStore createKeyStore() throws Exception {
        String purpose = "new key " + keyAlias;
        return buildKeyStore(
                new DualControlManager(properties, submissionCount, purpose).
                readDualMap(sslContext));
    }

    public KeyStore buildKeyStore(Map<String, char[]> dualPasswordMap) throws Exception {
        keyAlias = properties.getString("alias");
        keyStoreType = properties.getString("storetype");
        keyAlg = properties.getString("keyalg");
        keySize = properties.getInt("keysize");
        KeyGenerator keyGenerator = KeyGenerator.getInstance(keyAlg);
        keyGenerator.init(keySize);
        SecretKey secretKey = keyGenerator.generateKey();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        setEntry(keyStore, secretKey, keyAlias, dualPasswordMap);
        return keyStore;
    }

    private static void setEntry(KeyStore keyStore, SecretKey secretKey,
            String keyAlias, Map<String, char[]> dualPasswordMap) throws Exception {
        KeyStore.Entry entry = new KeyStore.SecretKeyEntry(secretKey);
        for (String dualAlias : dualPasswordMap.keySet()) {
            char[] dualPassword = dualPasswordMap.get(dualAlias);
            String alias = keyAlias + "-" + dualAlias;
            logger.info("alias " + alias);
            KeyStore.PasswordProtection prot =
                    new KeyStore.PasswordProtection(dualPassword);
            keyStore.setEntry(alias, entry, prot);
            prot.destroy();
        }
    }

    private void clear() {
        Arrays.fill(keyStorePassword, (char) 0);
    }
}
