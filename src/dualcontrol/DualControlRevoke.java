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
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author evan.summers
 */
public class DualControlRevoke {

    final static Logger logger = Logger.getLogger(DualControlRevoke.class);
    private VellumProperties properties = VellumProperties.systemProperties;
    private String username = properties.getString("dualcontrol.username");
    private String keyAlias = properties.getString("alias");
    private String keyStoreLocation = properties.getString("keystore");
    private String keyStoreType = properties.getString("storetype");
    private char[] keyStorePassword;
    private KeyStore keyStore;
    List<String> aliasList;

    public static void main(String[] args) throws Exception {
        logger.info("main " + Arrays.toString(args));
        try {
            new DualControlRevoke().start();
        } catch (DualControlException e) {
            logger.error(e.getMessage());
        }
    }

    void start() throws Exception {
        keyStorePassword = DualControlKeyStoreTools.getKeyStorePassword();
        keyStore = DualControlKeyStores.loadLocalKeyStore(keyStoreLocation, 
                keyStoreType, keyStorePassword);
        aliasList = Collections.list(keyStore.aliases());
        for (String alias : aliasList) {
            logger.debug("alias " + alias);
            if (matches(alias)) {
                logger.info("delete " + alias);
                keyStore.deleteEntry(alias);
            }
        }
        keyStore.store(new FileOutputStream(keyStoreLocation), keyStorePassword);
    }

    boolean matches(String alias) {
        if (alias.startsWith(keyAlias + "-" + username + "-")) {
            return true;
        }
        if (alias.startsWith(keyAlias + "-") && alias.endsWith("-" + username)) {
            return true;
        }
        return false;
    }
}
