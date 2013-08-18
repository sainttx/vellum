package dualcontrol;

import java.security.KeyStore;
import java.util.Arrays;
import java.util.Map;
import javax.crypto.SecretKey;
import org.apache.log4j.Logger;

/**
 *
 * @author evans
 */
public class DualControlSessions {

    public static SecretKey loadKey(String keyStorePath, char[] storePass, String alias) 
            throws Exception {
        char[] dualPass = null;
        try {
            KeyStore dualKeyStore = DualControlKeyStores.loadKeyStore(keyStorePath, storePass);
            Map.Entry<String, char[]> entry = DualControlReader.readDualEntry();
            String dualAlias = entry.getKey();
            dualPass = entry.getValue();
            return (SecretKey) dualKeyStore.getKey(alias + "-" + dualAlias, dualPass);
        } finally {
            if (dualPass != null) {
                Arrays.fill(dualPass, (char) 0);            
            }
        }        
    }
}
