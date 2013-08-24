package dualcontrol;

import java.security.KeyStore;
import java.util.Arrays;
import java.util.Map;
import javax.crypto.SecretKey;

/**
 *
 * @author evans
 */
public class DualControlSessions {

    public static SecretKey loadKey(String keyStoreLocation, char[] keyStorePass, 
            String alias) throws Exception {
        char[] dualPass = null;
        try {
            KeyStore dualKeyStore = 
                    DualControlKeyStores.loadKeyStore(keyStoreLocation, keyStorePass);
            Map.Entry<String, char[]> entry = DualControlReader.readDualEntry(alias);
            String dualAlias = entry.getKey();
            dualPass = entry.getValue();
            alias = alias + "-" + dualAlias;
            System.err.println("DualControlSessions " + alias);
            return (SecretKey) dualKeyStore.getKey(alias, dualPass);
        } finally {
            if (dualPass != null) {
                Arrays.fill(dualPass, (char) 0);            
            }
        }        
    }
}
