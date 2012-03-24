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
            
    VProvider() {
        super("VProvider", 1.0, "Provides KeyStore.JKS");
        put("KeyStore.JKS", VKeyStoreSpi.class.getName());
        put("Cipher.AES", VCipherSpi.class.getName());
    }

    
}
