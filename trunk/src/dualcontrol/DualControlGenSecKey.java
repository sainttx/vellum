package dualcontrol;

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
    char[] keyStorePassword;
    Map<String, char[]> dualMap;
    KeyStore keyStore;
    SecretKey secretKey;

    public static void main(String[] args) throws Exception {
        new DualControlGenSecKey().start();
    }

    void start() throws Exception {
        dualMap = new DualControlReader().readDualMap(submissionCount);
        keyStorePassword = getKeyStorePassword();
        KeyGenerator keyGenerator = KeyGenerator.getInstance(keyAlg);
        keyGenerator.init(keySize);
        secretKey = keyGenerator.generateKey();
        keyStore = DualControlKeyStores.loadKeyStore(keyStorePath, keyStorePassword);
        KeyStore.Entry entry = new KeyStore.SecretKeyEntry(secretKey);
        for (String dualAlias : dualMap.keySet()) {
            char[] dualPassword = dualMap.get(dualAlias);
            String alias = keyAlias + "-" + dualAlias;
            if (true) {
                System.err.printf("DualControlGenSecKey %s %s %s %d [%s]\n",
                        new String(keyStorePassword), dualAlias, alias, dualPassword.length, new String(dualPassword));
            }
            KeyStore.ProtectionParameter prot = new KeyStore.PasswordProtection(dualPassword);
            keyStore.setEntry(alias, entry, prot);
        }
        keyStore.store(new FileOutputStream(keyStorePath), keyStorePassword);
    }

    char[] getKeyStorePassword() {
        String storePasswordString = System.getProperty("storepass");
        if (storePasswordString != null) {
            return storePasswordString.toCharArray();
        } else {
            return System.console().readPassword("storepass: ");
        }
    }
}
