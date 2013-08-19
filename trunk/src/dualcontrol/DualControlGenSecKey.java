
package dualcontrol;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
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
    
    public static void main(String[] args) throws Exception {        
        new DualControlGenSecKey().start(args);
    }

    char[] getStorePass() {
        String storePasswordString = System.getProperty("storepass");
        if (storePasswordString != null) {
            return storePasswordString.toCharArray();
        } else {
            return System.console().readPassword("storepass: ");
        }
    }
    
    void start(String[] args) throws Exception {
        Map<String, char[]> dualMap = new DualControlReader().readDualMap(submissionCount);
        char[] storePassword = getStorePass();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(new FileInputStream(keyStorePath), storePassword);
        KeyGenerator keyGenerator = KeyGenerator.getInstance(keyAlg);
        keyGenerator.init(keySize);
        SecretKey secretKey = keyGenerator.generateKey();
        KeyStore.Entry entry = new KeyStore.SecretKeyEntry(secretKey);
        for (String dualAlias : dualMap.keySet()) {
            char[] dualPassword = dualMap.get(dualAlias);
            KeyStore.ProtectionParameter prot = new KeyStore.PasswordProtection(dualPassword);
            String alias = String.format("%s-%s", keyAlias, dualAlias);
            keyStore.setEntry(alias, entry, prot);
        }
        keyStore.store(new FileOutputStream(keyStorePath), storePassword);
    }    
}
