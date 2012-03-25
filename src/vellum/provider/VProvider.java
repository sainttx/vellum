/*
 * Copyright Evan Summers
 * 
 */
package vellum.provider;

import java.security.Provider;

/**
 *
 * @author evan
 */

public class VProvider extends Provider {
    
    public VProvider() {
        super("VProvider", 1.0, "Provides KeyStore.JCEKS");
        put("KeyStore.JCEKS", VKeyStoreSpi.class.getName());
        put("Cipher.AES", VCipherSpi.class.getName());
    }

    
}
