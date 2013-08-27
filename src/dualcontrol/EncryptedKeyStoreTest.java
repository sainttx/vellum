/*
 * Apache Software License 2.0, (c) Copyright 2013, Evan Summers
 * 
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
 * @author evans
 */
public class EncryptedKeyStoreTest {
    private final static Logger logger = Logger.getLogger(EncryptedKeyStoreTest.class);
    private final String keyAlg = "AES";
    private final int keySize = 128;
    private final String keyStoreType = "JCEKS";
    private String keyStoreLocation;
    private String keyAlias;
    private char[] keyPass;

    public static void main(String[] args) throws Exception {
        logger.debug("main " + Arrays.toString(args));
        if (args.length != 3) {
            System.err.println("usage: keystore alias keyPass"); 
        } else {
            new EncryptedKeyStoreTest(args[0], args[1], args[2].toCharArray()).start(5);
        }
    }    
    
    public EncryptedKeyStoreTest(String keyStoreLocation, String alias, char[] keyPass) {
        this.keyStoreLocation = keyStoreLocation;
        this.keyAlias = alias;
        this.keyPass = keyPass;
    }

    private void start(int count) throws Exception {
        long millis = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            test();
        }
        Log.infof(logger, "average %dms", Millis.elapsed(millis)/count);
    }
    
    private void test() throws Exception {
        String data = "4000555500001111";
        SecretKey dek = KeyGenerators.generateKey(keyAlg, keySize);
        VellumCipher cipher = AESCiphers.getCipher(dek);
        Encrypted encrypted = cipher.encrypt(data.getBytes());
        Log.info(logger, Bytes.toString(cipher.decrypt(encrypted)));
        long millis = System.currentTimeMillis();
        EncryptedKeyStores.storeKeyForce(dek, keyStoreLocation, 
                keyStoreType, keyAlias, keyPass);
        Log.infof(logger, "store %dms", Millis.elapsed(millis));
        millis = System.currentTimeMillis();
        dek = EncryptedKeyStores.loadKey(keyStoreLocation, 
                keyStoreType, keyAlias, keyPass);
        Log.infof(logger, "load %dms", Millis.elapsed(millis));
        Log.info(logger, keyAlias, dek.getAlgorithm());
        encrypted = cipher.encrypt(data.getBytes());
        Log.info(logger, Bytes.toString(cipher.decrypt(encrypted)));
    }
}
