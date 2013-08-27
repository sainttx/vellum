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
    
    private void start(String keyStoreLocation, String alias, char[] keyPass,
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
        EncryptedKeyStores.storeKeyForce(iterationCount, dek, keyStoreLocation, 
                keyStoreType, keyAlias, keyPass);
        Log.infof(logger, "store %dms", Millis.elapsed(millis));
        millis = System.currentTimeMillis();
        dek = EncryptedKeyStores.loadKey(keyStoreLocation, 
                keyStoreType, keyAlias, keyPass);
        Log.infof(logger, "load %dms", Millis.elapsed(millis));
        Log.info(logger, keyAlias, dek.getAlgorithm());
        millis = System.currentTimeMillis();
        encrypted = cipher.encrypt(data.getBytes());
        Log.infof(logger, "encrypt %dms", Millis.elapsed(millis));
        Log.info(logger, Bytes.toString(cipher.decrypt(encrypted)));
    }
}
