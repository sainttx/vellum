/*
 * Copyright Evan Summers
 * 
 */
package vellum.venigma;

import javax.crypto.spec.SecretKeySpec;
import vellum.util.Base64;

/**
 *
 * @author evan
 */
public class VenigmaApp {

    public static final VenigmaApp app = new VenigmaApp();
    public static final VenigmaConfig config = new VenigmaConfig();

    final SecretKeySpec secretKey; 
    
    public VenigmaApp() {
        try {
            secretKey = new SecretKeySpec(Base64.decode("ETO3KfrD464KrIOdvWQCRolZUF4qAwufjsyJoXG8SZk="), config.keyAlg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
