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

import java.util.Arrays;
import javax.crypto.SecretKey;
import org.apache.log4j.Logger;
import vellum.crypto.Encrypted;
import vellum.crypto.VellumCipher;
import vellum.datatype.Millis;
import vellum.util.Bytes;

/**
 *
 * @author evan.summers
 */
public class EncryptedKeyStoreTest {
    private final static Logger logger = Logger.getLogger(EncryptedKeyStoreTest.class);
    private final String keyAlg = "AES";
    private final int keySize = 256;
    private final String keyStoreType = "JCEKS";
    private String keyStoreLocation;
    private String keyAlias;
    private char[] keyPass;
    private int iterationCount; 
    
    public static void main(String[] args) throws Exception {
        logger.debug("main " + Arrays.toString(args));
        if (args.length != 5) {
            System.err.println("usage: keystore alias keyPass iterationCount repeat"); 
        } else {
            new EncryptedKeyStoreTest().start(args[0], args[1], args[2].toCharArray(), 
                    Integer.parseInt(args[3]), Integer.parseInt(args[4]));
        }
    }    
    
    public void start(String keyStoreLocation, String alias, char[] keyPass,
            int iterationCount, int repeat) throws Exception {
        this.keyStoreLocation = keyStoreLocation;
        this.keyAlias = alias;
        this.keyPass = keyPass;
        this.iterationCount = iterationCount;
        long millis = System.currentTimeMillis();
        for (int i = 0; i < repeat; i++) {
            test();
        }
        Log.infof(logger, "average %dms", Millis.elapsed(millis)/repeat);
    }
    
    private void test() throws Exception {
        String data = "4000555500001111";
        long millis = System.currentTimeMillis();
        SecretKey dek = KeyGenerators.generateKey(keyAlg, keySize);
        Log.infof(logger, "generate %dms", Millis.elapsed(millis));
        VellumCipher cipher = AESCiphers.getCipher(dek);
        millis = System.currentTimeMillis();
        Encrypted encrypted = cipher.encrypt(data.getBytes());
        Log.infof(logger, "encrypt %dms", Millis.elapsed(millis));
        millis = System.currentTimeMillis();
        Log.info(logger, Bytes.toString(cipher.decrypt(encrypted)));
        Log.infof(logger, "decrypt %dms", Millis.elapsed(millis));
        millis = System.currentTimeMillis();
        RecryptedKeyStores.storeKeyForce(iterationCount, dek, keyStoreLocation, 
                keyStoreType, keyAlias, keyPass);
        Log.infof(logger, "store %dms", Millis.elapsed(millis));
        millis = System.currentTimeMillis();
        dek = RecryptedKeyStores.loadKey(keyStoreLocation, 
                keyStoreType, keyAlias, keyPass);
        Log.infof(logger, "load %dms", Millis.elapsed(millis));
        Log.info(logger, keyAlias, dek.getAlgorithm());
        millis = System.currentTimeMillis();
        encrypted = cipher.encrypt(data.getBytes());
        Log.infof(logger, "encrypt %dms", Millis.elapsed(millis));
        Log.info(logger, Bytes.toString(cipher.decrypt(encrypted)));
    }
}
