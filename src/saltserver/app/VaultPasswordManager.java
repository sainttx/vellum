/*
 * Copyright Evan Summers
 * 
 */
package saltserver.app;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author evan
 */
public class VaultPasswordManager {
    private Map<String, char[]> passwordMap = new HashMap();

    public Map<String, char[]> getPasswordMap() {
        return passwordMap;
    }
    
    public void clear() {
        for (String key : passwordMap.keySet()) {
            char[] value = passwordMap.get(key);
            Arrays.fill(value, '0');
        }
        passwordMap.clear();
    }
}
