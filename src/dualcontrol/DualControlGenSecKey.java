
package dualcontrol;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.Map;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.apache.log4j.Logger;

/**
 *
 * @author evans
 */
public class DualControlGenSecKey { 
    final static Logger logger = Logger.getLogger(DualControlGenSecKey.class);
    
    int submissionCount = Integer.getInteger("dualcontrol.submissions", 3);
    String keyAlias = System.getProperty("alias"); 
    String keyStorePath = System.getProperty("keystore"); 
    String keyStoreType = System.getProperty("storetype");
    String keyAlg = System.getProperty("keyalg");
    int keySize = Integer.getInteger("keysize");
    
    char[] keyStorePassword;
    Map<String, char[]> dualMap;
    KeyStore keyStore;
    SecretKey secretKey;
    
    public static void main(String[] args) throws Exception {        
        new DualControlGenSecKey().start();
    }

    void start() throws Exception {
        dualMap = new DualControlReader().readDualMap(submissionCount);
        keyStorePassword = getStorePass();        
        KeyGenerator keyGenerator = KeyGenerator.getInstance(keyAlg);
        keyGenerator.init(keySize);
        secretKey = keyGenerator.generateKey();
        keyStore = KeyStore.getInstance(keyStoreType);
        loadKeyStore();
        KeyStore.Entry entry = new KeyStore.SecretKeyEntry(secretKey);
        for (String dualAlias : dualMap.keySet()) {
            char[] dualPassword = dualMap.get(dualAlias);
            KeyStore.ProtectionParameter prot = new KeyStore.PasswordProtection(dualPassword);
            String alias = String.format("%s-%s", keyAlias, dualAlias);
System.err.printf("DualControlGenSecKey %s %s\n", alias, new String(dualPassword));
            keyStore.setEntry(alias, entry, prot);
        }
        keyStore.store(new FileOutputStream(keyStorePath), keyStorePassword);
    }    

    char[] getStorePass() {
        String storePasswordString = System.getProperty("storepass");
        if (storePasswordString != null) {
            return storePasswordString.toCharArray();
        } else {
            return System.console().readPassword("storepass: ");
        }
    }
        
    void loadKeyStore() throws Exception {
        if (new File(keyStorePath).exists()) {
            keyStore.load(new FileInputStream(keyStorePath), keyStorePassword);
        } else {
            keyStore.load(null, keyStorePassword);
        }
    }
}
